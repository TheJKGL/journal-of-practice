package com.malakhov.journalofpractice.controllers;

import com.malakhov.journalofpractice.models.Group;
import com.malakhov.journalofpractice.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getGroups(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestParam String name) {
        groupService.createGroup(name);
        return ResponseEntity.ok("Group successfully created!");
    }

    @PutMapping("/{name}/add")
    public ResponseEntity<String> addMember(@RequestParam String email, @PathVariable String name) {
        groupService.addMember(email, name);
        return ResponseEntity.ok("Member successfully added!");
    }
}
