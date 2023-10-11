package com.example.FAMS.Service;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.UpdateResponse;
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
import org.mockito.junit.jupiter.MockitoExtension;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.FAMS.enums.Permission.USER_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @Mock
    private UserPermissionDAO userPermissionDAO;

    @InjectMocks
    private  UserServiceImpl userService;

    @Test
    void UpdateUser() throws ParseException {
        int userId = 1;
        String dateStr = "2023/05/10";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dob = dateFormat.parse(dateStr);
        UserPermission userPermission = UserPermission.builder()
                .role(Role.USER)
                .syllabus(List.of())
                .trainingProgram(List.of())
                .userClass(List.of())
                .userManagement(List.of(USER_VIEW))
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
