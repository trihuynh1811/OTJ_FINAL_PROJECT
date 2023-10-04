package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.ResposeObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserPermissionDAO userPermissionDAO;
    private final UserDAO userDAO;

    private List<ListUserResponse> userList;

    @Override
    public List<UserPermission> getUserPermission() {
        return userPermissionDAO.findAll();
    }

    @Override
    public ResponseEntity<ResposeObject> getAll() {
        try {
            userList = userDAO.findAllByRole(Role.USER);
            return ResponseEntity.ok(new ResposeObject("Successful", "Found user", userList));
        } catch (Exception e) {
            userList = Collections.emptyList();
            return ResponseEntity.ok(new ResposeObject("Failed", "Not found user", userList));
        }

    }

    @Override
    public User updateUser(int userId, Role role, String name, String phone, Date dob, String gender, String status) {
        logger.info("user id variable: " + userId);
        var user = userDAO.findById(userId).orElse(null);

        if (user == null) {
            // Handle user not found error or throw an exception
            return null;
        }
        if (role != null) {
            user.setRole(role);
        }
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        if (isValidPhoneNumber(phone)) {
            user.setPhone(phone);
        } else {
            return null;
        }
        if (gender != null && isValidGender(gender)) {
            user.setGender(gender);
        } else {
            return null;
        }
        if (isValidDateOfBirth(dob)) {
            logger.info("Check date " + dob);
            user.setDob(dob);
        } else {
            return null;
        }

        logger.info("Status");
        if (status != null && isValidStatus(status)) {

            user.setStatus(status);
        } else {
            return null;
        }

        logger.info("Modified date");
        // Update the modified date
        user.setModifiedDate(new Date());
        logger.info("Final result: " + user);
        // Save the updated user to the database
        userDAO.save(user);

        return user;
    }

    private boolean isValidGender(String gender) {
        return "Male".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender);
    }

    // Additional methods for validation

    private boolean isValidPhoneNumber(String phone) {
        // Implement phone number validation logic here based on your rules
        return true; // Return true if it's valid, otherwise return false
    }

    private boolean isValidDateOfBirth(Date dob) {
        // Implement date of birth validation logic here based on your rules
        return true; // Return true if it's valid, otherwise return false
    }

    private boolean isValidStatus(String status) {
        return true;
    }

}



