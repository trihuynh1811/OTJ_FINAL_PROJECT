package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.service_implementors.SyllabusServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
public class SyllabusController {

    @Autowired
    SyllabusServiceImpl syllabusService;

    @GetMapping
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<List<Syllabus>> get() {
        return ResponseEntity.status(418).body(syllabusService.getSyllabuses());
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<List<Syllabus>> getDetail() {
        return ResponseEntity.status(418).body(syllabusService.getDetailSyllabus());
    }

    @GetMapping("/show")
    public ResponseEntity<?> loadDataInDB() throws IOException {
        List<Syllabus> customers = syllabusService.loadSyllabusData();
        if (customers != null) {
            // Nếu thông tin khách hàng được tìm thấy, trả về phản hồi thành công.
            return ResponseEntity.ok(customers);
        } else {
            // Nếu không tìm thấy thông tin khách hàng, trả về phản hồi lỗi.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer data not found.");
        }
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<List<Syllabus>> create(@PathVariable("type") String type, @RequestBody JsonNode request, Authentication authentication) {
        switch(type){
            case "general":
//                String topicName = request.get("topic_name").asText();
//                String topicCode = request.get("topic_code").asText();
//                int version = request.get("version").asInt();
//                String technicalRequirement = request.get("technical_group").asText();
                Object creator = authentication.getPrincipal();
                System.out.println(creator);
                break;
            case "outline":
                break;
            case "other":
                break;
        }
        return ResponseEntity.status(418).body(syllabusService.getSyllabuses());
    }

    @GetMapping("/draft/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<List<Syllabus>> draftCreate(@PathVariable("type") String type) {
        return ResponseEntity.status(418).body(syllabusService.getSyllabuses());
    }

    @PutMapping("/update/{topicCode}")
    public ResponseEntity<Syllabus> updateSyllabusRequest(@PathVariable String topicCode, @RequestBody UpdateSyllabusRequest updateSyllabusRequest) {
        Syllabus updatedSyllabus = syllabusService.updateSyllabus(updateSyllabusRequest);
        if (updatedSyllabus != null) {
            return ResponseEntity.ok(updatedSyllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{topicCode}")
    public ResponseEntity<?> getSyllabusById(@PathVariable String topicCode) {
        Syllabus syllabus = syllabusService.getSyllabusById(topicCode);
        if (syllabus != null) {
            return ResponseEntity.ok(syllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
