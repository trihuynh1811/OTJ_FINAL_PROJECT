package com.example.FAMS.repositories;

import com.example.FAMS.models.TrainingProgramSyllabus;
import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.example.FAMS.models.time.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOfWeekDAO extends JpaRepository<DayOfWeek, Integer> {
}
