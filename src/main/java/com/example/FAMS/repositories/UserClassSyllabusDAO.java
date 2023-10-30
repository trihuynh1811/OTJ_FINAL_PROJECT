package com.example.FAMS.repositories;

import com.example.FAMS.models.User;
import com.example.FAMS.models.UserClassSyllabus;
import com.example.FAMS.models.composite_key.UserClassSyllabusCompositeKey;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserClassSyllabusDAO extends JpaRepository<UserClassSyllabus, UserClassSyllabusCompositeKey> {

    List<UserClassSyllabus> findByUserId_EmailAndClassCode_ClassId(String email, String classCode);

    List<?> findDistinctByClassCode_ClassId(String classCode);
}
