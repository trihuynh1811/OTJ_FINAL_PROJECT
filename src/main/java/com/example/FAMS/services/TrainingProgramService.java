package com.example.FAMS.services;

import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import com.example.FAMS.dto.responses.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface TrainingProgramService {
    ResponseEntity<ResponseObject> createTrainingProgram(TrainingProgramDTO trainingProgramDTO, int trainerID, String topicCode);

    ResponseEntity<ResponseObject> getAll();
}
