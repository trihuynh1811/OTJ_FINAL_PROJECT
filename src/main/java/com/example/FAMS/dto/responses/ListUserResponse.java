package com.example.FAMS.dto.responses;

import java.util.Date;


import com.example.FAMS.enums.Role;



public interface ListUserResponse {
    public int getUserId();

    public String getName();

    public String getEmail();

    public String getPhone();

    public Date getDob();

    public String getGender();

    public Role getRole();



}
