package com.example.findy.api._common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final RedisConnectionFactory redisConnectionFactory;

    @GetMapping("/redis/health")
    public String checkRedisHealth() {
        try {
            var connection = redisConnectionFactory.getConnection();
            String pong = connection.ping();
            connection.close();

            return "Redis PING: " + pong;  // 정상일 때 PONG
        } catch (Exception e) {
            return "FAILED: " + e.getMessage();
        }
    }

    @GetMapping("/api")
    public String index() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/docs/index.html");
        return new String(resource.getInputStream().readAllBytes());
    }

}
