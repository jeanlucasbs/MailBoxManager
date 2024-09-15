package com.jeanlucas.mailboxmanager.Repositories;

import com.jeanlucas.mailboxmanager.Models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {

    Optional<MessageModel> findByIdtAndFolder_IdtAndFolder_Mailbox_Name(int messageIdt, int folderIdt, String mailboxName);

    List<MessageModel> findByFolder_NameAndFolder_Mailbox_Name(String folderName, String mailBox);

    Optional<MessageModel> findByIdtAndFolder_NameAndFolder_Mailbox_Name(int messageIdt, String folderName, String mailBoxName);
}
