package com.jeanlucas.mailboxmanager.Services;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Exception.InvalidNameException;
import com.jeanlucas.mailboxmanager.Exception.ResourceAlreadyExistsException;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Repositories.FolderRepository;
import com.jeanlucas.mailboxmanager.Repositories.MailBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.jeanlucas.mailboxmanager.utils.ValidarEntrada.isValidEmail;

@Service
public class MailBoxService {

    @Autowired
    private MailBoxRepository mailBoxRepository;

    @Autowired
    private FolderRepository folderRepository;

    public MailBoxModel createMailBox(MailBoxDTO mailBoxDTO) {
        if(!isValidEmail(mailBoxDTO.getName())){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel ExistsMailBox = mailBoxRepository.findByName(mailBoxDTO.getName());

        if(ExistsMailBox != null){
            throw new ResourceAlreadyExistsException("Caixa de email já existe.");
        }

        MailBoxModel mailBox = new MailBoxModel();
        mailBox.setName(mailBoxDTO.getName());
        mailBoxRepository.save(mailBox);

        FolderModel inbox = new FolderModel();
        inbox.setName("INBOX");
        inbox.setMailbox(mailBox);

        FolderModel junk = new FolderModel();
        junk.setName("JUNK");
        junk.setMailbox(mailBox);

        FolderModel sent = new FolderModel();
        sent.setName("SENT");
        sent.setMailbox(mailBox);

        List<FolderModel> folders = Arrays.asList(inbox, junk, sent);
        mailBox.setFolders(folders);

        return mailBoxRepository.save(mailBox);
    }
}
