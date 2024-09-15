package com.jeanlucas.mailboxmanager.Mappers;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Models.MessageModel;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageModel toModel(MessageDTO messageDTO) {
        return MessageModel.builder()
        .idt(messageDTO.getIdt())
        .sender(messageDTO.getSender())
        .recipient(messageDTO.getRecipient())
        .subject(messageDTO.getSubject())
        .body(messageDTO.getBody())
        .sendAt(messageDTO.getSendAt())
        .read(messageDTO.isRead()).build();
    }

    public MessageDTO toDTO(MessageModel messageModel) {
        return MessageDTO.builder()
                .idt(messageModel.getIdt())
                .sender(messageModel.getSender())
                .recipient(messageModel.getRecipient())
                .subject(messageModel.getSubject())
                .body(messageModel.getBody())
                .sendAt(messageModel.getSendAt())
                .read(messageModel.isRead()).build();
    }
}
