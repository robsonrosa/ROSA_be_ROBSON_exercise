package com.ecore.roles.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static org.junit.jupiter.api.Assertions.*;

class DefaultExceptionHandlerTest {

    private DefaultExceptionHandler handler = new DefaultExceptionHandler();

    @Test
    public void shouldHaveControllerAdviceAnnotation() {
        assertTrue(DefaultExceptionHandler.class.isAnnotationPresent(ControllerAdvice.class));
    }

    @Test
    public void shouldHandle() {
        assertTrue(DefaultExceptionHandler.class.isAnnotationPresent(ControllerAdvice.class));
    }

}
