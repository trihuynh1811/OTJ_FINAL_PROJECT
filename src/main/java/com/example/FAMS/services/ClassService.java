package com.example.FAMS.services;

import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.ClassRequest.UpdateClassDTO;
import com.example.FAMS.dto.requests.ClassRequest.UpdateClassRequest;
import com.example.FAMS.dto.requests.ClassRequest.UpdateClass3Request;
import com.example.FAMS.dto.responses.Class.*;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.util.List;

public interface ClassService {
    List<GetClassesResponse> getClasses();

    ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException;

    ResponseEntity<ResponseObject> getFilter();

    Class createClass(CreateClassDTO request, Authentication authentication);

    UpdateClassResponse updateClass(UpdateClassDTO updateClassRequest);

    ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode);

    Class getClassById(String classId);

    List<Class> CalendarSort();

    Page<Class> getAllPagenation(Pageable pageable);

    ResponseEntity<ResponseObject> getDayCalendar(java.util.Date currentDate);

    ResponseEntity<ResponseObject> getWeekCalendar(java.util.Date startDate, java.util.Date endDate);

    UpdateCalendarResponse updateClassLearningDay(UpdateCalendarRequest request) throws ParseException;

    UpdateClass3Response updateClass3(UpdateClass3Request updateClass3Request);

    ResponseEntity<ResponseObject>deleteAllTrainingProgramSyllabus();

}
