package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    private MessageDTO messageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        messageDTO = new MessageDTO();
        messageDTO.setIdt(1);
        messageDTO.setSubject("Test Subject");
        messageDTO.setBody("Test Body");
    }

    @Test
    void sendMessageSuccessfully() {
        doNothing().when(messageService).sendMessage("jean@teste.com", messageDTO);

        ResponseEntity<Object> response = messageController.sendMessage("jean@teste.com", messageDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Mensagem armazenada.", response.getBody());
        verify(messageService, times(1)).sendMessage("jean@teste.com", messageDTO);
    }

    @Test
    void saveReceiveMesssageSuccessfully() {
        doNothing().when(messageService).saveReceiveMesssage("jean@teste.com", messageDTO);

        ResponseEntity<Object> response = messageController.saveReceiveMesssage("jean@teste.com", messageDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Mensagem armazenada.", response.getBody());
        verify(messageService, times(1)).saveReceiveMesssage("jean@teste.com", messageDTO);
    }

    @Test
    void updateMessageReadStatusSuccessfully() {
        doNothing().when(messageService).updateMessageReadStatus("jean@teste.com", 1, 1, messageDTO);

        ResponseEntity<Object> response = messageController.updateMessageReadStatus("jean@teste.com", 1, 1, messageDTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(messageService, times(1)).updateMessageReadStatus("jean@teste.com", 1, 1, messageDTO);
    }

    @Test
    void getMessageDetailSuccessfully() {
        when(messageService.getMessageDetail("jean@teste.com", "INBOX", 1)).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> response = messageController.getMessageDetail("jean@teste.com", "INBOX", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Subject", response.getBody().getSubject());
        assertEquals("Test Body", response.getBody().getBody());
        verify(messageService, times(1)).getMessageDetail("jean@teste.com", "INBOX", 1);

    }

    @Test
    void getMessagesByMainBoxAndFolderuccessfully() {
        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setIdt(1);
        messageDTO1.setSubject("Test Subject 2");
        messageDTO1.setBody("Test Body 2");
        List<MessageDTO> messages = List.of(messageDTO, messageDTO1);

        when(messageService.getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX")).thenReturn(messages);
        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(messageService, times(1)).getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX");
    }

    @Test
    void testGetMessagesByMainBoxAndFolderPaginationSuccessfully() {
        MessageDTO messageDTO1 = new MessageDTO();
        messageDTO1.setIdt(1);
        messageDTO1.setSubject("Test Subject 2");
        messageDTO1.setBody("Test Body 2");
        List<MessageDTO> messages = List.of(messageDTO, messageDTO1);
        Page<MessageDTO> pageMessages = new PageImpl<>(messages);

        Pageable pageable = PageRequest.of(0, 10);
        when(messageService.getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX", pageable)).thenReturn(pageMessages);
        ResponseEntity<Page<MessageDTO>> response = messageController.getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
        verify(messageService, times(1)).getMessagesByMainBoxAndFolder("jean@teste.com", "INBOX", pageable);
    }
}