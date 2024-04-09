package com.github.fishydarwin.LaModaBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AccessStatusController {

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> all() {
        return new ResponseEntity<>("\"OK\"", HttpStatus.OK);
    }

}
