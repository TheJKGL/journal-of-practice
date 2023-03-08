package com.malakhov.journalofpractice.controllers;

import com.malakhov.journalofpractice.models.Credential;
import com.malakhov.journalofpractice.models.request.SignUpRequest;
import com.malakhov.journalofpractice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/api/users"})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody Credential loginRequest) {
        return new ResponseEntity<>(userService.authenticate(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.register(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }
}
