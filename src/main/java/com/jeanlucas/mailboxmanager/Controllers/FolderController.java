package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Services.FolderService;
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
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping(value = "/v1/mailboxes/{mailbox}/folders" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FolderDTO> createFolder(@PathVariable("mailbox") String mailbox, @RequestBody FolderDTO folderDTO) {
        FolderDTO folder = folderService.createFolder(mailbox, folderDTO);
        folder.add(linkTo(methodOn(FolderController.class).createFolder(mailbox, folderDTO)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(folder);
    }

    @GetMapping("/v1/mailboxes/{mailbox}/folders")
    public ResponseEntity<List<FolderDTO>> getFoldersByMainBox(@PathVariable("mailbox") String mailBox) {
        List<FolderDTO> folders = folderService.getFoldersByMainBox(mailBox);

        List<FolderDTO> folderResources = folders.stream()
                .map(folder -> folder.add(linkTo(methodOn(FolderController.class).getFoldersByMainBox(mailBox)).withSelfRel())
                ).collect(Collectors.toList());

        return ResponseEntity.ok(folderResources);
    }

    @GetMapping("/v2/mailboxes/{mailbox}/folders")
    public ResponseEntity<Page<FolderDTO>> getFoldersByMainBox(@PathVariable("mailbox") String mailBox,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FolderDTO> folders = folderService.getFoldersByMainBox(mailBox, pageable);
        return ResponseEntity.ok(folders);
    }
}
