package com.jeanlucas.mailboxmanager.DTOs;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO extends RepresentationModel<FolderDTO> {

    private int idt;

    private String name;
}
