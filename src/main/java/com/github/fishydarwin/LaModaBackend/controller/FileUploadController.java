package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.manager.UserSessionManager;
import com.github.fishydarwin.LaModaBackend.service.StorageService;
import com.github.fishydarwin.LaModaBackend.service.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/uploads/get/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> uploadsGet(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/uploads/add")
    public ResponseEntity<String> uploadsAdd(@RequestParam("files") MultipartFile[] files, @RequestParam String sessionId) {

        User sessionUser = UserSessionManager.bySession(sessionId);
        if (sessionUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        try {
            List<String> resultingFilePaths = new ArrayList<>();
            for (MultipartFile file : files) resultingFilePaths.add(storageService.store(file));

            return ResponseEntity.ok("\"" + Arrays.toString(resultingFilePaths.toArray()) + "\"");
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not upload files!");
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> uploadsGetNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
