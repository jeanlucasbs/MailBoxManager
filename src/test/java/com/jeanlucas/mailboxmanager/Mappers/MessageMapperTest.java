package com.jeanlucas.mailboxmanager.Mappers;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Models.MessageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageMapperTest {

    @InjectMocks
    private MessageMapper messageMapper;

    private MessageDTO messageDTO;
    private MessageModel messageModel;

    private LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        messageMapper = new MessageMapper();
    }

    @Test
    public void testToModel() {
         messageDTO = MessageDTO.builder()
                .idt(1)
                .sender("jean@teste.com")
                .recipient("recipient@teste.com")
                .subject("Test Subject")
                .body("Test Body")
                .sendAt(localDateTime)
                .read(false)
                .build();

        MessageModel messageModel = messageMapper.toModel(messageDTO);

        assertNotNull(messageModel);
        assertEquals(messageDTO.getIdt(), messageModel.getIdt());
        assertEquals(messageDTO.getSender(), messageModel.getSender());
        assertEquals(messageDTO.getRecipient(), messageModel.getRecipient());
        assertEquals(messageDTO.getSubject(), messageModel.getSubject());
        assertEquals(messageDTO.getBody(), messageModel.getBody());
        assertEquals(messageDTO.getSendAt(), messageModel.getSendAt());
        assertEquals(messageDTO.isRead(), messageModel.isRead());
    }

    @Test
    public void testToDTO() {
        messageModel = MessageModel.builder()
                .idt(1)
                .sender("sender@teste.com")
                .recipient("recipient@teste.com")
                .subject("Test Subject")
                .body("Test Body")
                .sendAt(localDateTime)
                .read(true)
                .build();

        MessageDTO messageDTO = messageMapper.toDTO(messageModel);

        assertNotNull(messageDTO);
        assertEquals(messageModel.getIdt(), messageDTO.getIdt());
        assertEquals(messageModel.getSender(), messageDTO.getSender());
        assertEquals(messageModel.getRecipient(), messageDTO.getRecipient());
        assertEquals(messageModel.getSubject(), messageDTO.getSubject());
        assertEquals(messageModel.getBody(), messageDTO.getBody());
        assertEquals(messageModel.getSendAt(), messageDTO.getSendAt());
        assertEquals(messageModel.isRead(), messageDTO.isRead());
    }
}