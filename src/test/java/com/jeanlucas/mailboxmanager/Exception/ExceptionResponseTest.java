package com.jeanlucas.mailboxmanager.Exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExceptionResponseTest {

    @InjectMocks
    private ExceptionResponse exceptionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExceptionResponseConstructor() {
        Date now = new Date();
        String message = "Test message";
        String details = "Test details";

        ExceptionResponse exceptionResponse = new ExceptionResponse(now, message, details);

        assertNotNull(exceptionResponse);
        assertEquals(now, exceptionResponse.getTimestamp());
        assertEquals(message, exceptionResponse.getMessage());
        assertEquals(details, exceptionResponse.getDetails());
    }

    @Test
    public void testGetters() {
        Date now = new Date();
        String message = "Test message";
        String details = "Test details";
        ExceptionResponse exceptionResponse = new ExceptionResponse(now, message, details);

        assertEquals(now, exceptionResponse.getTimestamp());
        assertEquals(message, exceptionResponse.getMessage());
        assertEquals(details, exceptionResponse.getDetails());
    }
}