package com.example.FAMS.repositories;

import com.example.FAMS.models.TrainingCalendar;
import com.example.FAMS.models.composite_key.DayDateMonthYearClassCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCalendarDAO extends JpaRepository<TrainingCalendar, DayDateMonthYearClassCompositeKey> {
}
