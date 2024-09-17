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

@RestController
@RequestMapping("/api")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping(value = "/v1/mailboxes/{mailbox}/folders" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FolderDTO> createFolder(@PathVariable("mailbox") String mailbox, @RequestBody FolderDTO folderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(mailbox, folderDTO));
    }

    @GetMapping("/v1/mailboxes/{mailbox}/folders")
    public ResponseEntity<List<FolderDTO>> getFoldersByMainBox(@PathVariable("mailbox") String mailBox) {
        return ResponseEntity.ok(folderService.getFoldersByMainBox(mailBox));
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
