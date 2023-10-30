package com.example.FAMS.services;

import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.Class.*;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.models.*;
import com.example.FAMS.models.Class;
import org.springframework.http.ResponseEntity;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface ClassService {
    List<GetClassesResponse> getClasses();

    ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException;

    Class createClass(CreateClassDTO request, Authentication authentication);

    UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest);

    ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode);

    Class getClassById(String classId);

    List<Class> CalendarSort();

    List<Class> getAll();

  ResponseEntity<ResponseObject> getDayCalendar(java.util.Date currentDate);

  ResponseEntity<ResponseObject> getWeekCalendar(java.util.Date startDate, java.util.Date endDate);

  UpdateCalendarResponse updateClassLearningDay(UpdateCalendarRequest request) throws ParseException;

}
