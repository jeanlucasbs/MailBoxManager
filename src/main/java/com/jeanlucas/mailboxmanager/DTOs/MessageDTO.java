package com.jeanlucas.mailboxmanager.DTOs;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class MessageDTO {

    private int idt;

    private String sender;

    private String recipient;

    private String subject;

    private String body;

    private boolean read;

    private LocalDateTime sendAt;

    private String folder;
}
