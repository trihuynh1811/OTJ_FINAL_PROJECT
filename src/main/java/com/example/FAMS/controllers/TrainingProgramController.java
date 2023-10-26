package com.example.FAMS.controllers;

import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.service_implementors.TrainingProgramImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


}
