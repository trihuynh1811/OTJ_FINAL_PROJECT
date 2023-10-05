package com.example.FAMS.Service;

import com.example.FAMS.dto.requests.UpdateRequest;
import com.example.FAMS.dto.responses.UpdateResponse;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private  UserServiceImpl userService;

    @Test
    void UpdateUser() throws ParseException {
        int userId = 1;
        String dateStr = "2023/05/10";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dob = dateFormat.parse(dateStr);

        User user = User.builder()
                .userId(1)
                .role(Role.USER)
                .name("Anh Quan")
                .phone("0937534654")
                .dob(dob)
                .gender("Male")
                .status("Active")
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
