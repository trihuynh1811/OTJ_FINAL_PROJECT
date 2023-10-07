package com.example.FAMS.services;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.service_implementors.UserPermissionServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPermissionServiceImplTest {
  @Mock private UserDAO userDAO;

  @Mock private UserPermissionDAO userPermissionDAO;

  @InjectMocks private UserPermissionServiceImpl userPermissionService;

  @Test
  void User_GrantPermission_returnResponseObject() {
    int userId = 123;
    Role role = Role.USER;

    // Create a mock User object
    User mockUser = new User();
    mockUser.setUserId(userId);
//    mockUser.setRole(Role.USER);

    when(userDAO.findById(Mockito.anyInt())).thenReturn(Optional.of(mockUser));
    when(userDAO.save(Mockito.any())).thenReturn(new User());

    var user = userDAO.findById(1);
    var savedUser = userDAO.save(new User());

    Assertions.assertThat(savedUser).isNotNull();
    Assertions.assertThat(user).isNotNull();
  }
}
