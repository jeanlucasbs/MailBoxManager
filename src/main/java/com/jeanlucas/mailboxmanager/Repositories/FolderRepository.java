package com.jeanlucas.mailboxmanager.Repositories;

import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<FolderModel, Integer> {

    boolean existsByNameAndMailboxIdt(String name, int mailboxId);

    FolderModel findByNameAndMailbox(String sent, MailBoxModel mailBox);

    List<FolderModel> findByMailbox_Name(String mainBox);

    Page<FolderModel> findByMailbox_Name(String mainBox, Pageable pageable);

    Optional<FolderModel> findByName(String folderName);
}
