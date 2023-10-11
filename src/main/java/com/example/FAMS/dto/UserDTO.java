package com.example.FAMS.dto;

import com.example.FAMS.models.UserPermission;

import java.util.Date;

public interface UserDTO {

    public String getName() ;

    public String getEmail();

    public String getPhone();

    public Date getDob() ;

    public String getGender();

    public UserPermission getRole() ;

    public boolean isStatus() ;

    public String getCreatedBy();

    public Date getCreatedDate() ;

    public String getModifiedBy() ;

    public Date getModifiedDate() ;

}
