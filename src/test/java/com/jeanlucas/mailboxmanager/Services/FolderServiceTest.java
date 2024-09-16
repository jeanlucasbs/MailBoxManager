package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Exception.ResourceNotFoundException;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.FolderRepository;
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
import java.util.List;

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
    void createFolderSuccess() {
        Mockito.when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        Mockito.when(folderRepository.existsByNameAndMailboxIdt(folderDTO.getName(), mailBoxModel.getIdt())).thenReturn(false);
        Mockito.when(folderRepository.save(Mockito.any(FolderModel.class))).thenReturn(folderModel);

        FolderModel result = folderService.createFolder("Test", folderDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("NewFolder", result.getName());
        Assertions.assertEquals(mailBoxModel, result.getMailbox());
    }

    @Test
    void createFolderMailboxNotFound() {
        Mockito.when(mailBoxRepository.findByName("Test")).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            folderService.createFolder("Test", folderDTO);
        });

        Mockito.verify(folderRepository, Mockito.never()).save(Mockito.any(FolderModel.class));
    }

    @Test
    void createFolderFolderAlreadyExists() {
        Mockito.when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        Mockito.when(folderRepository.existsByNameAndMailboxIdt(folderDTO.getName(), mailBoxModel.getIdt())).thenReturn(true);

        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            folderService.createFolder(mailBoxModel.getName(), folderDTO);
        });

        Mockito.verify(folderRepository, Mockito.never()).save(Mockito.any(FolderModel.class));
    }

    @Test
    void createFolderFolderInvalidName() {
        FolderDTO invalidFolderDTO = new FolderDTO(1, "Invalid@Name");

        Mockito.when(mailBoxRepository.findByName("Test")).thenReturn(mailBoxModel);
        Mockito.when(folderRepository.existsByNameAndMailboxIdt(invalidFolderDTO.getName(), mailBoxModel.getIdt())).thenReturn(false);

        Assertions.assertThrows(InvalidNameException.class, () -> {
            folderService.createFolder("Test", invalidFolderDTO);
        });

        Mockito.verify(folderRepository, Mockito.never()).save(Mockito.any(FolderModel.class));
    }

    @Test
    void getFoldersByMainBoxSuccess() {
        Mockito.when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);

        FolderModel folder1 = new FolderModel();
        folder1.setName("Folder 1");
        FolderModel folder2 = new FolderModel();
        folder2.setName("Folder 2");

        List<FolderModel> folders = Arrays.asList(folder1, folder2);

        Mockito.when(folderRepository.findByMailbox_Name(mailBoxModel.getName())).thenReturn(folders);

        List<FolderDTO> folderDTOs = folderService.getFoldersByMainBox(mailBoxModel.getName());

        Assertions.assertEquals(2, folderDTOs.size());
        Assertions.assertEquals("Folder 1", folderDTOs.get(0).getName());
        Mockito.verify(folderRepository, Mockito.times(1)).findByMailbox_Name(mailBoxModel.getName());
    }

    @Test
    public void getFoldersByMainBoxMailboxNotFound() {
        Mockito.when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            folderService.getFoldersByMainBox(mailBoxModel.getName());
        });

        Mockito.verify(folderRepository, Mockito.never()).findByMailbox_Name(mailBoxModel.getName());
    }

    @Test
    public void getFoldersByMainBoxWithPaginationSuccess() {
        Mockito.when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(mailBoxModel);

        FolderModel folder1 = new FolderModel();
        folder1.setName("Folder 1");
        FolderModel folder2 = new FolderModel();
        folder2.setName("Folder 2");

        List<FolderModel> folderList = Arrays.asList(folder1, folder2);
        Page<FolderModel> folderPage = new PageImpl<>(folderList);

        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(folderRepository.findByMailbox_Name(mailBoxModel.getName(), pageable)).thenReturn(folderPage);

        Page<FolderDTO> result = folderService.getFoldersByMainBox(mailBoxModel.getName(), pageable);

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Folder 1", result.getContent().get(0).getName());
        Mockito.verify(folderRepository, Mockito.times(1)).findByMailbox_Name(mailBoxModel.getName(), pageable);
    }

    @Test
    public void testGetFoldersByMainBoxMailboxNotFoundWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(mailBoxRepository.findByName(mailBoxModel.getName())).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            folderService.getFoldersByMainBox(mailBoxModel.getName(), pageable);
        });

        Mockito.verify(folderRepository, Mockito.never()).findByMailbox_Name(mailBoxModel.getName(), pageable);
    }
}