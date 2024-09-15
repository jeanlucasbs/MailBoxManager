package com.jeanlucas.mailboxmanager.Controllers;

import com.jeanlucas.mailboxmanager.DTOs.FolderDTO;
import com.jeanlucas.mailboxmanager.Models.FolderModel;
import com.jeanlucas.mailboxmanager.Services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mailboxes")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping(value = "{mailbox}/folders" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FolderModel> createFolder(@PathVariable("mailbox") String mailbox, @RequestBody FolderDTO folderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(mailbox, folderDTO));
    }

    @GetMapping("/{mailbox}/folders")
    public ResponseEntity<List<FolderDTO>> getFoldersByMainBox(@PathVariable("mailbox") String mainBox) {
        return ResponseEntity.ok(folderService.getFoldersByMainBox(mainBox));
    }
}
