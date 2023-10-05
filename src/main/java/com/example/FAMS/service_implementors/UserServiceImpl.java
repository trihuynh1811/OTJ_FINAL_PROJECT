package com.example.FAMS.service_implementors;

import com.example.FAMS.controllers.UserController;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.ResposeObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final UserDAO userDAO;
  private List<ListUserResponse> userList;

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

}
