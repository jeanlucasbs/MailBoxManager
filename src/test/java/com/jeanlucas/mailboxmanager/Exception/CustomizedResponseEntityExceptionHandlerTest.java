package com.jeanlucas.mailboxmanager.Exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomizedResponseEntityExceptionHandlerTest {

    @InjectMocks
    private CustomizedResponseEntityExceptionHandler customizedResponseEntityExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void handleAllExceptions() {
        Exception ex = new Exception("Some error message");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/triggerException");

        ResponseEntity<ExceptionResponse> responseEntity = customizedResponseEntityExceptionHandler.handleAllExceptions(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ExceptionResponse exceptionResponse = responseEntity.getBody();
        assertNotNull(exceptionResponse);
    }

    @Test
    public void testHandleNotFoundExceptions() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/resourceNotFound");

        ResponseEntity<ExceptionResponse> responseEntity = customizedResponseEntityExceptionHandler.handleNotFoundExceptions(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleAlreadyExistsExceptions() {
        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("Recurso já encontrado");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/resourceAlreadyExists");

        ResponseEntity<ExceptionResponse> responseEntity = customizedResponseEntityExceptionHandler.handleAlreadyExistsExceptions(ex, request);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        ExceptionResponse exceptionResponse = responseEntity.getBody();
        assertNotNull(exceptionResponse);
    }

    @Test
    public void testHandleInvalidMailBoxNameException() {
        InvalidNameException ex = new InvalidNameException("Formato de email inválido.");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/invalidMailboxName");

        ResponseEntity<ExceptionResponse> responseEntity = customizedResponseEntityExceptionHandler.handleInvalidMailBoxNameException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse exceptionResponse = responseEntity.getBody();
        assertNotNull(exceptionResponse);
    }

    @Test
    public void testHandleInvalidSubjectException() {
        InvalidSubjectException ex = new InvalidSubjectException("O assunto deve ter no máximo 50 caracteres.");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/invalidSubject");

        ResponseEntity<ExceptionResponse> responseEntity = customizedResponseEntityExceptionHandler.handleInvalidSubjectException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponse exceptionResponse = responseEntity.getBody();
        assertNotNull(exceptionResponse);
    }
}