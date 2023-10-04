package com.example.FAMS.service_implementors;

import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionDAO userPermissionDAO;

    @Override
    public List<UserPermission> getUserPermission() {
        return userPermissionDAO.findAll();
    }
}
