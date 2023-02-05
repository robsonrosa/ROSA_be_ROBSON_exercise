package com.ecore.roles.web.rest;

import com.ecore.roles.exception.ErrorResponse;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.function.Supplier;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultExceptionHandlerTest {

    private final DefaultExceptionHandler handler = new DefaultExceptionHandler();

    @Test
    public void shouldHaveControllerAdviceAnnotation() {
        assertTrue(DefaultExceptionHandler.class.isAnnotationPresent(ControllerAdvice.class));
    }

    @Test
    public void shouldHaveExceptionHandlerAnnotationInAllPublicMethods() {
        assertTrue(Arrays.stream(DefaultExceptionHandler.class.getDeclaredMethods())
                .filter(ReflectionUtils::isPublic)
                .allMatch(method -> method.isAnnotationPresent(ExceptionHandler.class)));
    }

    @Test
    public void shouldHandleResourceNotFoundException() {
        final ResourceNotFoundException exception = new ResourceNotFoundException(String.class, randomUUID());
        handleException(404, exception.getMessage(), () -> handler.handle(exception));
    }

    @Test
    public void shouldHandleResourceExistsException() {
        final ResourceExistsException exception = new ResourceExistsException(String.class);
        handleException(400, exception.getMessage(), () -> handler.handle(exception));
    }

    @Test
    public void shouldHandleInvalidArgumentException() {
        final InvalidArgumentException exception = new InvalidArgumentException(String.class);
        handleException(400, exception.getMessage(), () -> handler.handle(exception));
    }

    private void handleException(
            int status,
            String message,
            Supplier<ResponseEntity<ErrorResponse>> supplier) {

        final ResponseEntity<ErrorResponse> actual = supplier.get();

        assertEquals(status, actual.getStatusCodeValue());
        assertEquals(status, actual.getBody().getStatus());
        assertEquals(message, actual.getBody().getError());
    }

}
