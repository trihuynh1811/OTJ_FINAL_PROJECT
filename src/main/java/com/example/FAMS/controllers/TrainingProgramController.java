package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateTrainingProgramRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.ResponseObjectVersion2;
import com.example.FAMS.dto.responses.TrainingProgramDTO;
import com.example.FAMS.dto.responses.UpdateTrainingProgramResponse;
import com.example.FAMS.models.TrainingProgram;
import com.example.FAMS.services.TrainingProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/training-program")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
public class TrainingProgramController {

    private final TrainingProgramService trainingProgram;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ResponseObject> createTrainingProgram(
            @RequestBody TrainingProgramDTO trainingProgramDTO) {
        return trainingProgram.createTrainingProgram(trainingProgramDTO);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObjectVersion2> getTrainingProgram(@PathVariable int id) {
        return trainingProgram.getTrainingProgramByCode(id);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObject> getAllTrainingProgram() {
        return trainingProgram.getAll();
    }

    @PutMapping("/update-program/{trainingProgramCode}/{topicCode}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<UpdateTrainingProgramResponse> updateTrainingProgramRequest(
            @PathVariable int trainingProgramCode,
            @PathVariable String topicCode,
            @RequestBody UpdateTrainingProgramRequest updateTrainingProgramRequest) {
        return ResponseEntity.ok(
                trainingProgram.updateTrainingProgram(
                        trainingProgramCode, topicCode, updateTrainingProgramRequest));
    }

    @GetMapping("/duplicate/{trainingProgramCode}")
    @PreAuthorize("hasAuthority('training:read')")
    public ResponseEntity<TrainingProgram> duplicateTrainingProgram(
            @PathVariable int trainingProgramCode) {
        return ResponseEntity.ok(trainingProgram.duplicateTrainingProgram(trainingProgramCode));
    }

    @PostMapping("/importCSV")
    @PreAuthorize("hasAuthority('training:import')")
    public ResponseEntity<?> loadDataInFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("choice") String choice,
            Authentication authentication)
            throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }
        try {
            return trainingProgram.processDataFromCSV(file, choice, authentication);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the file.");
        }
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyAuthority('training:read')")
    public Object searchTrainingProgram(@RequestParam(name = "keyword") String keyword) {
        return trainingProgram.searchTrainingProgram(keyword) != null ?
                trainingProgram.searchTrainingProgram(keyword) : "no data";
    }

    @PostMapping("/activate/{trainingProgramCode}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<ResponseObject> activateTrainingProgram(
            @PathVariable int trainingProgramCode) {
        return trainingProgram.changeTrainingProgramStatus(trainingProgramCode, "Activate");
    }

    @PostMapping("/deactivate/{trainingProgramCode}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<ResponseObject> deactivateTrainingProgram(
            @PathVariable int trainingProgramCode) {
        return trainingProgram.changeTrainingProgramStatus(trainingProgramCode, "De-activate");
    }

    @GetMapping("/TemplateCSV")
    public ResponseEntity<InputStreamResource> downloadTemplateCSV() {
        String csvData = "trainingProgramCode,name,userID,startDate,duration,status,createdBy,createdDate,modifiedBy,modifiedDate";
        String saveDirectory = "D:\\";
        try {
            File file = new File(saveDirectory + "TemplateCSV.csv");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(csvData.getBytes());
            outputStream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TemplateCSV.csv");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
