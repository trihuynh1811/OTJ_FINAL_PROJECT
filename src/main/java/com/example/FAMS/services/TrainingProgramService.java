package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.TrainingProgramDTO;
import org.springframework.http.ResponseEntity;

public interface TrainingProgramService {
    ResponseEntity<ResponseObject> createTrainingProgram(TrainingProgramDTO trainingProgramDTO, int trainerID);

    ResponseEntity<ResponseObject> getAll();
}
