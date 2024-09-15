package com.jeanlucas.mailboxmanager.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailBoxDTO {

    @JsonIgnore
    private int idt;

    private String name;

    public MailBoxDTO(String name) {
        this.name = name;
    }
}
