package com.example.FAMS.repositories;

import com.example.FAMS.models.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramDAO extends JpaRepository<TrainingProgram, Integer> {
}
