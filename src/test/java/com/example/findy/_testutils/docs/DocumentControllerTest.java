package com.example.findy._testutils.docs;

import com.example.findy.CustomResponseFieldsSnippet;
import com.example.findy._testutils.ApiDocumentUtils;
import com.example.findy._testutils.ControllerTest;
import com.findy.processor.RestDocs;
import com.findy.processor.RestDocsIgnore;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.findy._testutils.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest extends ControllerTest {

    @Test
    public void commons() throws Exception {
        this.mockMvc
                .perform(ApiDocumentUtils.get("/docs").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "enum", customResponseFields(
                                "common-response",
                                null,
                                attributes(key("title").value("공통 응답")),
                                fieldWithPath("error").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지").optional(),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                                subsectionWithPath("pageInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("페이지 정보")
                                        .optional()
                        ), responseFieldsForAllEnums()
                ));
    }

    @Test
    void page() throws Exception {
        this.mockMvc
                .perform(ApiDocumentUtils.get("/docs/page").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document(
                        "page", customResponseFields(
                                "common-response",
                                beneathPath("pageInfo").withSubsectionId("pageInfo"),
                                attributes(key("title").value("페이지 정보")),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 개수"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                fieldWithPath("hasPrevious").type(JsonFieldType.BOOLEAN).description("이전 페이지"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지")
                        )
                ));
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            PayloadSubsectionExtractor<?> subsectionExtractor,
            Map<String, Object> attributes,
            FieldDescriptor... descriptors
    ) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }

    @SuppressWarnings("rawtypes")
    public static CustomResponseFieldsSnippet[] responseFieldsForAllEnums() throws IOException {
        Reflections reflections = new Reflections("com.example.findy");
        Set<Class<? extends Enum>> enums = reflections.getSubTypesOf(Enum.class);

        Path enumDocsDir = Paths.get("src/docs/asciidoc/enum");
        Files.createDirectories(enumDocsDir);

        System.out.println("찾은 Enum 수: " + enums.size());
        enums.forEach(e -> System.out.println("📌 " + e.getName()));
        return enums
                .stream()
                .filter(enumClass -> enumClass.getAnnotation(RestDocsIgnore.class) == null)
                .peek(enumClass -> {
                    try {
                        System.out.println(enumClass.getName());
                        Path enumFile = enumDocsDir.resolve(enumClass.getSimpleName() + ".adoc");
                        String content = String.format(
                                ":docinfo: shared-head\n:docinfodir: ../\n\ninclude::{snippets}/enum/enum-response-fields-%s.adoc[]",
                                enumClass.getSimpleName()
                        );
                        Files.writeString(enumFile, content);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create enum doc file", e);
                    }
                })
                .map(DocumentControllerTest::responseFieldsForEnum)
                .toArray(CustomResponseFieldsSnippet[]::new);
    }

    @SuppressWarnings("rawtypes")
    private static CustomResponseFieldsSnippet responseFieldsForEnum(
            Class<? extends Enum> enumClass
    ) {
        String name = enumClass.getSimpleName();
        return new CustomResponseFieldsSnippet(
                "enum-response",
                beneathPath("data".concat("." + name)).withSubsectionId(name),
                List.of(enumConvertFieldDescriptor(enumClass)),
                attributes(key("title").value(name)),
                true
        );
    }

    @SuppressWarnings("rawtypes")
    private static FieldDescriptor[] enumConvertFieldDescriptor(Class<? extends Enum> enumClass) {
        return Arrays.stream(enumClass.getDeclaredFields()).filter(Field::isEnumConstant).map(field -> {
            RestDocs restDocs = field.getAnnotation(RestDocs.class);
            String description = restDocs.value();

            return PayloadDocumentation
                    .fieldWithPath(field.getName())
                    .description(description)
                    .type(JsonFieldType.STRING);
        }).toArray(FieldDescriptor[]::new);
    }
}
