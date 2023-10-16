package com.example.FAMS.repositories;

import com.example.FAMS.models.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassDAO extends JpaRepository<Class, Integer> {

    List<Class> findTop1000ByOrderByCreatedDateDesc();
}
