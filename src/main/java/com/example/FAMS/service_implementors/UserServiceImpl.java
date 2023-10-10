package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.JWTService;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserDAO userDAO;
    private final UserPermissionDAO userPermissionDAO;
    private final JWTService jwtService;
    private List<ListUserResponse> userList;

    @Override
    public ResponseEntity<ResponseObject> getAll() {
        try {
            userList = userDAO.getAllUsersWithRole();
            logger.info("Return list of user");
            return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
        } catch (Exception e) {
            userList = Collections.emptyList();
            return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
        }
    }

    @Override
    public UpdateResponse updateUser(UpdateRequest updateRequest) {
        Optional<User> optionalUser = userDAO.findById(updateRequest.getUserId());
        var permission = userPermissionDAO.findUserPermissionByRole(updateRequest.getRole())
                .orElse(null);
        User existingUser = optionalUser.orElse(null);
        if (existingUser != null) {
            existingUser.setRole(permission);
            existingUser.setName(updateRequest.getName());
            existingUser.setPhone(updateRequest.getPhone());
            existingUser.setDob(updateRequest.getDob());
            existingUser.setGender(updateRequest.getGender());
            existingUser.setStatus(updateRequest.isStatus());

            // Save the updated user
            User updatedUser = userDAO.save(existingUser);

            if (updatedUser != null) {
                return UpdateResponse.builder()
                        .status("Update successful")
                        .updatedUser(updatedUser)
                        .build();
            } else {
                return UpdateResponse.builder()
                        .status("Update failed")
                        .updatedUser(null)
                        .build();
            }
        } else {
            // Return an UpdateResponse indicating user not found
            return UpdateResponse.builder()
                    .status("User not found")
                    .updatedUser(null)
                    .build();
        }
    }

    @Override
    public ResponseObject deleteUser(String mail) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getHeader("Authorization").substring(7);
        String performUserEmail = jwtService.extractUserEmail(token);
        User performUser = userDAO.findByEmail(performUserEmail).orElse(null);

        //set default ResponseObject
        ResponseObject ro = ResponseObject
                .builder()
                .status("Failed")
                .message("Delete user failed")
                .payload("Performed user " + performUserEmail + " not found")
                .build();

        if (performUser != null) {
            switch (performUser.getRole().getRole()) {
                case SUPER_ADMIN:
                    ro = disableUser(performUserEmail, mail, null);
                    break;
                case CLASS_ADMIN:
                    ro = disableUser(performUserEmail, mail, Role.USER);
                    break;
            }
        }
        return ro;
    }

    private ResponseObject disableUser(String performUserEmail, String deletedUserEmail, Role role) {
        User deletedUser = userDAO.findByEmail(deletedUserEmail).orElse(null);

        if (deletedUser != null) {
            if (!performUserEmail.equalsIgnoreCase(deletedUserEmail)) {
                if (role == null) {
                    deletedUser.setStatus(false);
                    userDAO.save(deletedUser);
                    return ResponseObject
                            .builder()
                            .status("Successful")
                            .message("Delete user successfully")
                            .payload("")
                            .build();
                } else {
                    if (deletedUser.getRole().getRole().equals(role)) {
                        deletedUser.setStatus(false);
                        userDAO.save(deletedUser);
                        return ResponseObject
                                .builder()
                                .status("Successful")
                                .message("Delete user successfully")
                                .payload("")
                                .build();
                    } else {
                        return ResponseObject
                                .builder()
                                .status("Failed")
                                .message("Delete user failed")
                                .payload("You don't have enough permission to delete this user")
                                .build();
                    }
                }
            }
            return ResponseObject
                    .builder()
                    .status("Failed")
                    .message("Delete user failed")
                    .payload("Can't delete yourself")
                    .build();
        }
        return ResponseObject
                .builder()
                .status("Failed")
                .message("Delete user failed")
                .payload("User " + deletedUserEmail + " not found")
                .build();
    }
}
