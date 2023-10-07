package com.example.FAMS.dto.requests;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {
    private String name;
    private String email;
    private String phone;
    private Date dob;
    private String gender;
    private UserPermission role;
    private String status;
    private String createdBy;
    private String modifiedBy;
}
