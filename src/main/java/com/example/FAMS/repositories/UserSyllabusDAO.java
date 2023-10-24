package com.example.FAMS.repositories;

import com.example.FAMS.models.UserSyllabus;
import com.example.FAMS.models.composite_key.UserSyllabusCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSyllabusDAO extends JpaRepository<UserSyllabus, UserSyllabusCompositeKey> {
}
