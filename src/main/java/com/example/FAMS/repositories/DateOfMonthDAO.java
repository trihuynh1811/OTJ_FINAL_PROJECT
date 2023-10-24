package com.example.FAMS.repositories;

import com.example.FAMS.models.time.DateOfMonth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateOfMonthDAO extends JpaRepository<DateOfMonth, Integer> {
}
