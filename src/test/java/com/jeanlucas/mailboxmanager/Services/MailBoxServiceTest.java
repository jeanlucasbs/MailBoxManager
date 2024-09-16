package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.MailBoxRepository;
import org.junit.jupiter.api.Assertions;
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
import java.util.Collections;
import java.util.List;

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

        mailBoxDTO = new MailBoxDTO(1, "teste@teste.com");

        mailBoxModel = new MailBoxModel();
        mailBoxModel.setIdt(1);
        mailBoxModel.setName(mailBoxDTO.getName());
    }

    @Test
    void createMailBoxSuccess() {
        Mockito.when(mailBoxRepository.findByName("teste@teste")).thenReturn(null);
        Mockito.when(mailBoxRepository.save(Mockito.any(MailBoxModel.class))).thenReturn(mailBoxModel);

        MailBoxModel result = service.createMailBox(mailBoxDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mailBoxDTO.getName(), result.getName());
    }

    @Test
    void createMailBoxInvalidEmail() {
        MailBoxDTO invalidMailBoxDTO = new MailBoxDTO("invalid-email");

        Assertions.assertThrows(InvalidNameException.class, () -> {
            service.createMailBox(invalidMailBoxDTO);
        });

        Mockito.verify(mailBoxRepository, Mockito.never()).save(Mockito.any(MailBoxModel.class));
    }

    @Test
    void createMailBoxAlreadyExists() {
        Mockito.when(mailBoxRepository.findByName(mailBoxDTO.getName())).thenReturn(mailBoxModel);

        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.createMailBox(mailBoxDTO);
        });

        Mockito.verify(mailBoxRepository, Mockito.never()).save(Mockito.any(MailBoxModel.class));
    }

    @Test
    void getAllMainBoxesSuccess() {
        MailBoxModel mailBoxModel1 = new MailBoxModel();
        mailBoxModel1.setName("teste@teste.com");
        MailBoxModel mailBoxModel2 = new MailBoxModel();
        mailBoxModel2.setName("mailbox@teste.com");

        List<MailBoxModel> mailBoxModels = Arrays.asList(mailBoxModel1, mailBoxModel2);

        Mockito.when(mailBoxRepository.findAll()).thenReturn(mailBoxModels);

        List<MailBoxDTO> result = service.getAllMainBoxes();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(mailBoxDTO.getName(), result.get(0).getName());
    }

    @Test
    void getAllMainBoxesWithPaginationSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MailBoxModel> mailBoxPage = new PageImpl<>(Collections.singletonList(mailBoxModel));

        Mockito.when(mailBoxRepository.findAll(pageable)).thenReturn(mailBoxPage);

        Page<MailBoxDTO> result = service.getAllMainBoxes(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(mailBoxDTO.getName(), result.getContent().get(0).getName());
    }
}