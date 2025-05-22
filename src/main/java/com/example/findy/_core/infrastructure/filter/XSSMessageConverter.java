package com.example.findy._core.infrastructure.filter;

import java.io.IOException;
import java.util.Arrays;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class XSSMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Safelist whiteList   = Safelist.relaxed();
    private static       ObjectMapper   objectMapper;

    private static final String[] EXCLUDE_PATH = {
            "/files/upload-urls",
            "/test/**", // TODO 문성하 삭제
    };

    public XSSMessageConverter(ObjectMapper objectMapper) {
        super();
        XSSMessageConverter.objectMapper = objectMapper;
    }

    @Override
    @NonNull
    protected ObjectWriter customizeWriter(@NonNull ObjectWriter writer, JavaType javaType, MediaType contentType) {
        return new JsonConverter(writer, writer.getConfig());
    }

    private static class JsonConverter extends ObjectWriter {

        protected JsonConverter(ObjectWriter base, SerializationConfig config) {
            super(base, config);
        }

        @Override
        public void writeValue(JsonGenerator gen, Object value) throws IOException {
            // 현재 쓰레드(요청) 컨텍스트에서 HttpServletRequest를 가져온다
            if (RequestContextHolder.currentRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String uri = request.getRequestURI();

                // 1) “EXCLUDE_PATH 중 하나라도 매칭되면” → 필터링 생략
                boolean shouldExclude = Arrays
                        .stream(EXCLUDE_PATH)
                        .anyMatch(excludePattern -> pathMatcher.match(excludePattern, uri));

                if (shouldExclude) { // 필터링 스킵: 그냥 원본 value 를 직렬화 후 반환
                    super.writeValue(gen, value);
                    return;
                }
            }

            ObjectNode objectNode = objectMapper.valueToTree(value);
            Object sanitizedValue = sanitizeJsonNode(objectNode);
            super.writeValue(gen, sanitizedValue);
        }

        private boolean isUrl(String text) {
            return text.startsWith("http://") || text.startsWith("https://");
        }

        private Object sanitizeJsonNode(ObjectNode node) {
            node.fieldNames().forEachRemaining(fieldName -> {
                JsonNode childNode = node.get(fieldName);
                if (childNode.isTextual()) {
                    String value = childNode.asText();

                    // 문자열이 URL이면 필터링 제외
                    if (!isUrl(value)) {
                        String sanitizedValue = Jsoup.clean(value, whiteList);
                        node.put(fieldName, sanitizedValue);
                    }
                } else if (childNode.isArray()) {
                    ArrayNode arrayNode = (ArrayNode) childNode;
                    for (int i = 0; i < arrayNode.size(); i++) {
                        JsonNode arrayElement = arrayNode.get(i);
                        if (arrayElement.isObject()) {
                            sanitizeJsonNode((ObjectNode) arrayElement);
                        } else if (arrayElement.isTextual()) {
                            String value = arrayElement.asText();

                            // 2문자열이 URL이면 필터링 제외
                            if (!isUrl(value)) {
                                String sanitizedValue = Jsoup.clean(value, whiteList);
                                if (!sanitizedValue.isEmpty()) {
                                    arrayNode.set(i, sanitizedValue);
                                } else {
                                    arrayNode.remove(i);
                                    i--;
                                }
                            }
                        } else {
                            break;
                        }
                    }
                } else if (childNode.isObject()) {
                    sanitizeJsonNode((ObjectNode) childNode);
                }
            });
            return node;
        }
    }
}
