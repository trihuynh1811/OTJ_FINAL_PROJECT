package com.example.FAMS.repositories;

import com.example.FAMS.dto.responses.SearchFilterResponse;
import com.example.FAMS.models.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SyllabusDAO extends JpaRepository<Syllabus, String> {

    List<Syllabus> findTop1000ByOrderByCreatedDateDesc();

    List<Syllabus> findAllByOrderByCreatedDateDesc();

    int countByTopicCodeLike(String topicCode);
//    @Query(value = "SELECT top 1 * from syllabus where topic_code like %:topicCode order by topic_code desc",nativeQuery = true)
//    Syllabus getLastSyllabusByTopicCode(String topicCode);

    Optional<Syllabus> findByTopicName(String topicName);

    @Query(value = "SELECT *\n" +
            "FROM syllabus AS fc\n" +
            "INNER JOIN training_unit AS tu ON tu.topic_code = fc.topic_code\n" +
            "INNER JOIN training_contents AS tc ON tc.unit_code = tu.unit_code\n" +
            "INNER JOIN syllabus_objective AS so ON so.topic_code = fc.topic_code\n" +
            "WHERE  fc.topic_code = 'huy';", nativeQuery = true)
    List<Syllabus> detail(String topicCode);
}
