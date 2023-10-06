package com.example.FAMS.dto;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.UserPermission;

import java.util.Date;

public interface UserDTO {

    public String getName() ;

    public String getEmail();

    public String getPhone();

    public Date getDob() ;

    public String getGender();

    public Role getRole() ;

    public String getStatus() ;

    public String getCreatedBy();

    public Date getCreatedDate() ;

    public String getModifiedBy() ;

    public Date getModifiedDate() ;

}
