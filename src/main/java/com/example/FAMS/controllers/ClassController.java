package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.dto.responses.UpdateClassResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.service_implementors.ClassServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public ResponseEntity<String> getClasses() {
        return ResponseEntity.status(HttpStatus.OK).body("a list of class have been returned.");
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('class:read')")
    public ResponseEntity<String> getDetailClasses() {
        return ResponseEntity.ok("details of a class have been returned.");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<String> createClass(){

        return ResponseEntity.status(HttpStatus.OK).body("successfully create a class.");
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<String> updateClass(@PathVariable String classId) {
        return ResponseEntity.status(200).body("successfully update class with id: " + classId);
    }

    @GetMapping("/search/{classId}")
    public ResponseEntity<?> getClassById(@PathVariable int classId) {
        Class classInfo = classService.getClassById(classId);
        if (classInfo != null) {
            return ResponseEntity.ok(classInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
