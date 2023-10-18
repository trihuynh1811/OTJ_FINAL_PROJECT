package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.Class.ClassDetailResponse;
import com.example.FAMS.dto.responses.Class.DeactivateClassResponse;
import com.example.FAMS.dto.responses.Class.UpdateClassResponse;
import com.example.FAMS.models.Class;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

public interface ClassService {
    List<Class> getClasses();

    ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException;

    Class createClass(String className, String classCode, int duration, String location, Date startDate, Date endDate, String createdBy);

    UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);

    ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode, boolean deactivated);

    Class getClassById(String classId);
}
