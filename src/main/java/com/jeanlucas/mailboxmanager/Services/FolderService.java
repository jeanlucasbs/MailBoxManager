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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.jeanlucas.mailboxmanager.utils.ValidarEntrada.isValidFolderName;

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

    public List<FolderDTO> getFoldersByMainBox(String mailBox) {
        MailBoxModel mailBoxModel = mailBoxRepository.findByName(mailBox);
        if(mailBoxModel == null){
            throw new ResourceNotFoundException("Caixa de email não encontrada.");
        }

        List<FolderModel> folders = folderRepository.findByMailbox_Name(mailBox);
        return folders.stream().map(folder -> new FolderDTO(folder.getIdt(), folder.getName())).collect(Collectors.toList());
    }

    public Page<FolderDTO> getFoldersByMainBox(String mailBox, Pageable pageable) {
        MailBoxModel mailBoxModel = mailBoxRepository.findByName(mailBox);
        if(mailBoxModel == null){
            throw new ResourceNotFoundException("Caixa de email não encontrada.");
        }
        return folderRepository.findByMailbox_Name(mailBox, pageable).map(folder -> new FolderDTO(folder.getIdt(), folder.getName()));
    }

}
