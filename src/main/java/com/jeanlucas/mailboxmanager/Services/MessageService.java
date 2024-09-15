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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.jeanlucas.mailboxmanager.utils.ValidarEntrada.isValidEmail;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MailBoxRepository mailBoxRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MessageMapper mapper;

    public void sendMessage(String mailBoxName, MessageDTO messageDTO){

        if(!isValidEmail(mailBoxName) || !isValidEmail(messageDTO.getRecipient())){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel mailBox = mailBoxRepository.findByName(mailBoxName);
        if(mailBox == null){
            throw new ResourceNotFoundException("Caixa de email não existe.");
        }

        FolderModel sentFolder = folderRepository.findByNameAndMailbox("SENT", mailBox);
        if(sentFolder == null){
            throw new ResourceNotFoundException("Pasta 'SENT' não encontrada para a caixa de e-mail");
        }

        if (messageDTO.getSubject() != null && messageDTO.getSubject().length() > 50) {
            throw new InvalidSubjectException("O assunto deve ter no máximo 50 caracteres.");
        }

        MessageModel message = new MessageModel();
        message.setSender(mailBoxName);
        message.setRecipient(messageDTO.getRecipient());
        message.setSubject(messageDTO.getSubject());
        message.setBody(messageDTO.getBody());
        message.setRead(true);
        message.setSendAt(LocalDateTime.now());
        message.setFolder(sentFolder);

        messageRepository.save(message);
    }

    public void saveReceiveMesssage(String mailBoxName, MessageDTO messageDTO){

        if(!isValidEmail(mailBoxName) || !isValidEmail(messageDTO.getSender())){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel mailBox = mailBoxRepository.findByName(mailBoxName);
        if(mailBox == null){
            throw new ResourceNotFoundException("Caixa de email não existe.");
        }

        if (messageDTO.getSubject() != null && messageDTO.getSubject().length() > 50) {
            throw new InvalidSubjectException("O assunto deve ter no máximo 50 caracteres.");
        }

        FolderModel folder = folderRepository.findByNameAndMailbox(messageDTO.getFolder(), mailBox);
        if (folder == null) {
            folder = folderRepository.findByNameAndMailbox("INBOX", mailBox);
            if (folder == null) {
                throw new ResourceNotFoundException("Pasta não encontrada para a caixa de e-mail");
            }
        }

        MessageModel message = new MessageModel();
        message.setSender(messageDTO.getSender());
        message.setRecipient(mailBoxName);
        message.setSubject(messageDTO.getSubject());
        message.setBody(messageDTO.getBody());
        message.setRead(false);
        message.setSendAt(LocalDateTime.now());
        message.setFolder(folder);

        messageRepository.save(message);
    }

    public void updateMessageReadStatus(String mailBoxName, int folderIdt, int messageIdt, MessageDTO messageDTO){
        if(!isValidEmail(mailBoxName)){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel mailBox = mailBoxRepository.findByName(mailBoxName);
        if(mailBox == null){
            throw new ResourceNotFoundException("Caixa de email não existe.");
        }

        FolderModel folder = folderRepository.findById(folderIdt).orElse(null);
        if (folder == null) {
            throw new ResourceNotFoundException("Pasta não encontrada para a caixa de e-mail");
        }

        MessageModel message = messageRepository.findByIdtAndFolder_IdtAndFolder_Mailbox_Name(messageIdt, folderIdt, mailBoxName)
                .orElseThrow(() -> new ResourceNotFoundException("Mensagem não encontrada."));

        message.setRead(messageDTO.isRead());
        messageRepository.save(message);
    }

    public List<MessageDTO> getFoldersByMainBoxAndFolder(String mailBoxName, String folderName){
        if(!isValidEmail(mailBoxName)){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel mailBox = mailBoxRepository.findByName(mailBoxName);
        if(mailBox == null){
            throw new ResourceNotFoundException("Caixa de email não existe.");
        }

        FolderModel folder = folderRepository.findByName(folderName).orElse(null);
        if (folder == null) {
            throw new ResourceNotFoundException("Pasta não encontrada para a caixa de e-mail");
        }

        List<MessageModel> messages = messageRepository.findByFolder_NameAndFolder_Mailbox_Name(folderName, mailBoxName);
        return messages.stream().map(message -> new MessageDTO(message.getIdt(),
                                                               message.getSender(),
                                                               message.getSubject(),
                                                               message.getSendAt(),
                                                               message.isRead())).collect(Collectors.toList());


    }

    public MessageDTO getMessageDetail(String mailBoxName, String folderName, int messageIdt){
        if (!isValidEmail(mailBoxName)){
            throw new InvalidNameException("Formato de email inválido.");
        }

        MailBoxModel mailBox = mailBoxRepository.findByName(mailBoxName);
        if (mailBox == null){
            throw new ResourceNotFoundException("Caixa de email não existe.");
        }

        FolderModel folder = folderRepository.findByName(folderName).orElse(null);
        if (folder == null) {
            throw new ResourceNotFoundException("Pasta não encontrada para a caixa de e-mail");
        }

        MessageModel message = messageRepository.findByIdtAndFolder_NameAndFolder_Mailbox_Name(messageIdt, folderName, mailBoxName)
                .orElseThrow(() -> new ResourceNotFoundException("Mensagem não encontrada."));

        return mapper.toDTO(message);
    }
}
