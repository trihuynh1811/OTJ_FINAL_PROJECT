package com.example.FAMS.repositories;

import com.example.FAMS.models.UserPermission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionDAO extends JpaRepository<UserPermission, String> {

}
