package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateTrainingProgramRequest;
import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateTrainingProgramResponse;
import com.example.FAMS.models.TrainingProgram;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainingProgramService {
    ResponseEntity<ResponseObject> createTrainingProgram(TrainingProgramDTO trainingProgramDTO, int trainerID, String topicCode);

    ResponseEntity<ResponseObject> getAll();

    UpdateTrainingProgramResponse updateTrainingProgram(int trainingProgramCode, int  userId, UpdateTrainingProgramRequest updateTrainingProgramRequest);

    TrainingProgram duplicateTrainingProgram(int trainingProramCode);

    List<TrainingProgram> processDataFromCSV(MultipartFile file, Authentication authentication);
}
