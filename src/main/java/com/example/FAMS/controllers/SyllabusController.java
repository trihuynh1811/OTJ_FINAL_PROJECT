package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.service_implementors.SyllabusServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
        return ResponseEntity.ok(syllabusService.getDetailSyllabus());
    }

    @PostMapping("/importCSV")
    public ResponseEntity<?> loadDataInFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            List<Syllabus> syllabus = syllabusService.processDataFromCSV(file);
            return ResponseEntity.ok(syllabus);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<List<Syllabus>> create(@PathVariable("type") String type, @RequestBody JsonNode request, Authentication authentication) {
        switch (type) {
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

    @GetMapping("/duplicate/{topicCode}")
    public ResponseEntity<?> duplicateTopicCode(@PathVariable String topicCode) {
        Syllabus updatesyllabusRequest = syllabusService.duplicateSyllabus(topicCode + "_[0-9]");
        boolean check = true;
        if (updatesyllabusRequest == null) {
            updatesyllabusRequest = syllabusService.getSyllabusById(topicCode);
            check = false;
        }
        Syllabus syllabusexits = new Syllabus();
        syllabusexits.setTopicName(updatesyllabusRequest.getTopicName());
        syllabusexits.setLearningObjectives(updatesyllabusRequest.getLearningObjectives());
        syllabusexits.setUserID(updatesyllabusRequest.getUserID());
        syllabusexits.setTrainingPrinciples(updatesyllabusRequest.getTrainingPrinciples());
        syllabusexits.setVersion(updatesyllabusRequest.getVersion());
        syllabusexits.setTechnicalGroup(updatesyllabusRequest.getTechnicalGroup());
        syllabusexits.setVersion(updatesyllabusRequest.getVersion());
        syllabusexits.setTrainingAudience(updatesyllabusRequest.getTrainingAudience());
        syllabusexits.setTopicOutline(updatesyllabusRequest.getTopicOutline());
        syllabusexits.setTrainingMaterials(updatesyllabusRequest.getTrainingMaterials());
        syllabusexits.setPriority(updatesyllabusRequest.getPriority());
        syllabusexits.setPublishStatus(updatesyllabusRequest.getPublishStatus());
        syllabusexits.setCreatedBy(updatesyllabusRequest.getCreatedBy());
        syllabusexits.setCreatedDate(new Date());
        syllabusexits.setModifiedBy(updatesyllabusRequest.getModifiedBy());
        syllabusexits.setModifiedDate(new Date());
        topicCode = updatesyllabusRequest.getTopicCode();
        String topicCodeClone = "";
        if (check) {
            int index = topicCode.lastIndexOf('_');
            topicCodeClone = topicCode.substring(0, index + 1) + (Integer.parseInt(topicCode.substring(index + 1)) + 1);
        } else {
            topicCodeClone += topicCode + "_1";
        }
        syllabusexits.setTopicCode(topicCodeClone);
        return ResponseEntity.ok(syllabusService.saveSyllabus(syllabusexits));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSyllabus(@RequestParam(name = "createdDate", required = false)
                                            String createdDate,
                                            @RequestParam(name = "searchValue", required = false)
                                            String searchValue,
                                            @RequestParam(name = "orderBy", required = false)
                                            String orderBy) {
        List<Syllabus> syllabusList = syllabusService.searchSyllabus(createdDate, searchValue, orderBy);
        return ResponseEntity.ok(syllabusList);
    }



}
