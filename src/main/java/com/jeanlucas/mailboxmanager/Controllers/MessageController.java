package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mailboxes")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping(value = "/{mailbox}/send-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessage(@PathVariable("mailbox") String mailbox, @RequestBody MessageDTO message){
        messageService.sendMessage(mailbox, message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mensagem armazenada.");
    }

    @PostMapping(value = "/{mailbox}/receive-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveReceiveMesssage(@PathVariable("mailbox") String mailbox, @RequestBody MessageDTO message){
        messageService.saveReceiveMesssage(mailbox, message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mensagem armazenada.");
    }

    @PutMapping(value = "/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMessageReadStatus(@PathVariable("mailbox") String mailBoxName,
                                                          @PathVariable("folderIdt") int folderIdt,
                                                          @PathVariable("messageIdt") int messageIdt,
                                                          @RequestBody MessageDTO messageDTO){
        if(messageDTO == null){
            return ResponseEntity.badRequest().build();
        }
        messageService.updateMessageReadStatus(mailBoxName, folderIdt, messageIdt, messageDTO);
        return ResponseEntity.noContent().build();
    }
}
