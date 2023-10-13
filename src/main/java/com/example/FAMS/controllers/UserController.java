package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'CLASS_ADMIN', 'SUPER_ADMIN')")
public class UserController {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObject> getAllUser() {
        return userService.getAll();
    }

    @GetMapping("/pagination")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObject> getPagination(
            @RequestParam(defaultValue = "0", name = "pageNo") Integer pageNo
    ) {
        return userService.pagination(pageNo);
    }

    @PutMapping("/update-user/{userEmail}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UpdateResponse> updateUserRequest(
            @PathVariable String userEmail,
            @RequestBody UpdateRequest updateRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(userEmail, updateRequest));
    }

    @DeleteMapping("/delete/{email}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<ResponseObject> deletePermission(@PathVariable String email){
        return ResponseEntity.ok(userService.deleteUser(email));
    }
}
