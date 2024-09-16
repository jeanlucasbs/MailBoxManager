package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.MailBoxRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MailBoxServiceTest {

    @InjectMocks
    private MailBoxService service;

    @Mock
    private MailBoxRepository mailBoxRepository;

    private MailBoxModel mailBoxModel;
    private MailBoxDTO mailBoxDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mailBoxDTO = new MailBoxDTO();
        mailBoxDTO.setIdt(1);
        mailBoxDTO.setName("teste@teste.com");

        mailBoxModel = new MailBoxModel();
        mailBoxModel.setIdt(1);
        mailBoxModel.setName(mailBoxDTO.getName());
    }

    @Test
    void createMailBoxSuccessfully() {
        when(mailBoxRepository.findByName("teste@teste")).thenReturn(null);
        when(mailBoxRepository.save(any(MailBoxModel.class))).thenReturn(mailBoxModel);

        MailBoxDTO result = service.createMailBox(mailBoxDTO);

        assertNotNull(result);
        assertEquals(mailBoxDTO.getName(), result.getName());
    }

    @Test
    void createMailBoxInvalidEmail() {
        MailBoxDTO invalidMailBoxDTO = new MailBoxDTO("invalid-email");

        assertThrows(InvalidNameException.class, () -> {
            service.createMailBox(invalidMailBoxDTO);
        });

        verify(mailBoxRepository, never()).save(any(MailBoxModel.class));
    }

    @Test
    void createMailBoxAlreadyExists() {
        when(mailBoxRepository.findByName(mailBoxDTO.getName())).thenReturn(mailBoxModel);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.createMailBox(mailBoxDTO);
        });

        verify(mailBoxRepository, never()).save(any(MailBoxModel.class));
    }

    @Test
    void getAllMainBoxesSuccessfully() {
        MailBoxModel mailBoxModel1 = new MailBoxModel();
        mailBoxModel1.setName("teste@teste.com");
        MailBoxModel mailBoxModel2 = new MailBoxModel();
        mailBoxModel2.setName("mailbox@teste.com");

        List<MailBoxModel> mailBoxModels = Arrays.asList(mailBoxModel1, mailBoxModel2);

        when(mailBoxRepository.findAll()).thenReturn(mailBoxModels);

        List<MailBoxDTO> result = service.getAllMainBoxes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mailBoxDTO.getName(), result.get(0).getName());
    }

    @Test
    void getAllMainBoxesWithPaginationSuccessfully() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MailBoxModel> mailBoxPage = new PageImpl<>(Collections.singletonList(mailBoxModel));

        when(mailBoxRepository.findAll(pageable)).thenReturn(mailBoxPage);

        Page<MailBoxDTO> result = service.getAllMainBoxes(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mailBoxDTO.getName(), result.getContent().get(0).getName());
    }
}