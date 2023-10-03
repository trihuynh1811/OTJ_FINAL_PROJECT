package com.example.FAMS.controllers;

import com.example.FAMS.dto.responses.ResposeObject;
import com.example.FAMS.service_implementors.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('USER')")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/")
//    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResposeObject> getAllUser() {
        return userServiceImpl.getAll();
    }

}
