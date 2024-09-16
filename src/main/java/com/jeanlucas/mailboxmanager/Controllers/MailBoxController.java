package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.MailBoxDTO;
import com.jeanlucas.mailboxmanager.Services.MailBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class MailBoxController {

    @Autowired
    private MailBoxService mailBoxService;

    @PostMapping(value = "/v1/mailboxes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MailBoxDTO> createMailBox(@RequestBody MailBoxDTO mailBoxDTO) {
        MailBoxDTO createdMailBox = mailBoxService.createMailBox(mailBoxDTO);

        createdMailBox.add(linkTo(methodOn(MailBoxController.class)
                .createMailBox(mailBoxDTO)).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMailBox);
    }

    @GetMapping("/v1/mailboxes")
    public ResponseEntity<List<MailBoxDTO>> getAllMainBoxes() {
        List<MailBoxDTO> mailBoxes = mailBoxService.getAllMainBoxes();

        List<MailBoxDTO> folderResources = mailBoxes.stream()
                .map(folder -> folder.add(linkTo(methodOn(MailBoxController.class).getAllMainBoxes()).withSelfRel())
                ).collect(Collectors.toList());

        return ResponseEntity.ok(folderResources);
    }

    @GetMapping("/v2/mailboxes")
    public ResponseEntity<Page<MailBoxDTO>> getAllMainBoxes(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<MailBoxDTO> mailBoxes = mailBoxService.getAllMainBoxes(pageable);
        return ResponseEntity.ok(mailBoxes);
    }
}
