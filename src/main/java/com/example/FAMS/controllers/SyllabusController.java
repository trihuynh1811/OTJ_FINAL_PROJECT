package com.example.FAMS.controllers;

import com.example.FAMS.models.Syllabus;
import com.example.FAMS.service_implementors.SyllabusService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;

    @GetMapping
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<List<Syllabus>> get() {
        return ResponseEntity.status(418).body(syllabusService.getSyllabuses());
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

}
