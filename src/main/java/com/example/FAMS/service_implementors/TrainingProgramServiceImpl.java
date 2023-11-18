package com.example.FAMS.service_implementors;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
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
        if (person != null && person.getRole().getRole() == Role.TRAINER) {
            trainingProgram.setUserID(person);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "The person is not a trainer or not found", null));
        }
        if (requester != null) {
            trainingProgram.setStatus("active");
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

//    @Override
//    public ResponseEntity<ResponseObject> getAll() {
////    List<TrainingProgramModified> userList;
//        List<TrainingProgram> userList;
//        try {
////      userList = trainingProgramDAO.findBy(TrainingProgramModified.class);
//            userList = trainingProgramDAO.findBy(TrainingProgram.class);
//            return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
//        } catch (Exception e) {
//            userList = Collections.emptyList();
//            return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
//        }
//    }

    @Override
    public ResponseEntity<ResponseObject> getAllActive() {
        List<TrainingProgram> userList;

        try {
            userList = trainingProgramDAO.findTrainingProgramsByStatus("Active");

            return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
        } catch (Exception e) {
            userList = Collections.emptyList();
            return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getAll() {
        List<TrainingProgram> userList;
        try {
            userList = trainingProgramDAO.getAllBy();
            return ResponseEntity.ok(new ResponseObject("Successful", "Found user", userList));
        } catch (Exception e) {
            userList = Collections.emptyList();
            return ResponseEntity.ok(new ResponseObject("Failed", "Not found user", userList));
        }
    }

    @Override
    public ResponseEntity<ResponseTrainingProgram> updateTrainingProgram(
            int trainingProgramCode,
            TrainingProgramDTO trainingProgramDTO) {
        TrainingProgram trainingProgramExisted = trainingProgramDAO.findById(trainingProgramCode).orElse(null);

        if (trainingProgramExisted == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseTrainingProgram("Failed", "Training program not found", 0, null, null));
        }

        if (!trainingProgramDTO.getTrainingProgramName().equals(trainingProgramExisted.getName())) {
            if (trainingProgramDAO.getTrainingProgramByName(trainingProgramDTO.getTrainingProgramName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseTrainingProgram("Failed", "The Training Program name is already in use",0, null,null));
            }
        }

        if (trainingProgramDTO.getDuration() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseTrainingProgram("Failed", "The duration cannot be negative", 0,null,null));
        }
//        if (!"Active".equalsIgnoreCase(trainingProgramDTO.getStatus())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseTrainingProgram("Failed", "The status must be Active", 0,null,null));
//        }
        User trainer = userDAO.findByEmail(trainingProgramDTO.getTrainerGmail())
                .filter(user -> user.getRole().getRole() == Role.TRAINER)
                .orElse(null);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseTrainingProgram("Failed", "The person is not a trainer or not found",0,null, null));
        }

        String token = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest()
                .getHeader("Authorization")
                .substring(7);
        String userEmail = jwtService.extractUserEmail(token);
        var requester = userDAO.findUserByEmail(userEmail).orElse(null);

        if (requester == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseTrainingProgram("Failed", "Cannot find the user",0,null, null));
        }
        trainingProgramExisted.setName(trainingProgramDTO.getTrainingProgramName());
        trainingProgramExisted.setDuration(trainingProgramDTO.getDuration());
//        trainingProgramExisted.setStatus(trainingProgramDTO.getStatus());
        trainingProgramExisted.setUserID(trainer);
        trainingProgramExisted.setStartDate(new Date());
        trainingProgramExisted.setCreatedBy(requester.getName());
        trainingProgramExisted.setCreatedDate(new Date());
        trainingProgramExisted.setModifiedBy(requester.getName());
        trainingProgramExisted.setModifiedDate(new Date());

        TrainingProgram updatedProgram = trainingProgramDAO.save(trainingProgramExisted);


        List<TrainingProgramSyllabus> trainingProgramSyllabusList = updateTrainingSyllabus(trainingProgramExisted, trainingProgramDTO.getTopicCode());

        return ResponseEntity.ok(new ResponseTrainingProgram("Successful", "Updated training program", 0, updatedProgram, trainingProgramSyllabusList));
    }

    // Trong hàm updateTrainingSyllabus
    private List<TrainingProgramSyllabus> updateTrainingSyllabus(TrainingProgram trainingProgram, String[] topicCodes) {
        List<TrainingProgramSyllabus> trainingProgramSyllabusList = new ArrayList<>();
        SyllabusTrainingProgramCompositeKey compositeKey = new SyllabusTrainingProgramCompositeKey();
        compositeKey.setTrainingProgramCode(trainingProgram.getTrainingProgramCode());

        for (String element : topicCodes) {
            Syllabus syllabus = syllabusDAO.findById(element).orElse(null);

            if (syllabus != null) {
                boolean exists = trainingProgram.getTrainingProgramSyllabus().stream()
                        .anyMatch(existingSyllabus ->
                                existingSyllabus.getTopicCode().getTopicCode().equals(syllabus.getTopicCode()));

                if (!exists) {
                    TrainingProgramSyllabus trainingProgramSyllabus =
                            TrainingProgramSyllabus.builder()
                                    .id(
                                            SyllabusTrainingProgramCompositeKey.builder()
                                                    .topicCode(syllabus.getTopicCode())
                                                    .trainingProgramCode(trainingProgram.getTrainingProgramCode())
                                                    .build())
                                    .topicCode(syllabus)
                                    .trainingProgramCode(trainingProgram)
                                    .deleted(false)
                                    .build();
                    trainingProgramSyllabusList.add(trainingProgramSyllabus);
                }
            }
        }

        return trainingProgramSyllabusList;
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
        SimpleDateFormat[] dateFormats = {
                new SimpleDateFormat("dd/MM/yyyy"),
                new SimpleDateFormat("dd-MM-yyyy")
        };
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
                                    .startDate(parseDate(data[1], dateFormats))
                                    .duration(Integer.parseInt(data[2]))
                                    .status(data[3].equalsIgnoreCase("1") ? "active" : "inactive")
                                    .createdBy(getCreator(authentication).getName())
                                    .createdDate(parseDate(data[4], dateFormats))
                                    .modifiedBy(getCreator(authentication).getName())
                                    .modifiedDate(parseDate(data[5], dateFormats))
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
                                    .startDate(parseDate(data[1], dateFormats))
                                    .duration(Integer.parseInt(data[2]))
                                    .status(data[3])
                                    .createdBy(getCreator(authentication).getName())
                                    .createdDate(parseDate(data[4], dateFormats))
                                    .modifiedBy(getCreator(authentication).getName())
                                    .modifiedDate(parseDate(data[5], dateFormats))
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
    private java.sql.Date parseDate(String dateString, SimpleDateFormat[] dateFormats) throws ParseException {
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                Date parsedDate = dateFormat.parse(dateString);
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                log.info(dateFormat.toPattern() + " not equal " + dateString);
            }
        }
        throw new ParseException("Không thể chuyển đổi ngày", 0);
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
        if (checkExisted(trainingProgramCode, value)) {
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
                                "Training program with code " + trainingProgramCode + " is not found or already be " + value.toLowerCase() + "d",
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

    private boolean checkExisted(int trainingProgramCode, String value) {
        TrainingProgram trainingProgram = trainingProgramDAO.findById(trainingProgramCode).orElse(null);
        String result = value.equalsIgnoreCase("Activate") ? "active" : "inactive";
        return trainingProgram != null && !trainingProgram.getStatus().equalsIgnoreCase(result);
    }

    public User getCreator(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
