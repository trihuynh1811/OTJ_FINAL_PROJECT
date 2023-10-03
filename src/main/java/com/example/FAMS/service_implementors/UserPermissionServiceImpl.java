package com.example.FAMS.service_implementors;

import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPermissionServiceImpl implements UserPermissionService {
    @Autowired
    private UserPermissionDAO userPermissionDAO;
    @Override
    public List<UserPermission> GetUserPermission() {

        return userPermissionDAO.findAll();
    }
}
