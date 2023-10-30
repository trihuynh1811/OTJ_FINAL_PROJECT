package com.example.FAMS.repositories;

import com.example.FAMS.models.ClassUser;
import com.example.FAMS.models.composite_key.ClassUserCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassUserDAO extends JpaRepository<ClassUser, ClassUserCompositeKey> {
}
