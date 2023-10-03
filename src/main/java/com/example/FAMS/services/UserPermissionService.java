package com.example.FAMS.services;

import com.example.FAMS.models.UserPermission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserPermissionService {
    List<UserPermission> GetUserPermission();
}
