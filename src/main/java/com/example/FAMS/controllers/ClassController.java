package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.Class.ClassDetailResponse;
import com.example.FAMS.dto.responses.Class.CreateClassResponse;
import com.example.FAMS.dto.responses.Class.DeactivateClassResponse;
import com.example.FAMS.dto.responses.Class.UpdateClassResponse;
import com.example.FAMS.models.Class;
import com.example.FAMS.service_implementors.ClassServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
@Log4j2
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
    public ResponseEntity<CreateClassResponse> createClass(
            @PathVariable(name = "type", required = false) String type,
            @RequestBody CreateClassDTO createClassDTO,
            Authentication authentication) {

        switch (type) {
            case "general":
                // Xử lý tạo lớp học dựa trên thông tin từ request
                Class result = classService.createClass(createClassDTO, authentication);
                if(result == null){
                    return ResponseEntity.status(400).body(new CreateClassResponse(null, "successfully create class."));
                }
                return ResponseEntity.status(200).body(new CreateClassResponse(result, "successfully create class."));
            case "schedule":
                // Xử lý tạo lịch trình cho lớp học
                break;
            case "other":
                // Xử lý tạo thông tin khác cho lớp học
                break;
        }
        return ResponseEntity.status(400).body(new CreateClassResponse(null, "fail to create class."));
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
    public ResponseEntity<?> getClassById(@PathVariable String classId) {
        Class classInfo = classService.getClassById(classId);
        if (classInfo != null) {
            return ResponseEntity.ok(classInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<DeactivateClassResponse> deactivateClass(@PathVariable("id") String classCode, @RequestParam(defaultValue = "false", name = "deactivated") boolean deactivated){
        return classService.deactivateClass(classCode, deactivated);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ClassDetailResponse> getClassDetail(@PathVariable("id") String classCode) throws InterruptedException {
        return classService.getClassDetail(classCode);
    }

    @GetMapping("/listClass")
    public ResponseEntity<?> getall(){
        return ResponseEntity.ok(classService.getAll());
    }
}
