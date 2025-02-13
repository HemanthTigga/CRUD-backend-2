package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/heartbeat")
    public ResponseEntity<String> heartBeat(){
        return ResponseEntity.ok().body("Running");
    }
}
