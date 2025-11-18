package com.example.findy.api.game.origin.controller;

import com.example.findy._testutils.ControllerTest;
import com.example.findy.api.game.origin.service.OriginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OriginController.class)
class OriginControllerTest extends ControllerTest {

    @MockitoBean
    private OriginService originService;

    @Test
    void updateHeart() throws Exception {
    }

    @Test
    void getScore() throws Exception {
    }
}