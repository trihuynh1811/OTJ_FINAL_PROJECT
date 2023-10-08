package com.example.FAMS.dto;

import com.example.FAMS.enums.Permission;
import com.example.FAMS.enums.Role;

import java.io.Serializable;
import java.util.List;

public interface UserPermissionDTO extends Serializable {

    public Role getRole() ;

    public List<Permission> getSyllabus();

    public List<Permission> getTrainingProgram();

    public List<Permission> getUserClass() ;

    public List<Permission> getLearningMaterial() ;

    public List<Permission> getUserManagement();
}
