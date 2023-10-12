package com.example.FAMS.repositories;

import com.example.FAMS.models.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SyllabusDAO extends JpaRepository<Syllabus, String> {

    List<Syllabus> findTop1000ByOrderByCreatedDateDesc();

    List<Syllabus> findAllByOrderByCreatedDateDesc();

    int countByTopicCodeLike(String topicCode);
}
