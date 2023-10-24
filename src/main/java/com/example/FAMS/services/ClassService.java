package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateClassResponse;
import com.example.FAMS.models.*;
import com.example.FAMS.models.Class;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface ClassService {
  List<Class> getClasses();

  List<Class> getDetailClass();

  public Class createClass(
      String className,
      String classCode,
      String duration,
      String location,
      Date startDate,
      Date endDate,
      String createdBy,
      Set<ClassLearningDay> learningDays,
      Set<ClassUser> classUsers,
      User user);

  UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);

  Class getClassById(int classId);

    List<Class> CalendarSort();

    List<Class> getAll();

  ResponseEntity<ResponseObject> getDayCalendar(java.util.Date currentDate);

  ResponseEntity<ResponseObject> getWeekCalendar(java.util.Date startDate, java.util.Date endDate);
}
