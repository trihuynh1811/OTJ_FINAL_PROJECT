package com.example.FAMS.repositories;

import com.example.FAMS.models.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionDAO extends JpaRepository<UserPermission, Integer> {
}
