package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.service_implementors.SyllabusServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@PreAuthorize("hasRole('CLASS_ADMIN') or hasRole('SUPER_ADMIN') or hasRole('TRAINER')")
@Log4j2
public class SyllabusController {

    @Autowired
    SyllabusServiceImpl syllabusService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    SyllabusDAO syllabusDAO;

    @GetMapping
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<?> get() {
        List<Syllabus> syllabusList = syllabusService.getSyllabuses();
        log.info(syllabusList);
        return ResponseEntity.status(200).body(syllabusList);
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<String> create(@PathVariable("type") String type, @RequestBody CreateSyllabusGeneralRequest request, Authentication authentication){
        int result = -1;
//        log.info(syllabusDAO.findById(request.getTopicCode()).get());
        switch(type){
            case "general":
                log.info(authentication.getPrincipal());
                result = syllabusService.createSyllabusGeneral(request, authentication);
                if(result == 1){
                    return ResponseEntity.status(418).body("syllabus is duplicated, change it or else.");
                }
                return ResponseEntity.status(200).body("syllabus created.");
            case "other":
                break;
        }
        return ResponseEntity.status(404).body(null);
    }

    @PostMapping("/create/outline")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<?> create(@RequestBody CreateSyllabusOutlineRequest request, Authentication authentication) {
        syllabusService.createSyllabusOutline(request, authentication);
        return ResponseEntity.status(200).body(request);
    }



    @GetMapping("/draft/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<List<Syllabus>> draftCreate(@PathVariable("type") String type) {
        return ResponseEntity.status(418).body(syllabusService.getSyllabuses());
    }


}
