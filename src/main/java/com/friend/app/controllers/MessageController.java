package com.friend.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/message")
public class MessageController {

    @PostMapping("/telegram")
    public ResponseEntity<HttpStatus> sendTelegramNotification() {
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
