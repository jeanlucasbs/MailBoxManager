package com.jeanlucas.mailboxmanager.Repositories;

import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailBoxRepository extends JpaRepository<MailBoxModel, Integer> {

    MailBoxModel findByName(String name);
}
