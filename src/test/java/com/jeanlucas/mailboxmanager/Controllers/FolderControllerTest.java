package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Services.FolderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

class FolderControllerTest {

    @InjectMocks
    private FolderController folderController;

    @Mock
    private FolderService folderService;

    private FolderDTO folderDTO;
    private FolderModel folderModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        folderDTO = new FolderDTO();
        folderDTO.setIdt(1);
        folderDTO.setName("NewFolder");

        folderModel = new FolderModel();
        folderModel.setName("NewFolder");
    }

    @Test
    public void createFolderSuccessfully() throws Exception {
        when(folderService.createFolder("jean@hotmail.com", folderDTO)).thenReturn(folderModel);

        ResponseEntity<FolderModel> response = folderController.createFolder("jean@hotmail.com", folderDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(folderModel, response.getBody());
    }

    @Test
    public void getFoldersByMainBox() throws Exception {
        List<FolderDTO> folders = Arrays.asList(new FolderDTO(), new FolderDTO());

        when(folderService.getFoldersByMainBox("jean@hotmail.com")).thenReturn(folders);

        ResponseEntity<List<FolderDTO>> response = folderController.getFoldersByMainBox("jean@hotmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(folders, response.getBody());
    }

    @Test
    public void getFoldersByMainBoxWithPaging() throws Exception {
        List<FolderDTO> folders = Arrays.asList(new FolderDTO(), new FolderDTO());
        Pageable pageable = PageRequest.of(0, 10);
        Page<FolderDTO> page = new PageImpl<>(folders);

        when(folderService.getFoldersByMainBox("jean@hotmail.com", pageable)).thenReturn(page);

        ResponseEntity<Page<FolderDTO>> response = folderController.getFoldersByMainBox("jean@hotmail.com", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }
}