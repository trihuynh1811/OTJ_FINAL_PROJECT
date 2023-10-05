package com.example.FAMS.controllers;

import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
// @PreAuthorize("hasAnyRole('USER', 'CLASS_ADMIN', 'SUPER_ADMIN')")
public class UserController {

  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping("/")
  //    @PreAuthorize("hasAuthority('user:read')")
  public ResponseEntity<ResponseObject> getAllUser() {
    return userService.getAll();
  }

  @PutMapping("/updateUser/{userId}")
  //    @PreAuthorize("hasAuthority('user:update')")
  public ResponseEntity<User> updateUser(
      @PathVariable int userId,
      @RequestParam(name = "role", required = false) Role role,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "phone", required = false) String phone,
      @RequestParam(name = "dob", required = false) String dob,
      @RequestParam(name = "gender", required = false) String gender,
      @RequestParam(name = "status", required = false) String status
  )
      throws ParseException {
    Date converter = new SimpleDateFormat("dd/MM/yyyy").parse(dob);

    User updatedUser = userService.updateUser(userId, role, name, phone, converter, gender, status);

    if (updatedUser != null) {
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found or validation failed
    }
  }


}
