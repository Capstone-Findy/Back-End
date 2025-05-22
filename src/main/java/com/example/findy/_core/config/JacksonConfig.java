package com.example.findy._core.config;

import com.example.findy._core.infrastructure.filter.XSSMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Configuration
@AllArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class JacksonConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new XSSMessageConverter(objectMapper);
    }
}
