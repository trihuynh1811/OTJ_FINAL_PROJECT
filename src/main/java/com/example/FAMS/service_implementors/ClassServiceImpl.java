package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.UpdateClassResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.ClassLearningDay; // Thêm ClassLearningDay
import com.example.FAMS.models.ClassUser;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.ClassDAO;
import com.example.FAMS.repositories.TrainingProgramDAO;
import com.example.FAMS.services.ClassService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

  @Autowired ClassDAO classDAO;
  TrainingProgramDAO trainingProgramDAO;

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
  public List<Class> getAll() {
    return classDAO.getAll();
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
