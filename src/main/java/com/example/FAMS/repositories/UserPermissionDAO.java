package com.example.FAMS.repositories;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.UserPermission;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionDAO extends JpaRepository<UserPermission, Integer> {

    Optional<UserPermission> findUserPermissionByRole(Role role);
}
