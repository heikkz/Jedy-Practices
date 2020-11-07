package ru.heikkz.jp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("returned");
    }
}
