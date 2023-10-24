package com.example.FAMS.services;

import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.Class.ClassDetailResponse;
import com.example.FAMS.dto.responses.Class.CreateClassResponse;
import com.example.FAMS.dto.responses.Class.DeactivateClassResponse;
import com.example.FAMS.dto.responses.Class.UpdateClassResponse;
import com.example.FAMS.models.Class;
import org.springframework.http.ResponseEntity;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.util.List;

public interface ClassService {
    List<Class> getClasses();

    ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException;

    Class createClass(CreateClassDTO request, Authentication authentication);

    UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);

    ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode, boolean deactivated);

    Class getClassById(String classId);

    List<Class> getAll();
}
