package com.example.FAMS.controllers;

import com.example.FAMS.models.UserPermission;
import com.example.FAMS.service_implementors.UserPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class UserPermissionController {
    @Autowired
    private UserPermissionServiceImpl userPermissionService;

    @GetMapping("/permissions")
    public ResponseEntity<List<UserPermission>> GetAll() {
        List<UserPermission> list = userPermissionService.GetUserPermission();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}