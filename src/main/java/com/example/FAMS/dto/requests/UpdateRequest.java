package com.example.FAMS.dto.requests;

import com.example.FAMS.enums.Role;
import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private int userId;
    private Role role;
    private String name;
    private String phone;
    private Date dob;
    private String gender;
    private String status;
}
