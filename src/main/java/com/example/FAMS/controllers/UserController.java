package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.CreateResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'CLASS_ADMIN', 'SUPER_ADMIN')")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObject> getAllUser() {
        return userService.getAll();
    }

    @PostMapping("/update-user/{userId}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UpdateResponse> updateUserRequest(@PathVariable int userId, @RequestBody UpdateRequest updateRequest) {

        return ResponseEntity.ok(userService.updateUser(updateRequest));
    }

}
