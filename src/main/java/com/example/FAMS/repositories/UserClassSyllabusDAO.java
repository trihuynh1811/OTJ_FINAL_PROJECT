package com.example.FAMS.repositories;

import com.example.FAMS.models.UserClassSyllabus;
import com.example.FAMS.models.composite_key.UserClassSyllabusCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClassSyllabusDAO extends JpaRepository<UserClassSyllabus, UserClassSyllabusCompositeKey> {
}
