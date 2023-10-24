package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.UserSyllabus;
import com.example.FAMS.models.composite_key.UserSyllabusCompositeKey;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.service_implementors.SyllabusServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
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

    @PostMapping("/downloadCSV")
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        syllabusService.downloadCSV();

        String computerAccountName = System.getProperty("user.name");
        File csvFile = new File("C:/Users/" + computerAccountName + "/Downloads/Template.csv");

        if (csvFile.exists()) {
            byte[] data = Files.readAllBytes(csvFile.toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Template.csv");

            return ResponseEntity.ok().headers(headers).body(data);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read CSV file.".getBytes());
        }
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<String> create(@PathVariable("type") String type, @RequestBody CreateSyllabusGeneralRequest request, Authentication authentication) {
        int result = -1;
//        log.info(syllabusDAO.findById(request.getTopicCode()).get());
        switch (type) {
            case "general":
                log.info(authentication.getPrincipal());
                result = syllabusService.createSyllabusGeneral(request, authentication);
                if (result == 1) {
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

    @PutMapping("/update/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:update')")
    public ResponseEntity<Syllabus> updateSyllabusRequest(@PathVariable String topicCode, @RequestBody UpdateSyllabusRequest updateSyllabusRequest) {
        Syllabus updatedSyllabus = syllabusService.updateSyllabus(updateSyllabusRequest);
        if (updatedSyllabus != null) {
            return ResponseEntity.ok(updatedSyllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<?> getSyllabusById(@PathVariable String topicCode) {
        Syllabus syllabus = syllabusService.getSyllabusById(topicCode);
        if (syllabus != null) {
            return ResponseEntity.ok(syllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/duplicate/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:update')")
    public ResponseEntity<?> duplicateTopicCode(@PathVariable String topicCode, Authentication authentication) {
        Syllabus syllabus = syllabusService.duplicateSyllabus(topicCode + "_[0-9]", authentication);
        return ResponseEntity.ok(syllabus);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<?> searchSyllabus(
            @RequestParam(name = "createdDate", required = false)
            String createdDate,
            @RequestParam(name = "searchValue", required = false)
            String searchValue,
            @RequestParam(name = "orderBy", required = false)
            String orderBy) {
        List<Syllabus> syllabusList = syllabusService.searchSyllabus(createdDate, searchValue, orderBy);
        return ResponseEntity.ok(syllabusList);
    }


}
