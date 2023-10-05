package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.models.Class;

import java.sql.Date;
import java.util.List;

public interface ClassService {
    List<Class> getClasses();

    Class createClass(String className, String classCode, String duration, String location, Date startDate, Date endDate, String createdBy);

    Class updateClass(UpdateClassRequest updateClassRequest);

    Class getClassById(int classId);
}
