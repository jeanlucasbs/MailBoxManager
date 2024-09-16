package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Exception.ResourceNotFoundException;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.FolderRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FolderServiceTest {

    @InjectMocks
    private FolderService folderService;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private MailBoxRepository mailBoxRepository;

    private FolderModel folderModel;
    private MailBoxModel mailBoxModel;
    private FolderDTO folderDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        folderDTO = new FolderDTO(1, "NewFolder");

        mailBoxModel = new MailBoxModel();
        mailBoxModel.setIdt(1);
        mailBoxModel.setName("Test");

        folderModel = new FolderModel();
        folderModel.setName(folderDTO.getName());
        folderModel.setMailbox(mailBoxModel);
    }

    @Test
    void createFolderSuccessfully() {
        when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        when(folderRepository.existsByNameAndMailboxIdt(folderDTO.getName(), mailBoxModel.getIdt())).thenReturn(false);
        when(folderRepository.save(any(FolderModel.class))).thenReturn(folderModel);

        FolderDTO result = folderService.createFolder("Test", folderDTO);

        assertNotNull(result);
        assertEquals("NewFolder", result.getName());
    }

    @Test
    void createFolderMailboxNotFound() {
        when(mailBoxRepository.findByName("Test")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            folderService.createFolder("Test", folderDTO);
        });

        verify(folderRepository, never()).save(any(FolderModel.class));
    }

    @Test
    void createFolderFolderAlreadyExists() {
        when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        when(folderRepository.existsByNameAndMailboxIdt(folderDTO.getName(), mailBoxModel.getIdt())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            folderService.createFolder(mailBoxModel.getName(), folderDTO);
        });

        verify(folderRepository, never()).save(any(FolderModel.class));
    }

    @Test
    void createFolderInvalidName() {
        FolderDTO invalidFolderDTO = new FolderDTO(1, "Invalid@Name");

        when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        when(folderRepository.existsByNameAndMailboxIdt(invalidFolderDTO.getName(), mailBoxModel.getIdt())).thenReturn(false);

        assertThrows(InvalidNameException.class, () -> {
            folderService.createFolder("Test", invalidFolderDTO);
        });

        verify(folderRepository, never()).save(any(FolderModel.class));
    }

    @Test
    void getFoldersByMainBoxSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);

        FolderModel folder1 = new FolderModel();
        folder1.setName("Folder 1");
        FolderModel folder2 = new FolderModel();
        folder2.setName("Folder 2");

        List<FolderModel> folders = Arrays.asList(folder1, folder2);

        when(folderRepository.findByMailbox_Name(mailBoxModel.getName())).thenReturn(folders);

        List<FolderDTO> folderDTOs = folderService.getFoldersByMainBox(mailBoxModel.getName());

        assertEquals(2, folderDTOs.size());
        assertEquals("Folder 1", folderDTOs.get(0).getName());
        verify(folderRepository, times(1)).findByMailbox_Name(mailBoxModel.getName());
    }

    @Test
    public void getFoldersByMainBoxMailboxNotFound() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            folderService.getFoldersByMainBox(mailBoxModel.getName());
        });

        verify(folderRepository, never()).findByMailbox_Name(mailBoxModel.getName());
    }

    @Test
    public void getFoldersByMainBoxWithPaginationSuccessfully() {
        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);

        FolderModel folder1 = new FolderModel();
        folder1.setName("Folder 1");
        FolderModel folder2 = new FolderModel();
        folder2.setName("Folder 2");

        List<FolderModel> folderList = Arrays.asList(folder1, folder2);
        Page<FolderModel> folderPage = new PageImpl<>(folderList);

        Pageable pageable = PageRequest.of(0, 10);
        when(folderRepository.findByMailbox_Name(mailBoxModel.getName(), pageable)).thenReturn(folderPage);

        Page<FolderDTO> result = folderService.getFoldersByMainBox(mailBoxModel.getName(), pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Folder 1", result.getContent().get(0).getName());
        verify(folderRepository, times(1)).findByMailbox_Name(mailBoxModel.getName(), pageable);
    }

    @Test
    public void getFoldersByMainBoxMailboxNotFoundWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);

        when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            folderService.getFoldersByMainBox(mailBoxModel.getName(), pageable);
        });

        verify(folderRepository, never()).findByMailbox_Name(mailBoxModel.getName(), pageable);
    }
}