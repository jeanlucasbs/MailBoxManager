package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Models.MailBoxModel;
import com.jeanlucas.mailboxmanager.Services.MailBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mailboxes")
public class MailBoxController {

    @Autowired
    private MailBoxService mailBoxService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MailBoxModel> createMailBox(@RequestBody MailBoxDTO mailBoxDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mailBoxService.createMailBox(mailBoxDTO));
    }
}
