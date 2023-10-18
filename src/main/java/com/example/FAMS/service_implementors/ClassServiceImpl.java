package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.Class.*;
import com.example.FAMS.models.*;
import com.example.FAMS.models.Class;
import com.example.FAMS.repositories.ClassDAO;
import com.example.FAMS.repositories.TrainingProgramDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.services.ClassService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassDAO classDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TrainingProgramDAO trainingProgramDAO;

    @Override
    public List<Class> getClasses() {
        return classDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public Class createClass(String className, String classCode, int duration, String location, Date startDate, Date endDate, String createdBy) {
        Class classInfo = Class.builder()
                .className(className)
                .classId(classCode)
                .duration(duration)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .createdBy(createdBy)
                .build();

        classDAO.save(classInfo);
        return classInfo;
    }


    @Override
    public UpdateClassResponse updateClass(UpdateClassRequest updateClassRequest) {
        Optional<Class> optionalClass = classDAO.findById(updateClassRequest.getClassCode());
        Class existingClass = optionalClass.orElse(null);
        if (existingClass != null) {
            existingClass =
                    Class.builder()
                            .classId(existingClass.getClassId())
                            .className(updateClassRequest.getClassName())
                            .classId(updateClassRequest.getClassCode())
                            .duration(updateClassRequest.getDuration())
                            .location(updateClassRequest.getLocation())
                            .startDate(updateClassRequest.getStartDate())
                            .endDate(updateClassRequest.getEndDate())
                            .createdBy(existingClass.getCreatedBy())
                            .createdDate(existingClass.getCreatedDate())
                            .modifiedBy(existingClass.getModifiedBy())
                            .modifiedDate(existingClass.getModifiedDate())
                            .build();

            Class updatedClass = classDAO.save(existingClass);

            if (updatedClass != null) {
                return UpdateClassResponse.builder()
                        .status("Update Class successful")
                        .updatedClass(updatedClass)
                        .build();
            } else {
                // Xử lý nếu việc cập nhật thất bại
                return UpdateClassResponse.builder()
                        .status("Update Class failed")
                        .updatedClass(null)
                        .build();
            }
        } else {
            // Xử lý nếu lớp học không tồn tại
            return UpdateClassResponse.builder()
                    .status("Class not found")
                    .updatedClass(null)
                    .build();
        }
    }

    @Override
    public ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode, boolean deactivated) {
        Class c = classDAO.findById(classCode).isPresent() ? classDAO.findById(classCode).get() : null;
        String d = deactivated ? "deactivate" : "activate";
        if (c != null) {
            c.setDeactivated(deactivated);
            return ResponseEntity.status(200).body(new DeactivateClassResponse("successfully " + d + " class with id " + classCode + " (⌐■_■)👍"));
        }
        return ResponseEntity.status(400).body(new DeactivateClassResponse("fail to " + d + " class with id " + classCode + " (⌐■⌒■)👎"));
    }

    @Override
    public ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException {
        Class c = classDAO.findById(classCode).isPresent() ? classDAO.findById(classCode).get() : null;
        if (c != null) {
            List<UserDTO> trainerList = new ArrayList<>();
            List<UserDTO> adminList = new ArrayList<>();
            List<SyllabusDTO> syllabusList;
            List<ClassUser> classUsers = c.getClassUsers().stream().toList();
            User user = null;

            for (int i = 0; i < classUsers.size(); i++) {
                if (classUsers.get(i).getUserType().equals("TRAINER")) {
                    user = userDAO.findById(classUsers.get(i).getUserID().getUserId()).get();
                    trainerList.add(UserDTO.builder()
                            .userEmail(user.getEmail())
                            .userName(user.getName())
                            .userId(user.getUserId())
                            .build());
                }
                if (classUsers.get(i).getUserType().equals("CLASS_ADMIN")) {
                    user = userDAO.findById(classUsers.get(i).getUserID().getUserId()).get();
                    adminList.add(UserDTO.builder()
                            .userEmail(user.getEmail())
                            .userName(user.getName())
                            .userId(user.getUserId())
                            .build());
                }
            }
            syllabusList = getAllSyllabusInTrainingProgram(c.getTrainingProgram().getTrainingProgramSyllabus().stream().toList());

            ClassDetailResponse res = ClassDetailResponse.builder()
                    .classId(classCode)
                    .className(c.getClassName())
                    .fsu(c.getFsu())
                    .createdBy(c.getCreatedBy())
                    .createdDate(c.getCreatedDate())
                    .deactivated(c.isDeactivated())
                    .duration(c.getDuration())
                    .endDate(c.getEndDate())
                    .location(c.getLocation())
                    .modifiedBy(c.getModifiedBy())
                    .modifiedDate(c.getModifiedDate())
                    .startDate(c.getStartDate())
                    .status(c.getStatus())
                    .adminList(adminList)
                    .trainerList(trainerList)
                    .trainingProgram(TrainingProgramDTO.builder()
                            .trainingProgramCode(c.getTrainingProgram().getTrainingProgramCode())
                            .trainingProgramName(c.getTrainingProgram().getName())
                            .modifyBy(c.getTrainingProgram().getModifiedBy())
                            .modifyDate(c.getTrainingProgram().getModifiedDate())
                            .build())
                    .syllabusList(syllabusList)
                    .message("found class with id " + classCode)
                    .build();
            return ResponseEntity.status(200).body(res);
        }
        return ResponseEntity.status(400).body(new ClassDetailResponse("class with id " + classCode + " not found"));
    }

    public List<Class> getDetailClasses() {
        return classDAO.findAll();
    }

    @Override
    public Class getClassById(String classId) {
        Optional<Class> optionalClass = classDAO.findById(classId);
        return optionalClass.orElse(null);
    }

    public List<SyllabusDTO> getAllSyllabusInTrainingProgram(
            List<TrainingProgramSyllabus> trainingProgramSyllabusList
    ) throws InterruptedException {
        List<SyllabusDTO> syllabusList = new ArrayList<>();
        int numThreads = calculateNumThreads(trainingProgramSyllabusList.size());

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        int batchSize = (int) Math.ceil((double) trainingProgramSyllabusList.size() / numThreads);
        int fromIndex = 0;

        for(int i = 0; i < numThreads; i++){
            int toIndex = Math.min(fromIndex + batchSize, trainingProgramSyllabusList.size());

            List<TrainingProgramSyllabus> tps = trainingProgramSyllabusList.subList(fromIndex, toIndex);

            executorService.submit(() -> {
                for(int j = 0; j < tps.size(); j++){
                    Syllabus syllabus = tps.get(j).getTopicCode();
                    SyllabusDTO syllabusDTO = SyllabusDTO.builder()
                            .topicCode(syllabus.getTopicCode())
                            .numberOfDay(syllabus.getNumberOfDay())
                            .version(syllabus.getVersion())
                            .publishStatus(syllabus.getPublishStatus())
                            .topicName(syllabus.getTopicName())
                            .createdBy(syllabus.getCreatedBy())
                            .createdDate(syllabus.getCreatedDate())
                            .build();
                    syllabusList.add(syllabusDTO);
                }
            });

            fromIndex = toIndex;

        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        return syllabusList;
    }

    private int calculateNumThreads(int numRecords) {
        final int maxThreads = 10;
        return Math.min(maxThreads, (int) Math.ceil((double) numRecords / 10));
    }

}
