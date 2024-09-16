package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Services.MailBoxService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MailBoxControllerTest {

    @InjectMocks
    private MailBoxController mailBoxController;

    @Mock
    private MailBoxService mailBoxService;

    private MailBoxModel mailBoxModel;
    private MailBoxDTO mailBoxDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mailBoxModel = new MailBoxModel();
        mailBoxModel.setIdt(1);
        mailBoxModel.setName("jean@hotmail.com");

        mailBoxDTO = new MailBoxDTO();
        mailBoxDTO.setIdt(1);
        mailBoxDTO.setName("jean@hotmail.com");
    }

    @Test
    void createMailBoxSuccessfully() {
        when(mailBoxService.createMailBox(mailBoxDTO)).thenReturn(mailBoxDTO);

        ResponseEntity<MailBoxDTO> response = mailBoxController.createMailBox(mailBoxDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mailBoxDTO, response.getBody());
    }

    @Test
    void getAllMainBoxesSuccessfully() {
        List<MailBoxDTO> mailBoxs = Arrays.asList(new MailBoxDTO(), new MailBoxDTO());

        when(mailBoxService.getAllMainBoxes()).thenReturn(mailBoxs);

        ResponseEntity<List<MailBoxDTO>> response = mailBoxController.getAllMainBoxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mailBoxs, response.getBody());
    }

    @Test
    void getAllMainBoxesWithPaging() {
        List<MailBoxDTO> mailBoxs = Arrays.asList(new MailBoxDTO(), new MailBoxDTO());
        Pageable pageable = PageRequest.of(0, 10);
        Page<MailBoxDTO> page = new PageImpl<>(mailBoxs);

        when(mailBoxService.getAllMainBoxes(pageable)).thenReturn(page);

        ResponseEntity<Page<MailBoxDTO>> response = mailBoxController.getAllMainBoxes(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}