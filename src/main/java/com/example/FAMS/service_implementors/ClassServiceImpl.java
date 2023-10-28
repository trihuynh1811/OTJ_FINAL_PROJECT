package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.CalendarDayResponse;
import com.example.FAMS.dto.responses.CalendarWeekResponse;
import com.example.FAMS.dto.responses.Class.*;
import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.models.*;
import com.example.FAMS.dto.responses.*;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.composite_key.ClassLocationCompositeKey;
import com.example.FAMS.models.composite_key.ClassUserCompositeKey;
import com.example.FAMS.models.composite_key.UserClassSyllabusCompositeKey;
import com.example.FAMS.repositories.*;
import com.example.FAMS.services.ClassService;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassDAO classDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TrainingProgramDAO trainingProgramDAO;

    @Autowired
    SyllabusDAO syllabusDAO;

//    @Autowired
//    FsuDAO fsuDAO;

    @Autowired
    ClassLearningDayDAO classLearningDayDAO;

    @Autowired
    ClassUserDAO classUserDAO;

    @Autowired
    LocationDAO locationDAO;

//    @Autowired
//    ClassLocationDAO classLocationDAO;

    @Autowired
    UserClassSyllabusDAO userClassSyllabusDAO;

    List<CalendarDayResponse> dayCalendars;
    List<CalendarWeekResponse> weekCalendars;

    @Override
    public List<Class> getClasses() {
        return classDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public Class createClass(CreateClassDTO request, Authentication authentication) {
        try {
            log.info(request);
            Class classInfo = null;
            List<ClassUser> classUserList = new ArrayList<>();
            List<UserClassSyllabus> userSyllabusList = new ArrayList<>();
            List<ClassLearningDay> classLearningDayList = new ArrayList<>();
//            List<ClassLocation> classLocationList = new ArrayList<>();
//            Fsu fsu = fsuDAO.findById(request.getFsu().toUpperCase()).get();
            List<Location> locationList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            User user = userDAO.findByEmail(request.getCreated()).get();
            String timeFromStr = request.getClassTimeFrom().split(":").length == 3 ? request.getClassTimeFrom() : request.getClassTimeFrom() + ":00";
            String timeToStr = request.getClassTimeTo().split(":").length == 3 ? request.getClassTimeTo() : request.getClassTimeTo() + ":00";
            classInfo = Class.builder()
                    .className(request.getNameClass())
                    .classId(request.getClassCode())
                    .duration(request.getTotalTimeLearning())
                    .startDate(sdf.parse(request.getStartDate()))
                    .endDate(sdf.parse(request.getEndDate()))
                    .createdBy(user.getName())
                    .review(request.getReview())
                    .approve(request.getApprove())
                    .attendeeActual(Integer.parseInt(request.getAttendeeActual()))
                    .attendee(request.getAttendee())
                    .attendeePlanned(Integer.parseInt(request.getAttendeePlanned()))
                    .attendeeAccepted(Integer.parseInt(request.getAttendeeAccepted()))
                    .timeFrom(Time.valueOf(timeFromStr))
                    .timeTo(Time.valueOf(timeToStr))
                    .fsu(request.getFsu())
                    .status(request.getStatus())
                    .trainingProgram(trainingProgramDAO.findById(Integer.parseInt(request.getTrainingProgram())).get())
                    .createdDate(new java.util.Date())
                    .build();

            classDAO.save(classInfo);

            for (int i = 0; i < request.getListDay().size(); i++) {
                Date date = sdf.parse(request.getListDay().get(i));
                String[] getDate = request.getListDay().get(i).split("/");
                ClassLearningDay learningDay = ClassLearningDay.builder()
                        .classId(classInfo)
                        .date(Integer.parseInt(getDate[1]))
                        .month(Integer.parseInt(getDate[0]))
                        .year(Integer.parseInt(getDate[2]))
                        .enrollDate(date)
                        .timeFrom(classInfo.getTimeFrom())
                        .timeTo(classInfo.getTimeTo())
                        .build();

                classLearningDayList.add(learningDay);
            }

            for (int i = 0; i < request.getAttendeeList().size(); i++) {
                user = userDAO.findByEmail(request.getAttendeeList().get(i)).get();
                ClassUser classUser = ClassUser.builder()
                        .id(ClassUserCompositeKey.builder()
                                .userId(user.getUserId())
                                .classId(classInfo.getClassId())
                                .build())
                        .userID(user)
                        .classId(classInfo)
                        .userType(user.getRole().getRole().name())
                        .build();

                classUserList.add(classUser);
            }

            for (int i = 0; i < request.getTrainer().size(); i++) {
                user = userDAO.findByEmail(request.getTrainer().get(i).getGmail()).get();
                for (int j = 0; j < request.getTrainer().get(i).getClassCode().size(); j++) {
                    Syllabus s = syllabusDAO.findById(request.getTrainer().get(i).getClassCode().get(j)).get();

                    UserClassSyllabus userClassSyllabus = UserClassSyllabus.builder()
                            .id(UserClassSyllabusCompositeKey.builder()
                                    .classCode(classInfo.getClassId())
                                    .userId(user.getUserId())
                                    .topicCode(s.getTopicCode())
                                    .build())
                            .classCode(classInfo)
                            .topicCode(s)
                            .userId(user)
                            .userType(user.getRole().getRole().name())
                            .build();

                    userSyllabusList.add(userClassSyllabus);
                }
            }

            for (int i = 0; i < request.getLocation().size(); i++) {
//                Location location = locationDAO.findById(Long.parseLong(request.getLocation().get(i))).get();
//                ClassLocation cl = ClassLocation.builder()
//                        .id(ClassLocationCompositeKey.builder()
//                                .classCode(classInfo.getClassId())
//                                .locationId(location.getLocationId())
//                                .build())
//                        .locationCode(location)
//                        .classCode(classInfo)
//                        .build();

                Location l = Location.builder()
                        .location(request.getLocation().get(i))
                        .classId(classInfo)
                        .build();

//                classLocationList.add(cl);
                locationList.add(l);
            }

            classLearningDayDAO.saveAll(classLearningDayList);
            classUserDAO.saveAll(classUserList);
            userClassSyllabusDAO.saveAll(userSyllabusList);
//            classLocationDAO.saveAll(classLocationList);
            locationDAO.saveAll(locationList);

            CreateClassResponse res = CreateClassResponse.builder()
                    .createdClass(classInfo)
                    .message("create class successfully.")
                    .build();
            return classInfo;

        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }

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
                            .startDate(updateClassRequest.getStartDate())
                            .endDate(updateClassRequest.getEndDate())
                            .createdBy(existingClass.getCreatedBy())
                            .createdDate(existingClass.getCreatedDate())
                            .modifiedBy(existingClass.getModifiedBy())
                            .modifiedDate(existingClass.getModifiedDate())
                            .classLearningDays(
                                    existingClass.getClassLearningDays()) // Bổ sung cập nhật danh sách ngày học
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
            log.info(c.getTrainingProgram().getTrainingProgramSyllabus().size());
            List<UserDTO> trainerList = new ArrayList<>();
            List<UserDTO> adminList = new ArrayList<>();
            List<SyllabusDTO> syllabusList = new ArrayList<>();
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
            if (!c.getTrainingProgram().getTrainingProgramSyllabus().isEmpty()) {
                syllabusList = getAllSyllabusInTrainingProgram(c.getTrainingProgram().getTrainingProgramSyllabus().stream().toList());
            }

            ClassDetailResponse res = ClassDetailResponse.builder()
                    .classId(classCode)
                    .className(c.getClassName())
                    .createdBy(c.getCreatedBy())
                    .createdDate(c.getCreatedDate())
                    .deactivated(c.isDeactivated())
                    .duration(c.getDuration())
                    .endDate(c.getEndDate())
                    .modifiedBy(c.getModifiedBy())
                    .modifiedDate(c.getModifiedDate())
                    .startDate(c.getStartDate())
                    .status(c.getStatus())
                    .adminList(adminList)
                    .trainerList(trainerList)
                    .timeFrom(c.getTimeFrom())
                    .timeTo(c.getTimeTo())
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

    @Override
    public List<Class> CalendarSort() {
        return classDAO.getCalendarSort();
    }

    @Override
    public List<Class> getAll() {
        return classDAO.getAll();
    }

    @Override
    public ResponseEntity<ResponseObject> getDayCalendar(java.util.Date currentDate) {
        try {
            dayCalendars = classDAO.getCalendarByDay(currentDate);
            return ResponseEntity.ok(new ResponseObject("Successful", "List of classroom", dayCalendars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseObject> getWeekCalendar(java.util.Date startDate, java.util.Date endDate) {
        try {
            weekCalendars = classDAO.getCalendarByWeek(startDate, endDate);
            return ResponseEntity.ok(new ResponseObject("Successful", "List of classroom", weekCalendars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
        }
    }

    @Override
    public UpdateCalendarResponse updateClassLearningDay(UpdateCalendarRequest request) throws ParseException {
        String id = request.getId();
        String enrollDate = request.getEnrollDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date eDate = dateFormat.parse(enrollDate);
        Time timeFrom = request.getTimeFrom();
        Time timeTo = request.getTimeTo();
        String value = request.getValue();

        ClassLearningDay classLearningDay = classLearningDayDAO.findByClassId_ClassIdAndEnrollDate(id, eDate);


        if (classLearningDay != null) {
            if ("Only".equals(value)) {
                classLearningDay.setTimeFrom(timeFrom);
                classLearningDay.setTimeTo(timeTo);
                classLearningDay = classLearningDayDAO.save(classLearningDay);
            } else if ("All".equals(value)) {
                List<ClassLearningDay> classLearningDays = classLearningDayDAO.findByClassId_ClassId(id);
                for (ClassLearningDay day : classLearningDays) {
                    day.setTimeFrom(timeFrom);
                    day.setTimeTo(timeTo);
                }
                classLearningDayDAO.saveAll(classLearningDays);
            }

            if (classLearningDay != null) {
                return UpdateCalendarResponse.builder()
                        .status("Update Calendar successful")
                        .updateClassLearningDay(classLearningDay)
                        .build();
            } else {
                return UpdateCalendarResponse.builder()
                        .status("Update Calendar failed")
                        .updateClassLearningDay(classLearningDay)
                        .build();
            }
        } else {
            return UpdateCalendarResponse.builder()
                    .status("Calendar not found")
                    .updateClassLearningDay(null)
                    .build();
        }
    }


    public List<Class> searchClass(String createdDate, String searchValue, String orderBy) {
        List<Class> classList = classDAO.findAll();

        if (!Strings.isNullOrEmpty(createdDate)) {
            classList =
                    classList.stream()
                            .filter(
                                    c ->
                                            new SimpleDateFormat("yyyy-MM-dd")
                                                    .format(c.getCreatedDate())
                                                    .equals(createdDate))
                            .collect(Collectors.toList());
        }

        if (!Strings.isNullOrEmpty(searchValue)) {
            classList =
                    classList.stream()
                            .filter(
                                    c ->
                                            c.getClassName().toLowerCase().contains(searchValue.trim().toLowerCase())
                                                    || c.getClassId()
                                                    .toLowerCase()
                                                    .contains(searchValue.trim().toLowerCase()))
                            .collect(Collectors.toList());
        }

        if (!Strings.isNullOrEmpty(orderBy)) {
            if (orderBy.equals("asc")) {
                classList.sort(Comparator.comparing(Class::getClassName));
            } else if (orderBy.equals("desc")) {
                classList.sort(Comparator.comparing(Class::getClassName).reversed());
            }
        }

        return classList;
    }


    public List<SyllabusDTO> getAllSyllabusInTrainingProgram(
            List<TrainingProgramSyllabus> trainingProgramSyllabusList
    ) throws InterruptedException {
        List<SyllabusDTO> syllabusList = new ArrayList<>();
        int numThreads = calculateNumThreads(trainingProgramSyllabusList.size());

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        int batchSize = (int) Math.ceil((double) trainingProgramSyllabusList.size() / numThreads);
        int fromIndex = 0;

        for (int i = 0; i < numThreads; i++) {
            int toIndex = Math.min(fromIndex + batchSize, trainingProgramSyllabusList.size());

            List<TrainingProgramSyllabus> tps = trainingProgramSyllabusList.subList(fromIndex, toIndex);

            executorService.submit(() -> {
                for (int j = 0; j < tps.size(); j++) {
                    Syllabus syllabus = tps.get(j).getTopicCode();
                    SyllabusDTO syllabusDTO = SyllabusDTO.builder()
                            .topicCode(syllabus.getTopicCode())
                            .numberOfDay(syllabus.getNumberOfDay())
                            .version(syllabus.getVersion())
                            .publishStatus(syllabus.getPublishStatus())
                            .topicName(syllabus.getTopicName())
                            .createdBy(syllabus.getCreatedBy().getName())
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

    public User getCreator(Authentication authentication) {
        Object creator = authentication.getPrincipal();
        if (creator instanceof User) {
            return (User) creator;
        }
        return null;
    }


}
