package com.example.FAMS.services;

import com.example.FAMS.enums.Permission;
import com.example.FAMS.repositories.UserPermissionDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserPermissionDAO userPermissionDAO;

    public static Set<Permission> getUserPermission() {
        Set<Permission> permissionSet = new HashSet<>();


        return permissionSet;
    }
}
