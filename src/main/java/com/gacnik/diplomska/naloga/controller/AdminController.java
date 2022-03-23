package com.gacnik.diplomska.naloga.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/admin")
@AllArgsConstructor
public class AdminController {
    @GetMapping("/all")
    public ResponseEntity<String> fetchAllEmployees() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
