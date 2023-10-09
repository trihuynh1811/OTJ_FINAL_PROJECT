package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.requests.CreateRequest;
import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.dto.responses.UserWithRoleDTO;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserService;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final UserDAO userDAO;

  private List<ListUserResponse> userList;
  private final UserPermissionDAO userPermissionDAO;


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
      existingUser.setStatus(updateRequest.getStatus());

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


}
