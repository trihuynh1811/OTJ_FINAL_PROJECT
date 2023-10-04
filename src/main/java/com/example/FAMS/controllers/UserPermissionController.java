package com.example.FAMS.controllers;

import com.example.FAMS.models.UserPermission;
import com.example.FAMS.service_implementors.UserServiceImpl;
import com.example.FAMS.services.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('USER', 'CLASS_ADMIN', 'SUPER_ADMIN')")
public class UserPermissionController {

    private final UserPermissionService userPermissionService;

    @GetMapping("/get-permissions")
//    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<UserPermission>> GetAllPermission() {
        List<UserPermission> list = userPermissionService.getUserPermission();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
