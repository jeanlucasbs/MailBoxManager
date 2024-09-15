package com.jeanlucas.mailboxmanager.Repositories;

import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<FolderModel, Integer> {

    boolean existsByNameAndMailboxIdt(String name, int mailboxId);

    FolderModel findByNameAndMailbox(String sent, MailBoxModel mailBox);

    FolderModel findByName(MailBoxModel mailBox);
}
