package com.example.FAMS.repositories;

import com.example.FAMS.models.time.DateOfMonth;
import com.example.FAMS.models.time.MonthOfYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthOfYearDAO extends JpaRepository<MonthOfYear, Integer> {
}
