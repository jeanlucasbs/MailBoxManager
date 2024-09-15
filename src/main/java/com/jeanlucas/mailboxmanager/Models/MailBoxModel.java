package com.jeanlucas.mailboxmanager.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MAILBOX")
public class MailBoxModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "mailbox", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FolderModel> folders;
}
