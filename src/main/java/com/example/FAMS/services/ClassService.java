package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.UpdateClassResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;

import java.sql.Date;
import java.util.List;

public interface ClassService {
    List<Class> getClasses();

    List<Class> getDetailClass();

    Class createClass(String className, String classCode, String duration, String location, Date startDate, Date endDate, String createdBy);

    UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);

    Class getClassById(int classId);


}
