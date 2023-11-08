package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateTrainingProgramRequest;
import com.example.FAMS.dto.responses.ResponseObjectVersion2;
import com.example.FAMS.dto.responses.TrainingProgramDTO;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.UpdateTrainingProgramResponse;
import com.example.FAMS.models.TrainingProgram;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface TrainingProgramService {
    ResponseEntity<ResponseObject> createTrainingProgram(TrainingProgramDTO trainingProgramDTO);

    ResponseEntity<ResponseObject> getAll();

    UpdateTrainingProgramResponse updateTrainingProgram(int trainingProgramCode, String topicCode, UpdateTrainingProgramRequest updateTrainingProgramRequest);

    TrainingProgram duplicateTrainingProgram(int trainingProgramCode);

    ResponseEntity<ResponseObject> changeTrainingProgramStatus(int trainingProgramCode, String value);

    ResponseEntity<ResponseObject> processDataFromCSV(MultipartFile file, String choice, Authentication authentication) throws Exception;

    TrainingProgram searchTrainingProgram(String keyword);

    ResponseEntity<ResponseObjectVersion2> getTrainingProgramByCode(int code);
}
