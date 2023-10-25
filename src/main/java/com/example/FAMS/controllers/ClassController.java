package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.responses.ClassRes.GetAllClasses;
import com.example.FAMS.dto.responses.ClassRes.GetClass;
import com.example.FAMS.dto.responses.ClassRes.GetClassDetail;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.service_implementors.ClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/class")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
public class ClassController {

    @Autowired
    ClassServiceImpl classService;

    @GetMapping
    @PreAuthorize("hasAuthority('class:read')")
    public ResponseEntity<List<GetAllClasses>> getClasses() {
        List<GetAllClasses> classes = getListClasses();
        return ResponseEntity.status(HttpStatus.OK).body(classes);
    }

    @GetMapping("/detail/{classId}")
    @PreAuthorize("hasAuthority('class:read')")
    public ResponseEntity<GetClassDetail> getDetailClasses(@PathVariable String classId) {
        return ResponseEntity.ok(detailOfClass(classId));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<GetClassDetail> createClass(){
        return ResponseEntity.status(HttpStatus.OK).body(detailOfClass("mock class"));
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<String> updateClass(@PathVariable String classId) {
        return ResponseEntity.status(200).body("successfully update class with id: " + classId);
    }

    @PutMapping("/delete/{classId}")
    public ResponseEntity<String> deleteClass(@PathVariable String classId) {
        return ResponseEntity.status(200).body("successfully delete class with id: " + classId);
    }

    @GetMapping("/search/{classId}")
    public ResponseEntity<GetClass> getClassById(@PathVariable String classId) {
        return ResponseEntity.status(200).body(getClass_(classId));
    }

    @GetMapping("/listClass")
    public ResponseEntity<?> getall(){
        return ResponseEntity.ok(classService.getAll());

    }

    @GetMapping("/sortCalendar")
    public ResponseEntity<?> sortCalendar(){
        return ResponseEntity.ok(classService.CalendarSort());

    }

    @GetMapping("/view-calendar/day")
    public ResponseEntity<ResponseObject> getDayCalendar(@RequestParam(name = "currentDate") String currentDate) throws ParseException {
        Date current = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
        return classService.getDayCalendar(current);
    }

    @GetMapping("/view-calendar/week")
    public ResponseEntity<ResponseObject> getWeekCalendar(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate
    ) throws ParseException {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        return classService.getWeekCalendar(start, end);
    }

    @PutMapping("/update-calendar")
    public UpdateCalendarResponse updateClassLearningDay(@RequestBody UpdateCalendarRequest request) throws ParseException {
        return classService.updateClassLearningDay(request);
    }

    public List<GetAllClasses> getListClasses(){
        List<GetAllClasses> classes = new ArrayList<>();
        for(int i = 0; i < 5; i ++){
            GetAllClasses class_ = GetAllClasses.builder()
                    .className("class " + i)
                    .classCode(Integer.toString(i))
                    .createdOn(new Date().getTime())
                    .fsu("fhcm")
                    .status("planing")
                    .createBy("joe mama")
                    .location("joe mama house")
                    .duration(69)
                    .build();

            classes.add(class_);
        }
        return classes;
    }

    public GetClassDetail detailOfClass(String classId){
        List<Syllabus> syllabusList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            Syllabus s = Syllabus.builder()
                    .topicCode("mock topic " + i)
                    .topicName("topic " + i)
                    .topicOutline("lorem...")
                    .courseObjective("lorem...")
                    .modifiedBy("joe father")
                    .modifiedDate(new Date())
                    .version("1.0")
                    .trainingMaterials("nothing")
                    .trainingPrinciples("idk")
                    .publishStatus("active")
                    .createdBy("joe")
                    .createdDate(new Date())
                    .technicalGroup("a pc")
                    .trainingAudience(100)
                    .numberOfDay(2000)
                    .priority("high")
                    .build();

            syllabusList.add(s);
        }

        GetClassDetail class_ = GetClassDetail.builder()
                .className("class " + classId)
                .classCode(classId)
                .createdOn(new Date().getTime())
                .fsu("fhcm")
                .status("planing")
                .createBy("joe mama")
                .location("joe mama house")
                .duration(69)
                .attendee("fresher")
                .attendeePlanned(100)
                .attendeeAccepted(99)
                .attendeeActual(99)
                .timeFrom(Time.valueOf("08:00:00"))
                .timeTo(Time.valueOf("10:00:00"))
                .startDate(new Date().getTime() - 1000000)
                .endDate(new Date().getTime() + 2000000)
                .trainingProgram("how to code with joe mama.")
                .trainingProgramModifiedDate(new Date().getTime() - 5000000)
                .listOfSyllabus(syllabusList)
                .build();

        return class_;
    }

    public GetClass getClass_(String id){
        return GetClass.builder()
                .className("class " + id)
                .classCode(id)
                .createdOn(new Date().getTime())
                .fsu("fhcm")
                .status("planing")
                .createBy("joe mama")
                .location("joe mama house")
                .duration(69)
                .build();
    }


}
