package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Exception.ResourceNotFoundException;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.FolderRepository;
import com.jeanlucas.mailboxmanager.Repositories.MailBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MailBoxRepository mailBoxRepository;

    public FolderModel createFolder(String mailBoxName, FolderDTO folderDTO) {

        MailBoxModel mailBoxModel = mailBoxRepository.findByName(mailBoxName);

        if (mailBoxModel == null) {
            throw new ResourceNotFoundException("Mailbox não existe");
        }

        boolean folderExists = folderRepository.existsByNameAndMailboxIdt(folderDTO.getName(), mailBoxModel.getIdt());

        if(folderExists) {
            throw new ResourceAlreadyExistsException("Pasta já existe na mailbox.");
        }

        if(!isValidFolderName(folderDTO.getName())){
            throw new InvalidNameException("Formato da pasta inválido.");
        }

        FolderModel folder = new FolderModel();
        folder.setName(folderDTO.getName());
        folder.setMailbox(mailBoxModel);

        return folderRepository.save(folder);
    }

    private boolean isValidFolderName(String name) {
        return name != null && name.matches("[a-zA-Z0-9_-]{1,100}");
    }
}
