package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MessageDTO;
import com.jeanlucas.mailboxmanager.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping(value = "/v1/mailboxes/{mailbox}/send-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMessage(@PathVariable("mailbox") String mailbox, @RequestBody MessageDTO message){
        messageService.sendMessage(mailbox, message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mensagem armazenada.");
    }

    @PostMapping(value = "/v1/mailboxes/{mailbox}/receive-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveReceiveMesssage(@PathVariable("mailbox") String mailbox, @RequestBody MessageDTO message){
        messageService.saveReceiveMesssage(mailbox, message);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mensagem armazenada.");
    }

    @PutMapping(value = "/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}/read", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages/{messageIdt}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> getMessageDetail(@PathVariable("mailbox") String mailBoxName,
                                                       @PathVariable("folderIdt") String folderName,
                                                       @PathVariable("messageIdt") int messageIdt){
        return ResponseEntity.ok(messageService.getMessageDetail(mailBoxName, folderName, messageIdt));
    }

    @GetMapping(value = "/v1/mailboxes/{mailbox}/folders/{folderIdt}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDTO>> getMessagesByMainBoxAndFolder(@PathVariable("mailbox") String mailBoxName,
                                                                          @PathVariable("folderIdt") String folderName){
        return ResponseEntity.ok(messageService.getMessagesByMainBoxAndFolder(mailBoxName, folderName));
    }

    @GetMapping(value = "/v2/mailboxes/{mailbox}/folders/{folderIdt}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MessageDTO>> getMessagesByMainBoxAndFolder(@PathVariable("mailbox") String mailBoxName,
                                                                          @PathVariable("folderIdt") String folderName,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageDTO> messages = messageService.getMessagesByMainBoxAndFolder(mailBoxName, folderName, pageable);
        return ResponseEntity.ok(messages);
    }
}
