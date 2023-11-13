package com.example.FAMS.repositories;

import com.example.FAMS.dto.responses.TrainingProgramDetails;
import com.example.FAMS.models.TrainingProgram;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramDAO extends JpaRepository<TrainingProgram, Integer> {
  <T> List<T> findBy(Class<T> classType);

  List<TrainingProgram> getAllBy();

  List<TrainingProgram> findTrainingProgramsByStatus(String status);

  TrainingProgram findByName(String trainingProgramName);

  Optional<TrainingProgram> getTrainingProgramByName(String name);

  @Query(
      value =
          "SELECT SUM(s.number_of_day) as 'totalTrainingProgramDates'\n"
              + "FROM training_programs tp\n"
              + "LEFT JOIN training_program_syllabuses tps ON tps.training_program_code = tp.training_program_code\n"
              + "LEFT JOIN syllabus s ON s.topic_code = tps.topic_code\n"
              + "WHERE tp.training_program_code = :code and tp.status = 'Active';",
      nativeQuery = true)
  Integer totalSubjectDays(@Param("code") int training_program_code);

  @Query(
      value =
          "SELECT sum(tu.day_number) as 'totalSubjectDays'\n"
              + "FROM training_programs tp\n"
              + "LEFT JOIN training_program_syllabuses tps ON tps.training_program_code = tp.training_program_code\n"
              + "LEFT JOIN syllabus s ON s.topic_code = tps.topic_code\n"
              + "LEFT JOIN training_unit tu ON tu.topic_code = s.topic_code\n"
              + "WHERE tp.training_program_code = :code and tp.status = 'Active'",
      nativeQuery = true)
  Integer totalTrainingProgramDates(@Param("code") int training_program_code);

  @Query(
      value =
          "SELECT tp.name as 'trainingProgramName', tp.modified_by, tp.modified_date, s.course_objective,\n"
              + "s.topic_name, s.publish_status, tu.unit_name, tu.day_number, tc.content_name, \n"
              + "lo.name as 'objective', tc.duration \n"
              + "FROM training_programs tp\n"
              + "LEFT JOIN training_program_syllabuses tps ON tps.training_program_code = tp.training_program_code\n"
              + "LEFT JOIN syllabus s ON s.topic_code = tps.topic_code\n"
              + "LEFT JOIN training_unit tu ON tu.topic_code = s.topic_code\n"
              + "LEFT JOIN training_contents tc ON tc.unit_code = tu.unit_code\t\n"
              + "LEFT JOIN learning_objective lo ON lo.content_code = tc.content_code\n"
              + "WHERE tp.training_program_code = :code and tp.status = 'Active'\n"
              + "GROUP BY tp.training_program_code, tp.name , tu.day_number, tp.modified_by, tp.modified_by, s.course_objective,\n"
              + "s.topic_name, s.publish_status, tu.unit_name, tc.content_name, \n"
              + "lo.name, tc.duration;",
      nativeQuery = true)
  List<TrainingProgramDetails> getTrainingProgramDetails(@Param("code") int training_program_code);
}
