package com.example.findy._testutils;

import com.example.findy._core.config.JacksonConfig;
import com.example.findy._core.infrastructure.filter.HttpServletWrapperFilter;
import com.example.findy._testutils.fixture.PropertiesFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.reflect.Field;
import java.util.Collection;

@AutoConfigureRestDocs
@Import({JacksonConfig.class})
@ExtendWith({RestDocumentationExtension.class})
public abstract class ControllerTest {

    public ObjectMapper objectMapper;
    public MockMvc      mockMvc;

    public ControllerTest() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @BeforeEach
    public void setUp(
            final WebApplicationContext webApplicationContext,
            final RestDocumentationContextProvider restDocumentationContextProvider
    ) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .addFilters(new HttpServletWrapperFilter(PropertiesFixture.securityProperties().cookie()))
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(
                        MockMvcRestDocumentation
                                .documentationConfiguration(restDocumentationContextProvider)
                                .operationPreprocessors()
                                .withRequestDefaults(
                                        Preprocessors.prettyPrint(),
                                        Preprocessors
                                                .modifyUris()
                                                .scheme("https")
                                                .host("api.alignmiracle.com")
                                                .removePort()
                                )
                                .withResponseDefaults(Preprocessors.prettyPrint())
                )
                .build();
    }

    protected MultiValueMap<String, String> toMultiValueMap(Object object) throws IllegalAccessException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String key = field.getName();
            Object value = field.get(object);

            if (value instanceof Collection) {
                ((Collection<?>) value).forEach(v -> params.add(key, v.toString()));
            } else if (value != null) {
                params.add(key, value.toString());
            }
        }

        return params;
    }

}
