package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.ResponseObject;
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
    public ResponseEntity<List<Class>> getClasses() {
        return ResponseEntity.status(HttpStatus.OK).body(classService.getClasses());
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('class:read')")
    public ResponseEntity<List<Class>> getDetailClasses() {
        return ResponseEntity.ok(classService.getDetailClasses());
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<List<Class>> createClass(
            @PathVariable("type") String type,
            @RequestBody JsonNode request,
            Authentication authentication) {
        switch (type) {
            case "general":
                // Xử lý tạo lớp học dựa trên thông tin từ request
                Object creator = authentication.getPrincipal();
                System.out.println(creator);
                break;
            case "schedule":
                // Xử lý tạo lịch trình cho lớp học
                break;
            case "other":
                // Xử lý tạo thông tin khác cho lớp học
                break;
        }
        return ResponseEntity.status(HttpStatus.OK).body(classService.getClasses());
    }

    @GetMapping("/draft/create/{type}")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<List<Class>> draftCreateClass(@PathVariable("type") String type) {
        return ResponseEntity.status(HttpStatus.OK).body(classService.getDetailClasses());
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<UpdateClassResponse> updateClass(
            @PathVariable int classId, @RequestBody UpdateClassRequest updateClassRequest) {
        UpdateClassResponse updatedClass = classService.updateClass(updateClassRequest);
        if (updatedClass != null) {
            return ResponseEntity.ok(updatedClass);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
