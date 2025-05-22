package com.example.findy.api._common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApiController {
    @GetMapping("/api")
    public String index() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/docs/index.html");
        return new String(resource.getInputStream().readAllBytes());
    }
}
