package com.jeanlucas.mailboxmanager.DTOs;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO extends RepresentationModel<MessageDTO> {

    private int idt;

    private String sender;

    private String recipient;

    private String subject;

    private String body;

    private boolean read;

    private LocalDateTime sendAt;

    private String folder;

    public MessageDTO(int idt, String sender, String subject, LocalDateTime sendAt, boolean read) {
        this.idt = idt;
        this.sender = sender;
        this.subject = subject;
        this.sendAt = sendAt;
        this.read = read;
    }
}
