package com.example.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import static com.example.application.Application.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    @Test
    void mainPageRuns() {
        assertEquals(0, uNames.size());
        assertEquals(0, uRoles.size());
        assertEquals(0, passCodes.size());
    }
}