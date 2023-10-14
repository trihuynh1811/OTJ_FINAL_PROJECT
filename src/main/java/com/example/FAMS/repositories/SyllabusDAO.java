package com.example.FAMS.repositories;

import com.example.FAMS.models.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SyllabusDAO extends JpaRepository<Syllabus, String> {

    List<Syllabus> findTop1000ByOrderByCreatedDateDesc();

    @Query(value = "SELECT top 1 * from syllabus where topic_code like %:topicCode order by topic_code desc", nativeQuery = true)
    Syllabus getLastSyllabusByTopicCode(String topicCode);
}
