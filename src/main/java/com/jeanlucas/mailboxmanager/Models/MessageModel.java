package com.jeanlucas.mailboxmanager.Models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MESSAGE")
public class MessageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idt;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 50)
    private String subject;

    private String body;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "send_at", nullable = false)
    private LocalDateTime sendAt;

    @ManyToOne
    @JoinColumn(name = "folder_idt", referencedColumnName = "idt", nullable = false)
    private FolderModel folder;
}
