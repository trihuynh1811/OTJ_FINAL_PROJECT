package com.example.FAMS.controllers;

import com.example.FAMS.dto.requests.UpdateTrainingProgramRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.TrainingProgramDTO;
import com.example.FAMS.dto.responses.UpdateTrainingProgramResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import com.example.FAMS.service_implementors.TrainingProgramImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/training-program")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
public class TrainingProgramController {
    private final TrainingProgramImpl trainingProgram;



    @PostMapping("/create")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ResponseObject> createTrainingProgram(
            @RequestBody TrainingProgramDTO trainingProgramDTO,
            @RequestParam(name = "trainerID") int trainerID,
            @RequestParam(name = "topicCode") String topicCode
    ) {
        return trainingProgram.createTrainingProgram(trainingProgramDTO, trainerID, topicCode);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseObject> getAllTrainingProgram() {
        return trainingProgram.getAll();
    }

    @PutMapping("/update-program/{trainingProgramCode}/{userId}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<UpdateTrainingProgramResponse> updateTrainingProgramRequest(
            @PathVariable int trainingProgramCode,
            @PathVariable int userId,
            @RequestBody UpdateTrainingProgramRequest updateTrainingProgramRequest
    ) {
        return ResponseEntity.ok(trainingProgram.updateTrainingProgram(trainingProgramCode, userId, updateTrainingProgramRequest));
    }

    @GetMapping("/duplicate/{trainingProramCode}")
    @PreAuthorize("hasAuthority('training:read')")
    public ResponseEntity<TrainingProgram> duplicateTrainingProgram(@PathVariable int trainingProramCode){
        return ResponseEntity.ok(trainingProgram.duplicateTrainingProgram(trainingProramCode));
    }

    @PostMapping("/importCSV")
    @PreAuthorize("hasAuthority('training:import')")
    public ResponseEntity<?> loadDataInFile(@RequestParam("file") MultipartFile file,
                                            @RequestParam("choice") String choice,
                                            Authentication authentication) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }
        try {
            return trainingProgram.processDataFromCSV(file, choice, authentication);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the file.");
        }
    }

    @PostMapping("/activate/{trainingProgramCode}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<ResponseObject> activateTrainingProgram(@PathVariable int trainingProgramCode){
        return trainingProgram.changeTrainingProgramStatus(trainingProgramCode, "Activate");
    }

    @PostMapping("/deactivate/{trainingProgramCode}")
    @PreAuthorize("hasAnyAuthority('training:update')")
    public ResponseEntity<ResponseObject> deactivateTrainingProgram(@PathVariable int trainingProgramCode){
        return trainingProgram.changeTrainingProgramStatus(trainingProgramCode, "De-activate");
    }


}
