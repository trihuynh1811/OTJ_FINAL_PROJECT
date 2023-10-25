package com.example.FAMS.repositories;

import com.example.FAMS.models.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramDAO extends JpaRepository<TrainingProgram, Integer> {
    <T> List<T> findBy(Class<T> classType);
}
