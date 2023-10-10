package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.models.Class;
import com.example.FAMS.service_implementors.ClassServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
public class ClassController {

    @Autowired
    ClassServiceImpl classService;

    @GetMapping
    @PreAuthorize("hasAuthority('class:read')")
    public ResponseEntity<List<Class>> get() {
        return ResponseEntity.status(418).body(classService.getClasses());
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<List<Class>> create(@PathVariable("type") String type, @RequestBody JsonNode request, Authentication authentication) {
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
        return ResponseEntity.status(418).body(classService.getClasses());
    }

    @GetMapping("/draft/create/{type}")
    @PreAuthorize("hasAuthority('class:create')")
    public ResponseEntity<List<Class>> draftCreate(@PathVariable("type") String type) {
        return ResponseEntity.status(418).body(classService.getClasses());
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<Class> updateClassRequest(@PathVariable int classId, @RequestBody UpdateClassRequest updateClassRequest) {
        Class updatedClass = classService.updateClass(updateClassRequest);
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

}
