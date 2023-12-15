package com.example.FAMS.repository;

import com.example.FAMS.models.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepo extends JpaRepository<UserPermission, Integer> {
}
