package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.InvalidSubjectException;
import com.jeanlucas.mailboxmanager.Exception.ResourceNotFoundException;
import com.jeanlucas.mailboxmanager.Mappers.MessageMapper;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Models.MessageModel;
import com.jeanlucas.mailboxmanager.Repositories.FolderRepository;
import com.jeanlucas.mailboxmanager.Repositories.MailBoxRepository;
import com.jeanlucas.mailboxmanager.Repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MailBoxRepository mailBoxRepository;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private MessageMapper mapper;

    private MessageModel messageModel;
    private MessageDTO messageDTO;
    private MailBoxModel mailBoxModel;
    private FolderModel folderModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mailBoxModel = new MailBoxModel();
        mailBoxModel.setIdt(1);
        mailBoxModel.setName("teste@teste.com");

        messageDTO = new MessageDTO();
        messageDTO.setSender("sender@teste.com");
        messageDTO.setRecipient("recipient@teste.com");
        messageDTO.setSubject("Test Subject");
        messageDTO.setBody("Test Body");

        folderModel = new FolderModel();
        folderModel.setIdt(1);
        folderModel.setName("SENT");
        folderModel.setMailbox(mailBoxModel);

        messageModel = new MessageModel();
        messageModel.setIdt(1);
        messageModel.setSender("sender@teste.com");
        messageModel.setRecipient("recipient@teste.com");
        messageModel.setSubject("Test Subject");
        messageModel.setFolder(folderModel);
    }

    @Test
    void sendMessageSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(folderModel.getName(), mailBoxModel)).thenReturn(folderModel);
        when(mapper.toModel(messageDTO)).thenReturn(messageModel);

        messageService.sendMessage("teste@teste.com", messageDTO);

        verify(messageRepository, times(1)).save(messageModel);
    }

    @Test
    void sendMessageInvalidName() {
        messageDTO.setRecipient("invalid-name");

        InvalidNameException ex = assertThrows(InvalidNameException.class, () -> {
            messageService.sendMessage(mailBoxModel.getName(), messageDTO);
        });

        assertEquals("Formato de email inválido.", ex.getMessage());
    }

    @Test
    void sendMessageMailboxNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.sendMessage("teste@teste.com", messageDTO);
        });

        assertEquals("Caixa de email não existe.", ex.getMessage());
    }

    @Test
    void sendMessageSentFolderNotFound() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(anyString(), any(MailBoxModel.class))).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.sendMessage("teste@teste.com", messageDTO);
        });

        assertEquals("Pasta 'SENT' não encontrada para a caixa de e-mail", ex.getMessage());
    }

    @Test
    void sendMessageSentSubjectSuccessfuly() {
        String validSubject = "Este é um valido assunto.";
        messageDTO.setSubject(validSubject);

        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(folderModel.getName(), mailBoxModel)).thenReturn(folderModel);
        when(mapper.toModel(messageDTO)).thenReturn(messageModel);

        messageService.sendMessage("teste@teste.com", messageDTO);

        verify(messageRepository, times(1)).save(messageModel);
    }

    @Test
    void sendMessageSentSubjectInvalid() {
        String validSubject = "Este é um assunto que ultrapassa cinquenta caracteres. Assunto não é valido.";
        messageDTO.setSubject(validSubject);

        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(folderModel.getName(), mailBoxModel)).thenReturn(folderModel);

        InvalidSubjectException ex = assertThrows(InvalidSubjectException.class, () -> {
            messageService.sendMessage(mailBoxModel.getName(), messageDTO);
        });

        assertEquals("O assunto deve ter no máximo 50 caracteres.", ex.getMessage());
    }

    @Test
    void saveReceiveMesssageSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(messageDTO.getFolder(), mailBoxModel)).thenReturn(folderModel);
        when(messageRepository.save(Mockito.any(MessageModel.class))).thenReturn(messageModel);

        messageService.saveReceiveMesssage(mailBoxModel.getName(), messageDTO);

        verify(messageRepository, Mockito.times(1)).save(Mockito.any(MessageModel.class));
    }

    @Test
    void saveReceiveMesssageInvalidName() {
        messageDTO.setSender("invalid");

        InvalidNameException ex = assertThrows(InvalidNameException.class, () -> {
            messageService.saveReceiveMesssage(mailBoxModel.getName(), messageDTO);
        });

        assertEquals("Formato de email inválido.", ex.getMessage());
    }

    @Test
    void saveReceiveMesssageMailboxNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.saveReceiveMesssage("teste@teste.com", messageDTO);
        });

        assertEquals("Caixa de email não existe.", ex.getMessage());
    }

    @Test
    void saveReceiveMesssageSentFolderNotFound() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(anyString(), any(MailBoxModel.class))).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.saveReceiveMesssage("teste@teste.com", messageDTO);
        });

        assertEquals("Pasta não encontrada para a caixa de e-mail", ex.getMessage());
    }

    @Test
    void updateMessageReadStatusSuccefully() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findById(anyInt())).thenReturn(Optional.of(folderModel));
        when(messageRepository.findByIdtAndFolder_IdtAndFolder_Mailbox_Name(anyInt(), anyInt(), anyString()))
                .thenReturn(Optional.of(messageModel));

        messageDTO.setRead(true);
        messageService.updateMessageReadStatus("test@example.com", 1, 1, messageDTO);

        assertTrue(messageModel.isRead());
        verify(messageRepository, times(1)).save(messageModel);
    }

    @Test
    void updateMessageReadStatusInvalidName() {
        InvalidNameException ex = assertThrows(InvalidNameException.class, () -> {
            messageService.updateMessageReadStatus("test@example", folderModel.getIdt(), messageModel.getIdt(), messageDTO);
        });

        assertEquals("Nome da caixa inválido.", ex.getMessage());
    }

    @Test
    void updateMessageReadStatusMailboxNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.updateMessageReadStatus(mailBoxModel.getName(), folderModel.getIdt(), messageModel.getIdt(), messageDTO);
        });

        assertEquals("Caixa de email não existe.", ex.getMessage());
    }

    @Test
    void updateMessageReadStatusSentFolderNotFound() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(anyString(), any(MailBoxModel.class))).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.updateMessageReadStatus(mailBoxModel.getName(), folderModel.getIdt(), messageModel.getIdt(), messageDTO);
        });

        assertEquals("Pasta não encontrada para a caixa de e-mail", ex.getMessage());
    }

    @Test
    void getMessagesByMainBoxAndFolderInvalidName() {
        mailBoxModel.setName("invalid");

        InvalidNameException ex = assertThrows(InvalidNameException.class, () -> {
            messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT");
        });

        assertEquals("Nome da caixa inválido.", ex.getMessage());
    }

    @Test
    void getMessagesByMainBoxAndMailBoxNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT");
        });

        assertEquals("Caixa de email não existe.", ex.getMessage());
    }

    @Test
    void getMessagesByMainBoxAndFolderNotFound() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(anyString(), any(MailBoxModel.class))).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT");
        });

        assertEquals("Pasta não encontrada para a caixa de e-mail", ex.getMessage());
    }

    @Test
    void getMessagesByMainBoxAndFolderSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByName(folderModel.getName())).thenReturn(Optional.of(folderModel));
        when(messageRepository.findByFolder_NameAndFolder_Mailbox_Name(folderModel.getName(), mailBoxModel.getName()))
                .thenReturn(Arrays.asList(messageModel));

        List<MessageDTO> messages = messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT");

        assertEquals(1, messages.size());
        verify(messageRepository, times(1)).findByFolder_NameAndFolder_Mailbox_Name(folderModel.getName(), mailBoxModel.getName());

    }

    @Test
    public void getMessagesByMainBoxAndFolderPagedSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByName(folderModel.getName())).thenReturn(Optional.of(folderModel));

        MessageModel message1 = new MessageModel();
        MessageModel message2 = new MessageModel();

        List<MessageModel> messageList = Arrays.asList(message1, message2);
        Page<MessageModel> messagePage = new PageImpl<>(messageList);
        Pageable pageable = PageRequest.of(0, 10);

        when(messageRepository.findByFolder_NameAndFolder_Mailbox_Name(folderModel.getName(), mailBoxModel.getName(), pageable))
                .thenReturn(messagePage);

        Page<MessageDTO> result = messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT", pageable);

        assertNotNull(result);
        verify(messageRepository, times(1)).findByFolder_NameAndFolder_Mailbox_Name(folderModel.getName(), mailBoxModel.getName(), pageable);
    }

    @Test
    void getMessagesByMainBoxAndFolderPagedNotFoundWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);

        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessagesByMainBoxAndFolder(mailBoxModel.getName(), "SENT", pageable);
        });

        verify(messageRepository, never()).findByFolder_NameAndFolder_Mailbox_Name("SENT", mailBoxModel.getName(), pageable);
    }

    @Test
    void getMessageDetailSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByName(folderModel.getName())).thenReturn(Optional.of(folderModel));
        when(messageRepository.findByIdtAndFolder_NameAndFolder_Mailbox_Name(messageModel.getIdt(), folderModel.getName(), mailBoxModel.getName()))
                .thenReturn(Optional.of(messageModel));
        when(mapper.toDTO(messageModel)).thenReturn(messageDTO);

        MessageDTO messages = messageService.getMessageDetail("teste@teste.com", "SENT", messageModel.getIdt());

        verify(messageRepository, times(1)).findByIdtAndFolder_NameAndFolder_Mailbox_Name(messageModel.getIdt(), folderModel.getName(), mailBoxModel.getName());
        assertNotNull(messages);
        assertEquals(messageDTO.getIdt(), messages.getIdt());
        assertEquals(messageDTO.getSender(), messages.getSender());
        assertEquals(messageDTO.getSubject(), messages.getSubject());
        assertEquals(messageDTO.getSendAt(), messages.getSendAt());
        assertEquals(messageDTO.isRead(), messages.isRead());
    }

    @Test
    void getMessageDetailInvalidName() {
        mailBoxModel.setName("invalid");

        InvalidNameException ex = assertThrows(InvalidNameException.class, () -> {
            messageService.getMessageDetail(mailBoxModel.getName(), "SENT", messageModel.getIdt());
        });

        assertEquals("Nome da caixa inválido.", ex.getMessage());
    }

    @Test
    void getMessageDetailNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessageDetail(mailBoxModel.getName(), "SENT", messageModel.getIdt());
        });

        assertEquals("Caixa de email não existe.", ex.getMessage());
    }

    @Test
    void getMessageDetailFolderNotFound() {
        when(mailBoxRepository.findByName(anyString())).thenReturn(mailBoxModel);
        when(folderRepository.findByNameAndMailbox(anyString(), any(MailBoxModel.class))).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessageDetail(mailBoxModel.getName(), "SENT", messageModel.getIdt());
        });

        assertEquals("Pasta não encontrada para a caixa de e-mail", ex.getMessage());
    }

    @Test
    void getMessageDetailMessageNotFound() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);
        when(folderRepository.findByName(folderModel.getName())).thenReturn(Optional.of(folderModel));
        when(messageRepository.findByIdtAndFolder_NameAndFolder_Mailbox_Name(messageModel.getIdt(), folderModel.getName(), mailBoxModel.getName()))
                .thenReturn(Optional.of(messageModel));
        when(mapper.toDTO(messageModel)).thenReturn(messageDTO);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            messageService.getMessageDetail(mailBoxModel.getName(), "SENT", anyInt());
        });

        assertEquals("Mensagem não encontrada.", ex.getMessage());
    }
}