package com.jeanlucas.mailboxmanager.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
