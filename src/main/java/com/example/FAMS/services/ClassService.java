package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.UpdateClassResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.ClassLearningDay;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface ClassService {
    List<Class> getClasses();

    List<Class> getDetailClass();

    Class createClass(String className, String classCode, String duration, String location, Date startDate, Date endDate, String createdBy);

    // Thêm phương thức tạo lớp học với danh sách ngày học
    Class createClassWithLearningDays(
            String className, String classCode, String duration, String location, Date startDate, Date endDate, String createdBy, Set<ClassLearningDay> learningDays
    );

    UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);








    Class getClassById(int classId);

    List<Class> getAll();}
