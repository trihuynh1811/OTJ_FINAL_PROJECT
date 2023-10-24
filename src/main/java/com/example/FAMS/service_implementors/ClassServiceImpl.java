package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.*;
import com.example.FAMS.models.*;
import com.example.FAMS.models.Class;
import com.example.FAMS.repositories.ClassDAO;
import com.example.FAMS.repositories.ClassLearningDayDAO;
import com.example.FAMS.repositories.TrainingProgramDAO;
import com.example.FAMS.services.ClassService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

  @Autowired ClassDAO classDAO;
  TrainingProgramDAO trainingProgramDAO;
  @Autowired
  ClassLearningDayDAO classLearningDayDAO;

  List<CalendarDayResponse> dayCalendars;
  List<CalendarWeekResponse> weekCalendars;

  @Override
  public List<Class> getClasses() {
    return classDAO.findTop1000ByOrderByCreatedDateDesc();
  }

  @Override
  public List<Class> getDetailClass() {
    return classDAO.findAll();
  }

  @Override
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
      User user) {
    // Kiểm tra điều kiện cho className, classCode, duration, location, startDate, endDate,
    // createdBy
    if (className == null
        || classCode == null
        || duration == null
        || location == null
        || startDate == null
        || endDate == null
        || createdBy == null) {
      throw new IllegalArgumentException("All fields are required.");
    }

    // Kiểm tra điều kiện cho learningDays
    if (learningDays == null || learningDays.isEmpty()) {
      throw new IllegalArgumentException("At least one learning day is required.");
    }

    // Kiểm tra điều kiện cho user: vai trò TRAINER
    if (user != null && !"TRAINER".equals(user.getRole().getRole().name())) {
      throw new IllegalArgumentException("The user must have the TRAINER role.");
    }

    Class classInfo =
        Class.builder()
            .className(className)
            .classCode(classCode)
            .duration(duration)
            .location(location)
            .startDate(startDate)
            .endDate(endDate)
            .createdBy(createdBy)
            .classLearningDays(learningDays)
            .classUsers(classUsers)
            .build();

    classDAO.save(classInfo);
    return classInfo;
  }

  @Override
  public UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest) {
    Optional<Class> optionalClass = classDAO.findById(updateClassRequest.getClassId());
    Class existingClass = optionalClass.orElse(null);
    if (existingClass != null) {
      existingClass =
          Class.builder()
              .classId(existingClass.getClassId())
              .className(updateClassRequest.getClassName())
              .classCode(updateClassRequest.getClassCode())
              .duration(updateClassRequest.getDuration())
              .location(updateClassRequest.getLocation())
              .startDate(updateClassRequest.getStartDate())
              .endDate(updateClassRequest.getEndDate())
              .createdBy(existingClass.getCreatedBy())
              .createdDate(existingClass.getCreatedDate())
              .modifiedBy(existingClass.getModifiedBy())
              .modifiedDate(existingClass.getModifiedDate())
              .classLearningDays(
                  existingClass.getClassLearningDays()) // Bổ sung cập nhật danh sách ngày học
              .build();

      Class updatedClass = classDAO.save(existingClass);

      if (updatedClass != null) {
        return UpdateClassResponse.builder()
            .status("Update Class successful")
            .updatedClass(updatedClass)
            .build();
      } else {
        return UpdateClassResponse.builder()
            .status("Update Class failed")
            .updatedClass(null)
            .build();
      }
    } else {
      return UpdateClassResponse.builder().status("Class not found").updatedClass(null).build();
    }
  }

  public List<Class> getDetailClasses() {
    return classDAO.findAll();
  }

  @Override
  public Class getClassById(int classId) {
    Optional<Class> optionalClass = classDAO.findById(classId);
    return optionalClass.orElse(null);
  }

  @Override
  public List<Class> CalendarSort(){
    return classDAO.getCalendarSort();
  }

  @Override
  public List<Class> getAll() {
    return classDAO.getAll();
  }

  @Override
  public ResponseEntity<ResponseObject> getDayCalendar(java.util.Date currentDate) {
    try {
      dayCalendars = classDAO.getCalendarByDay(currentDate);
      return ResponseEntity.ok(new ResponseObject("Successful", "List of classroom", dayCalendars));
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
    }
  }

  @Override
  public ResponseEntity<ResponseObject> getWeekCalendar(java.util.Date startDate, java.util.Date endDate) {
    try {
      weekCalendars = classDAO.getCalendarByWeek(startDate, endDate);
      return ResponseEntity.ok(new ResponseObject("Successful", "List of classroom", weekCalendars));
    } catch(Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
    }
  }

  @Override
  public UpdateCalendarResponse updateClassLearningDay(UpdateCalendarRequest request) throws ParseException {
    int id = request.getId();
    String enrollDate = request.getEnrollDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date eDate = dateFormat.parse(enrollDate);
    Time timeFrom = request.getTimeFrom();
    Time timeTo = request.getTimeTo();
    String value = request.getValue();

    ClassLearningDay classLearningDay = classLearningDayDAO.findByClassId_ClassIdAndEnrollDate(id, eDate);


    if (classLearningDay != null) {
      if ("Only".equals(value)) {
        classLearningDay.setTimeFrom(timeFrom);
        classLearningDay.setTimeTo(timeTo);
        classLearningDay = classLearningDayDAO.save(classLearningDay);
      } else if ("All".equals(value)) {
        List<ClassLearningDay> classLearningDays = classLearningDayDAO.findByClassId_ClassId(id);
        for (ClassLearningDay day : classLearningDays) {
          day.setTimeFrom(timeFrom);
          day.setTimeTo(timeTo);
        }
        classLearningDayDAO.saveAll(classLearningDays);
      }

      if (classLearningDay != null) {
        return UpdateCalendarResponse.builder()
                .status("Update Calendar successful")
                .updateClassLearningDay(classLearningDay)
                .build();
      } else {
        return UpdateCalendarResponse.builder()
                .status("Update Calendar failed")
                .updateClassLearningDay(classLearningDay)
                .build();
      }
    } else {
      return UpdateCalendarResponse.builder()
              .status("Calendar not found")
              .updateClassLearningDay(null)
              .build();
    }
  }


  public List<Class> searchClass(String createdDate, String searchValue, String orderBy) {
    List<Class> classList = classDAO.findAll();

    if (!Strings.isNullOrEmpty(createdDate)) {
      classList =
          classList.stream()
              .filter(
                  c ->
                      new SimpleDateFormat("yyyy-MM-dd")
                          .format(c.getCreatedDate())
                          .equals(createdDate))
              .collect(Collectors.toList());
    }

    if (!Strings.isNullOrEmpty(searchValue)) {
      classList =
          classList.stream()
              .filter(
                  c ->
                      c.getClassName().toLowerCase().contains(searchValue.trim().toLowerCase())
                          || c.getClassCode()
                              .toLowerCase()
                              .contains(searchValue.trim().toLowerCase()))
              .collect(Collectors.toList());
    }

    if (!Strings.isNullOrEmpty(orderBy)) {
      if (orderBy.equals("asc")) {
        classList.sort(Comparator.comparing(Class::getClassName));
      } else if (orderBy.equals("desc")) {
        classList.sort(Comparator.comparing(Class::getClassName).reversed());
      }
    }

    return classList;
  }
}
