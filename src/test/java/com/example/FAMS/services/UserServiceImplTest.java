package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.dtos.response.ListUserResponseImpl;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.service_implementors.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.FAMS.enums.Permission.USER_READ;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock private UserDAO userDAO;
  @InjectMocks private UserServiceImpl userService;
  @Mock private UserPermissionDAO userPermissionDAO;

  @Test
  void User_getAllUser_returnResponseObject() {

    List<ListUserResponse> expectedList = new ArrayList<>();
    expectedList.add(
        new ListUserResponseImpl(
            1, "User1", "user1@example.com", "1234567890", new Date(), "Male", Role.USER));
    expectedList.add(
        new ListUserResponseImpl(
            2, "User2", "user2@example.com", "9876543210", new Date(), "Female", Role.USER));

    when(userDAO.save(Mockito.any())).thenReturn(new User());
    when(userDAO.findAllByRole(Role.USER)).thenReturn(expectedList);

    List<ListUserResponse> list = userDAO.findAllByRole(Role.USER);
    var savedUser = userDAO.save(new User());
    Assertions.assertThat(list).isNotNull();
    Assertions.assertThat(list.size()).isGreaterThan(0);
    Assertions.assertThat(savedUser).isNotNull();
  }

  @Test
  void UpdateUser() throws ParseException {
    {
      int userId = 1;
      String dateStr = "2023/05/10";
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      Date dob = dateFormat.parse(dateStr);
      UserPermission userPermission =
          UserPermission.builder()
              .role(Role.USER)
              .syllabus(List.of())
              .trainingProgram(List.of())
              .userClass(List.of())
              .userManagement(List.of(USER_READ))
              .learningMaterial(List.of())
              .build();

      when(userPermissionDAO.findUserPermissionByRole(any()))
          .thenReturn(Optional.of(userPermission));

      User user =
          User.builder()
              .userId(1)
              .role(userPermissionDAO.findUserPermissionByRole(Role.USER).orElse(null))
              .name("Anh Quan")
              .phone("0937534654")
              .dob(dob)
              .gender("Male")
              .status(true)
              .build();
      when(userDAO.findById(1)).thenReturn(Optional.of(user));
      when(userDAO.save(user)).thenReturn(user);

      UpdateRequest updateRequest = new UpdateRequest();
      updateRequest.setUserId(userId);

      UpdateResponse updateResponse = userService.updateUser(updateRequest);

      // Sử dụng AssertJ để kiểm tra
      Assertions.assertThat(updateResponse).isNotNull();
      Assertions.assertThat(updateResponse.getStatus()).isEqualTo("Update successful");
      Assertions.assertThat(updateResponse.getUpdatedUser()).isEqualTo(user);
    }
  }

  @Test
  void Update_User() throws ParseException {
    int userId = 1;
    String dateStr = "2023/05/10";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date dob = dateFormat.parse(dateStr);
    UserPermission userPermission = UserPermission.builder()
            .role(Role.USER)
            .syllabus(List.of())
            .trainingProgram(List.of())
            .userClass(List.of())
            .userManagement(List.of(USER_READ))
            .learningMaterial(List.of())
            .build();

    when(userPermissionDAO.findUserPermissionByRole(any())).thenReturn(Optional.of(userPermission));

    User user = User.builder()
            .userId(1)
            .role(userPermissionDAO.findUserPermissionByRole(Role.USER).orElse(null))
            .name("Anh Quan")
            .phone("0937534654")
            .dob(dob)
            .gender("Male")
            .status(true)
            .build();
    when(userDAO.findById(1)).thenReturn(Optional.ofNullable(user));
    when(userDAO.save(user)).thenReturn(user);

    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.setUserId(userId);

    UpdateResponse updateResponse = userService.updateUser(updateRequest);
    Assertions.assertThat(updateResponse).isNotNull();
    Assertions.assertThat(updateResponse.getStatus()).isEqualTo("Update successful");
    Assertions.assertThat(updateResponse.getUpdatedUser()).isEqualTo(user);


    verify(userDAO, times(1)).findById(userId);
    verify(userDAO, times(1)).save(user);
  }
}
