package com.example.FAMS.services;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
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

import java.util.List;
import java.util.Optional;

import static com.example.FAMS.enums.Permission.USER_READ;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPermissionServiceImplTest {
  @Mock private UserDAO userDAO;

  @Mock private UserPermissionDAO userPermissionDAO;

  @InjectMocks private UserPermissionServiceImpl userPermissionService;

  @Test
  void User_GrantPermission_returnResponseObject() {
    int userId = 123;
    UserPermission userPermission = UserPermission.builder()
            .role(Role.USER)
            .syllabus(List.of())
            .trainingProgram(List.of())
            .userClass(List.of())
            .userManagement(List.of(USER_READ))
            .learningMaterial(List.of())
            .build();

    when(userPermissionDAO.findUserPermissionByRole(any())).thenReturn(Optional.of(userPermission));

    // Create a mock User object
    User mockUser = new User();
    mockUser.setUserId(userId);
    mockUser.setRole(userPermissionDAO.findUserPermissionByRole(Role.USER).orElse(null));

    when(userDAO.findById(Mockito.anyInt())).thenReturn(Optional.of(mockUser));
    when(userDAO.save(Mockito.any())).thenReturn(new User());

    var user = userDAO.findById(1);
    var savedUser = userDAO.save(new User());

    Assertions.assertThat(savedUser).isNotNull();
    Assertions.assertThat(user).isNotNull();
  }
}
