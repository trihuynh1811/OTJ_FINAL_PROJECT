package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.TrainingProgramDTO;
import com.example.FAMS.dto.responses.TrainingProgramModified;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import com.example.FAMS.models.TrainingProgramSyllabus;
import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.repositories.TrainingProgramDAO;
import com.example.FAMS.repositories.TrainingProgramSyllabusDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.JWTService;
import com.example.FAMS.services.TrainingProgramService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class TrainingProgramImpl implements TrainingProgramService {
  private final JWTService jwtService;
  private final SyllabusDAO syllabusDAO;
  private final TrainingProgramDAO trainingProgramDAO;
  private final UserDAO userDAO;
  private final TrainingProgramSyllabusDAO trainingProgramSyllabusDAO;
  private List<TrainingProgramModified> userList;

  @Override
  public ResponseEntity<ResponseObject> createTrainingProgram(
      TrainingProgramDTO trainingProgramDTO, int trainerID, String topicCode) {
    TrainingProgram trainingProgram = new TrainingProgram();
    Date date = new Date();
    String token =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest()
            .getHeader("Authorization")
            .substring(7);
    String userEmail = jwtService.extractUserEmail(token);
    var requester = userDAO.findUserByEmail(userEmail).orElse(null);
    var person = userDAO.findById(trainerID).orElse(null);
    var syllabus = syllabusDAO.findById(topicCode).orElse(null);

    trainingProgram.setName(trainingProgramDTO.getName());
    if (!trainingProgramDTO.getStartDate().before(date)
        && date != trainingProgramDTO.getStartDate()) {
      trainingProgram.setStartDate(trainingProgramDTO.getStartDate());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ResponseObject(
                  "Failed", "The start day cannot below or equal current day", null));
    }
    if (trainingProgramDTO.getDuration() > 0) {
      trainingProgram.setDuration(trainingProgramDTO.getDuration());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ResponseObject("Failed", "The duration cannot be negative", null));
    }
    if (trainingProgramDTO.getStatus().contains("Active")
        || trainingProgramDTO.getStatus().contains("Inactive")
        || trainingProgramDTO.getStatus().contains("Drafting")) {
      trainingProgram.setStatus(trainingProgramDTO.getStatus());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ResponseObject(
                  "Failed", "The status must be Active or Drafting or Inactive", null));
    }
    if (person != null && person.getRole().getRole() == Role.TRAINER) {
      trainingProgram.setUserID(person);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ResponseObject("Failed", "The person is not a trainer or not found", null));
    }
    if (requester != null && syllabus != null) {
      trainingProgram.setCreatedBy(requester.getName());
      trainingProgram.setCreatedDate(date);
      trainingProgram.setModifiedBy(requester.getName());
      trainingProgram.setModifiedDate(date);
      var result = trainingProgramDAO.save(trainingProgram);

      createTrainingSyllabus(result, syllabus);
      return ResponseEntity.ok(new ResponseObject("Successful", "Added training program", result));
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ResponseObject("Failed", "Cannot found the user", null));
    }

  }

  public void createTrainingSyllabus(TrainingProgram trainingProgram, Syllabus syllabus) {
    TrainingProgramSyllabus trainingProgramSyllabus =
        TrainingProgramSyllabus.builder()
            .id(
                SyllabusTrainingProgramCompositeKey.builder()
                    .topicCode(syllabus.getTopicCode())
                    .trainingProgramCode(trainingProgram.getTrainingProgramCode())
                    .build())
            .topicCode(syllabus)
            .trainingProgramCode(trainingProgram)
            .sequence("high")
            .build();
    trainingProgramSyllabusDAO.save(trainingProgramSyllabus);
  }

  @Override
  public ResponseEntity<ResponseObject> getAll() {
    try {
      userList = trainingProgramDAO.findBy(TrainingProgramModified.class);
      return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
    } catch (Exception e) {
      userList = Collections.emptyList();
      return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
    }
  }
}
