package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.requests.UpdatePasswordRequest;
import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.dto.responses.UserWithRoleDTO;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.JWTService;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserDAO userDAO;
    private final UserPermissionDAO userPermissionDAO;
    private final JWTService jwtService;
    private List<ListUserResponse> userList;
    private final PasswordEncoder passwordEncoder;


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
    public ResponseEntity<ResponseObject> pagination(int pageNo) {
        int totalPage = getNumberOfUsers();

        Pageable paging = PageRequest.of(pageNo, 2);
        Page<ListUserResponse> pagedResult = userDAO.findAllUsersBy(paging);

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(new ResponseObject("Successful", "null", totalPage, pagedResult.getContent()));
        } else {
            return ResponseEntity.ok(new ResponseObject("Failed", "null", totalPage, pagedResult.getContent()));
        }
    }

    @Override
    public UpdateResponse updateUser(String userEmail, UpdateRequest updateRequest) {
        var existedUser = userDAO.findByEmail(userEmail).orElse(null);
        var permission = userPermissionDAO.findUserPermissionByRole(updateRequest.getRole())
                .orElse(null);
        if (existedUser != null) {
            existedUser.setRole(permission);
            existedUser.setName(updateRequest.getName());
            existedUser.setPhone(updateRequest.getPhone());
            existedUser.setDob(updateRequest.getDob());
            existedUser.setGender(updateRequest.getGender());
            existedUser.setStatus(updateRequest.isStatus());
            existedUser.setModifiedBy(updateRequest.getModifiedBy());
            existedUser.setModifiedDate(new Date());

            // Save the updated user
            User updatedUser = userDAO.save(existedUser);

            if (updatedUser != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                return UpdateResponse.builder()
                        .status("Update successful")
                        .updatedUser(UserWithRoleDTO.builder()
                                .name(updatedUser.getName())
                                .role(updatedUser.getRole().getRole())
                                .email(updatedUser.getEmail())
                                .phone(updatedUser.getPhone())
                                .dob(updatedUser.getDob())
                                .gender(updatedUser.getGender())
                                .status(updatedUser.isStatus())
                                .createdBy(updatedUser.getCreatedBy())
                                .createdDate(updatedUser.getCreatedDate())
                                .modifiedBy(updatedUser.getModifiedBy())
                                .modifiedDate(updatedUser.getModifiedDate())
                                .build())
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
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("Failed")
                .message("Delete user failed")
                .payload("Performed user " + performUserEmail + " not found")
                .build();

        if (performUser != null) {
            switch (performUser.getRole().getRole()) {
                case SUPER_ADMIN:
                    responseObject = disableUser(performUserEmail, mail, null);
                    break;
                case CLASS_ADMIN:
                    responseObject = disableUser(performUserEmail, mail, Role.USER);
                    break;
            }
        }
        return responseObject;
    }

    @Override
    public ResponseObject updatePassword(UpdatePasswordRequest updateRequest) {
        var existedUser = userDAO.findByEmail(updateRequest.getUserEmail()).orElse(null);
        if (existedUser == null) {
            throw new RuntimeException("User not found");
        } else {
            existedUser.setPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
            User savedUser = userDAO.save(existedUser);
            return ResponseObject.builder()
                    .status("Successful")
                    .message("Update successfully")
                    .payload(UpdatePasswordRequest.builder()
                            .userEmail(savedUser.getEmail())
                            .newPassword(updateRequest.getNewPassword())
                            .build()
                    )
                    .build();
        }
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

    public int getNumberOfUsers() {
        return userDAO.countAllByStatusIs(true);
    }
}
