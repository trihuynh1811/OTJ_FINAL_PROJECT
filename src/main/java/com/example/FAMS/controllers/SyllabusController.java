package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.FileNameDTO;
import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.responses.Syllabus.*;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
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
    public ResponseEntity<List<GetAllSyllabusResponse>> get() {
        List<GetAllSyllabusResponse> syllabusList = syllabusService.getSyllabuses();
        log.info(syllabusList);
        return ResponseEntity.status(200).body(syllabusList);
    }

    @PostMapping("/get-presigned-url")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<PresignedUrlResponse> get(@RequestBody FileNameDTO request) {
        PresignedUrlResponse res = syllabusService.generatePresignedUrl(request.getFiles());
        if(res.getStatus() < 0){
            return ResponseEntity.status(500).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<GetSyllabusByPage> get(@RequestParam int amount, @RequestParam int pageNumber) {
        GetSyllabusByPage syllabusList = syllabusService.paging(amount, pageNumber);
        log.info(syllabusList);
        if(syllabusList.getStatus() > 0){
            return ResponseEntity.status(400).body(syllabusList);
        }
        else if(syllabusList.getStatus() < 0){
            return ResponseEntity.status(500).body(syllabusList);
        }
        return ResponseEntity.status(200).body(syllabusList);
    }

    @GetMapping("/detail/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:read')")
    public ResponseEntity<?> getDetail(@PathVariable String topicCode) {
        Syllabus syllabus = syllabusService.getDetailSyllabus(topicCode);
        if (syllabus != null) {
            return ResponseEntity.ok(syllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping("/importCSV")
//    public ResponseEntity<ResponseObject> loadDataInFile(@ModelAttribute CsvRequest csvRequest, Authentication authentication) throws IOException {
//        MultipartFile file = csvRequest.getFile();  // Access the MultipartFile from the CsvRequest
//        try {
//            List<Syllabus> syllabus = syllabusService.processDataFromCSV(file, authentication);
//            return ResponseEntity.ok(new ResponseObject("Successful", "List of CSV", syllabus));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't find the list", e.getMessage()));
//        }
//    }

    @PostMapping("/importCSV")
    public ResponseEntity<ResponseObject> loadDataInFile(@RequestParam("file") MultipartFile file,@RequestParam("choice") String choice, Authentication authentication) throws IOException {
        try {
            List<Syllabus> syllabus = syllabusService.processDataFromCSV(file,choice, authentication);
            return ResponseEntity.ok(new ResponseObject("Successful", "List of CSV", syllabus));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CSV file has exist.".getBytes());
        }
    }

    @PostMapping("/create/{type}")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<CreateSyllabusGeneralResponse> create(@PathVariable("type") String type, @RequestBody CreateSyllabusGeneralRequest request, Authentication authentication) {
        int result = -1;
        CreateSyllabusGeneralResponse res = null;
//        log.info(syllabusDAO.findById(request.getTopicCode()).get());
        switch (type) {
            case "general":
                log.info(authentication.getPrincipal());
                result = syllabusService.createSyllabusGeneral(request, authentication);
                switch (result) {
                    case -1:
                        res = CreateSyllabusGeneralResponse.builder()
                                .message("Server error, damn have to fix some bug :(.")
                                .build();
                        return ResponseEntity.status(500).body(res);
                    case 1:
                        res = CreateSyllabusGeneralResponse.builder()
                                .message("Syllabus is duplicated, change it or else.")
                                .build();
                        return ResponseEntity.status(418).body(res);
                    case 2:
                        res = CreateSyllabusGeneralResponse.builder()
                                .message("Successfully update syllabus.")
                                .build();
                        return ResponseEntity.status(200).body(res);
                }
                res = CreateSyllabusGeneralResponse.builder()
                        .message("Create syllabus successfully.")
                        .build();
                break;
            case "other":
                result = syllabusService.createSyllabusOther(request);
                if (result < 0) {
                    res = CreateSyllabusGeneralResponse.builder()
                            .message("Server error, dame have to fix some bug :(.")
                            .build();
                    return ResponseEntity.status(500).body(res);
                }
                res = CreateSyllabusGeneralResponse.builder()
                        .message("Save changes.")
                        .build();
                break;
        }
        return ResponseEntity.status(200).body(res);
    }

    @PostMapping("/create/outline")
    @PreAuthorize("hasAuthority('syllabus:create')")
    public ResponseEntity<?> create(@RequestBody CreateSyllabusOutlineRequest request, Authentication authentication) {
        syllabusService.createSyllabusOutline(request, authentication);
        return ResponseEntity.status(200).body(request);
    }


    @PutMapping("/update/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:update')")
    public ResponseEntity<UpdateSyllabusResponse> updateSyllabusRequest(
            @PathVariable String topicCode,
            @RequestBody UpdateSyllabusRequest updateSyllabusRequest) {
        UpdateSyllabusResponse updatedSyllabus = syllabusService.updateSyllabus(updateSyllabusRequest, topicCode);
        if (updatedSyllabus != null) {
            return ResponseEntity.ok(updatedSyllabus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/delete/{topicCode}")
    @PreAuthorize("hasAuthority('syllabus:update')")
    public ResponseEntity<DeleteSyllabusResponse> deleteSyllabus(
            @PathVariable String topicCode) {
        DeleteSyllabusResponse deleteSyllabus = syllabusService.deleteSyllabus(topicCode);
        if (deleteSyllabus.getStatus() > 0) {
            return ResponseEntity.status(400).body(deleteSyllabus);
        } else if (deleteSyllabus.getStatus() < 0) {
            return ResponseEntity.internalServerError().body(deleteSyllabus);
        }
        return ResponseEntity.ok(deleteSyllabus);
    }

    @PutMapping("/update/syllabus/outline")
    @PreAuthorize("hasAuthority('syllabus:update')")
    public ResponseEntity<String> updateSyllabusOutlineRequest(
            @RequestBody CreateSyllabusOutlineRequest request, Authentication authentication) {
        try{
            syllabusService.createSyllabusOutline(request, authentication);
            return ResponseEntity.ok("success");
        }catch (Exception err){
            err.printStackTrace();
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
        Syllabus duplicatedSyllabus = syllabusService.duplicateSyllabus(topicCode, authentication);

        return ResponseEntity.ok(duplicatedSyllabus);
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
