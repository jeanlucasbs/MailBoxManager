package com.jeanlucas.mailboxmanager.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "FOLDER", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "mailbox_idt"})})
public class FolderModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "mailbox_idt", nullable = false)
    @JsonBackReference
    private MailBoxModel mailbox;

    @OneToMany(mappedBy = "folder",  cascade = CascadeType.ALL)
    private List<MessageModel> messages;
}
