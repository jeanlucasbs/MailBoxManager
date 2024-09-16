package com.jeanlucas.mailboxmanager.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailBoxDTO extends RepresentationModel<MailBoxDTO> {

    @JsonIgnore
    private int idt;

    private String name;

    private List<FolderDTO> folders;

    public MailBoxDTO(String name) {
        this.name = name;
    }
}
