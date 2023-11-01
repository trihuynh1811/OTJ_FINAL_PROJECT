package com.example.FAMS.repositories;

import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.dto.responses.TrainingProgramModified;
import com.example.FAMS.models.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramDAO extends JpaRepository<TrainingProgram, Integer> {
  <T> List<T> findBy(Class<T> classType);

  TrainingProgram findByName(String trainingProgramName);
}
