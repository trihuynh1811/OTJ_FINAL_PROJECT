package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dtos.response.ListUserResponseImpl;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.service_implementors.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock private UserDAO userDAO;
  @InjectMocks private UserServiceImpl userService;

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
}
