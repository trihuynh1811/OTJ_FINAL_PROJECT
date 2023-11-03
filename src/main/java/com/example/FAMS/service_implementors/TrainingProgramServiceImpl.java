package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateTrainingProgramRequest;
import com.example.FAMS.dto.responses.*;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import com.example.FAMS.models.TrainingProgramSyllabus;
import com.example.FAMS.models.User;
import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.repositories.TrainingProgramDAO;
import com.example.FAMS.repositories.TrainingProgramSyllabusDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.JWTService;
import com.example.FAMS.services.TrainingProgramService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final JWTService jwtService;
    private final SyllabusDAO syllabusDAO;
    private final Logger logger = LoggerFactory.getLogger(TrainingProgramServiceImpl.class);
    private final TrainingProgramDAO trainingProgramDAO;
    private final UserDAO userDAO;
    private final TrainingProgramSyllabusDAO trainingProgramSyllabusDAO;

    @Override
    public ResponseEntity<ResponseObject> createTrainingProgram(
            TrainingProgramDTO trainingProgramDTO) {
        TrainingProgram trainingProgram = new TrainingProgram();
        Date date = new Date();
        String token =
                ((ServletRequestAttributes)
                        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest()
                        .getHeader("Authorization")
                        .substring(7);
        String userEmail = jwtService.extractUserEmail(token);
        var requester = userDAO.findUserByEmail(userEmail).orElse(null);
        var person = userDAO.findByEmail(trainingProgramDTO.getTrainerGmail()).orElse(null);
        if (trainingProgramDAO.getTrainingProgramByName(trainingProgramDTO.getTrainingProgramName()).orElse(null) == null) {
            trainingProgram.setName(trainingProgramDTO.getTrainingProgramName());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "The Training Program name already been used", null));
        }
        if (trainingProgramDTO.getDuration() > 0) {
            trainingProgram.setDuration(trainingProgramDTO.getDuration());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "The duration cannot be negative", null));
        }
        if (trainingProgramDTO.getStatus().contains("Active")
                || trainingProgramDTO.getStatus().contains("inActive")
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
        if (requester != null) {
            trainingProgram.setStartDate(date);
            trainingProgram.setCreatedBy(requester.getName());
            trainingProgram.setCreatedDate(date);
            trainingProgram.setModifiedBy(requester.getName());
            trainingProgram.setModifiedDate(date);
            var result = trainingProgramDAO.save(trainingProgram);

            createTrainingSyllabus(result, trainingProgramDTO.getTopicCode()); // problem
            return ResponseEntity.ok(new ResponseObject("Successful", "Added training program", result));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "Cannot found the user", null));
        }
    }

    public void createTrainingSyllabus(TrainingProgram trainingProgram, String[] syllabus) {
        try {
            for (String element : syllabus) {
                var Item = syllabusDAO.findById(element).orElse(null);
                if (Item != null) {
                    TrainingProgramSyllabus trainingProgramSyllabus =
                            TrainingProgramSyllabus.builder()
                                    .id(
                                            SyllabusTrainingProgramCompositeKey.builder()
                                                    .topicCode(Item.getTopicCode())
                                                    .trainingProgramCode(trainingProgram.getTrainingProgramCode())
                                                    .build())
                                    .topicCode(Item)
                                    .trainingProgramCode(trainingProgram)
                                    .deleted(false)
                                    .build();
                    trainingProgramSyllabusDAO.save(trainingProgramSyllabus);
                }
            }
        } catch (Exception e) {
            logger.info("Error occur in create TrainingSyllabus func " + e);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAll() {
        List<TrainingProgramModified> userList;
        try {
            userList = trainingProgramDAO.findBy(TrainingProgramModified.class);
            return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
        } catch (Exception e) {
            userList = Collections.emptyList();
            return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
        }
    }

    @Override
    public UpdateTrainingProgramResponse updateTrainingProgram(
            int trainingProgramCode,
            int userId,
            String topicCode,
            UpdateTrainingProgramRequest updateTrainingProgramRequest) {

        User userExisted = userDAO.findById(userId).orElse(null);

        if (userExisted == null) {
            return UpdateTrainingProgramResponse.builder().messager("User not found").build();
        }

        TrainingProgram trainingProgramExisted = trainingProgramDAO.findById(trainingProgramCode).orElse(null);
        Syllabus syllabusExisted = syllabusDAO.findById(topicCode).orElse(null);

        if (trainingProgramExisted != null) {
            trainingProgramExisted.setName(updateTrainingProgramRequest.getName());
            trainingProgramExisted.setStartDate(updateTrainingProgramRequest.getStartDate());
            trainingProgramExisted.setDuration(updateTrainingProgramRequest.getDuration());

            String status = trainingProgramExisted.getStatus();
            if (status != null && (status.equalsIgnoreCase("active"))) {
                trainingProgramExisted.setStatus(updateTrainingProgramRequest.getStatus());
            }
            trainingProgramExisted.setCreatedBy(updateTrainingProgramRequest.getCreatedBy());
            trainingProgramExisted.setCreatedDate(updateTrainingProgramRequest.getCreatedDate());
            trainingProgramExisted.setModifiedBy(updateTrainingProgramRequest.getModifiedBy());
            trainingProgramExisted.setModifiedDate(new Date());
            if (syllabusExisted != null) {
                TrainingProgramSyllabus trainingProgramSyllabus =
                        trainingProgramSyllabusDAO
                                .findByIdTopicCodeAndIdTrainingProgramCode(syllabusExisted.getTopicCode(), trainingProgramExisted.getTrainingProgramCode());
                trainingProgramSyllabus.setDeleted(updateTrainingProgramRequest.isDeleted());
                trainingProgramDAO.save(trainingProgramExisted);
                trainingProgramSyllabusDAO.save(trainingProgramSyllabus);

                return UpdateTrainingProgramResponse.builder()
                        .messager("Update training program success")
                        .updateTrainingProgram(trainingProgramExisted)
                        .build();
            } else {
                return UpdateTrainingProgramResponse.builder()
                        .messager("Syllabus not found")
                        .updateTrainingProgram(trainingProgramExisted)
                        .build();
            }
        } else {
            return UpdateTrainingProgramResponse.builder()
                    .messager("Training program not found")
                    .updateTrainingProgram(null)
                    .build();
        }
    }


    @Override
    public TrainingProgram duplicateTrainingProgram(int trainingProgramCode) {
        TrainingProgram originalTraining = trainingProgramDAO.findById(trainingProgramCode)
                .orElseThrow(() -> new EntityNotFoundException("Training program not found with code: " + trainingProgramCode));

        TrainingProgram newTrainingProgram = TrainingProgram.builder()
                .name(originalTraining.getName() + LocalTime.now().toString())
                .duration(originalTraining.getDuration())
                .userID(originalTraining.getUserID())
                .startDate(originalTraining.getStartDate())
                .createdBy(originalTraining.getCreatedBy())
                .createdDate(originalTraining.getCreatedDate())
                .modifiedDate(originalTraining.getModifiedDate())
                .modifiedBy(originalTraining.getModifiedBy())
                .status(originalTraining.getStatus())
                .build();
        return trainingProgramDAO.save(newTrainingProgram);
    }

    @Override
    public ResponseEntity<ResponseObject> processDataFromCSV(
            MultipartFile file, String choice, Authentication authentication) throws Exception {
        int count = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        boolean firstLine = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] data = line.split(",");
            TrainingProgram trainingProgramOption =
                    trainingProgramDAO.findById(Integer.parseInt(data[6])).orElse(null);
            if (data.length == 7) {
                if (choice.equalsIgnoreCase("Replace")) {
                    if (trainingProgramOption != null) {
                        trainingProgramDAO.deleteById(trainingProgramOption.getTrainingProgramCode());
                    }
                    count++;
                    trainingProgramDAO.save(
                            TrainingProgram.builder()
                                    .name(data[0])
                                    .userID(getCreator(authentication))
                                    .startDate(new java.sql.Date(dateFormat.parse(data[1]).getTime()))
                                    .duration(Integer.parseInt(data[2]))
                                    .status(data[3].equalsIgnoreCase("1") ? "active" : "inactive")
                                    .createdBy(getCreator(authentication).getName())
                                    .createdDate(new java.sql.Date(dateFormat.parse(data[4]).getTime()))
                                    .modifiedBy(getCreator(authentication).getName())
                                    .modifiedDate(new java.sql.Date(dateFormat.parse(data[5]).getTime()))
                                    .trainingProgramCode(Integer.parseInt(data[6]))
                                    .build());
                }

            } else {
                if (trainingProgramOption == null) {
                    count++;
                    trainingProgramDAO.save(
                            TrainingProgram.builder()
                                    .name(data[0])
                                    .userID(getCreator(authentication))
                                    .startDate(new java.sql.Date(dateFormat.parse(data[1]).getTime()))
                                    .duration(Integer.parseInt(data[2]))
                                    .status(data[3])
                                    .createdBy(getCreator(authentication).getName())
                                    .createdDate(new java.sql.Date(dateFormat.parse(data[4]).getTime()))
                                    .modifiedBy(getCreator(authentication).getName())
                                    .modifiedDate(new java.sql.Date(dateFormat.parse(data[5]).getTime()))
                                    .trainingProgramCode(Integer.parseInt(data[6]))
                                    .build());
                }
            }
        }
        if (count > 0) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            ResponseObject.builder()
                                    .status("Success")
                                    .message("Import successfully")
                                    .payload(null)
                                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseObject.builder().status("Fail").message("Import failed").payload(null).build());
    }

    @Override
    public TrainingProgram searchTrainingProgram(String keyword) {
        List<TrainingProgram> trainingProgramList = trainingProgramDAO.findAll();
        TrainingProgram trainingProgramByName = getNameIfExisted(keyword, trainingProgramList);
        TrainingProgram trainingProgramByCode = getCodeIfExisted(keyword, trainingProgramList);
        if (trainingProgramByName == null) {
            return trainingProgramByCode;
        }
        return trainingProgramByName;
    }

    @Override
    public ResponseEntity<ResponseObjectVersion2> getTrainingProgramByCode(int code) {
        try {
            Integer totalSubjectDays = trainingProgramDAO.totalSubjectDays(code);
            Integer totalTrainingProgramDates = trainingProgramDAO.totalTrainingProgramDates(code);

            List<TrainingProgramDetails> object = trainingProgramDAO.getTrainingProgramDetails(code);
            return ResponseEntity.ok(
                    new ResponseObjectVersion2(
                            "Successful", "Found user", totalSubjectDays, totalTrainingProgramDates, object));
        } catch (Exception e) {
            var object = Collections.emptyList();
            return ResponseEntity.ok(
                    new ResponseObjectVersion2("Failed", "Not found user", 0, 0, object));
        }
    }

    private TrainingProgram getNameIfExisted(String name, List<TrainingProgram> trainingProgramList) {
        for (TrainingProgram trainingProgram : trainingProgramList) {
            if (trainingProgram.getName().equalsIgnoreCase(name)
                    && trainingProgram.getStatus().equalsIgnoreCase("active")) return trainingProgram;
        }
        return null;
    }

    private TrainingProgram getCodeIfExisted(String code, List<TrainingProgram> trainingProgramList) {
        for (TrainingProgram trainingProgram : trainingProgramList) {
            if (Integer.toString(trainingProgram.getTrainingProgramCode()).equalsIgnoreCase(code)
                    && trainingProgram.getStatus().equalsIgnoreCase("active")) return trainingProgram;
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> changeTrainingProgramStatus(
            int trainingProgramCode, String value) {
        if (checkExisted(trainingProgramCode)) {
            switch (value) {
                case "Activate":
                    activateProgram(trainingProgramCode);
                    break;
                case "De-activate":
                    deactivateProgram(trainingProgramCode);
                    break;
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ResponseObject(
                                    value + " training program successfully",
                                    "Training program with code "
                                            + trainingProgramCode
                                            + " is now "
                                            + value.toLowerCase(),
                                    trainingProgramDAO.findById(trainingProgramCode).orElse(null)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ResponseObject(
                                value + " training program failed",
                                "Training program with code " + trainingProgramCode + " is not found",
                                "No data"));
    }

    private void activateProgram(int trainingProgramCode) {
        TrainingProgram trainingProgram = trainingProgramDAO.findById(trainingProgramCode).orElse(null);
        if (trainingProgram != null) {
            trainingProgram.setStatus("active");
            trainingProgramDAO.save(trainingProgram);
        }
    }

    private void deactivateProgram(int trainingProgramCode) {
        TrainingProgram trainingProgram = trainingProgramDAO.findById(trainingProgramCode).orElse(null);
        if (trainingProgram != null) {
            trainingProgram.setStatus("inactive");
            trainingProgramDAO.save(trainingProgram);
        }
    }

    private boolean checkExisted(int trainingProgramCode) {
        return trainingProgramDAO.existsById(trainingProgramCode);
    }

    public User getCreator(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
