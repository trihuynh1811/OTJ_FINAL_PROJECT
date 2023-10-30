package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.Calendar.UpdateCalendarRequest;
import com.example.FAMS.dto.requests.ClassRequest.CreateClassDTO;
import com.example.FAMS.dto.requests.ClassRequest.UpdateClass3Request;
import com.example.FAMS.dto.requests.UpdateClassRequest;
import com.example.FAMS.dto.responses.CalendarDayResponse;
import com.example.FAMS.dto.responses.CalendarWeekResponse;
import com.example.FAMS.dto.responses.Class.*;
import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import com.example.FAMS.dto.responses.UpdateCalendarResponse;
import com.example.FAMS.models.*;
import com.example.FAMS.dto.responses.*;
import com.example.FAMS.models.Class;
import com.example.FAMS.models.composite_key.ClassUserCompositeKey;
import com.example.FAMS.models.composite_key.SyllabusTrainingProgramCompositeKey;
import com.example.FAMS.models.composite_key.UserClassSyllabusCompositeKey;
import com.example.FAMS.repositories.*;
import com.example.FAMS.services.ClassService;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    TrainingProgramSyllabusDAO trainingProgramSyllabusDAO;

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
    List<SearchFilterResponse> filterResponses;

    List<CalendarDayResponse> dayCalendars;
    List<CalendarWeekResponse> weekCalendars;

    @Override
    public List<GetClassesResponse> getClasses() {
        List<Class> classes = classDAO.findTop1000ByOrderByCreatedDateDesc();
        List<GetClassesResponse> res = new ArrayList<>();
        List<String> location = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        for (int i = 0; i < classes.size(); i++) {
            for (int j = 0; j < classes.get(i).getLocations().stream().toList().size(); j++) {
                location.add(classes.get(i).getLocations().stream().toList().get(j).getLocation());
            }
            GetClassesResponse c = GetClassesResponse.builder()
                    .classCode(classes.get(i).getClassId())
                    .className(classes.get(i).getClassName())
                    .fsu(classes.get(i).getFsu())
                    .location(location)
                    .createdOn(sdf.format(new Date(classes.get(i).getCreatedDate().getTime())))
                    .createdBy(classes.get(i).getCreatedBy())
                    .duration(classes.get(i).getDuration())
                    .status(classes.get(i).getStatus())
                    .build();
            res.add(c);
        }
        return res;
    }

    @Override
    public ResponseEntity<ResponseObject> getFilter() {
        try {
            filterResponses = classDAO.searchByFilter();
            return ResponseEntity.ok(new ResponseObject("Successful", "List of classroom", filterResponses));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Failed", "Couldn't found the list", e.getMessage()));
        }
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
            String review = userDAO.findByEmail(request.getReview()).get().getName();
            String approve = userDAO.findByEmail(request.getApprove()).get().getName();
            String timeFromStr = request.getClassTimeFrom().split(":").length == 3 ? request.getClassTimeFrom() : request.getClassTimeFrom() + ":00";
            String timeToStr = request.getClassTimeTo().split(":").length == 3 ? request.getClassTimeTo() : request.getClassTimeTo() + ":00";

            classInfo = Class.builder()
                    .className(request.getNameClass())
                    .classId(request.getClassCode())
                    .duration(request.getTotalTimeLearning())
                    .startDate(sdf.parse(request.getStartDate()))
                    .endDate(sdf.parse(request.getEndDate()))
                    .createdBy(user.getName())
                    .review(review)
                    .approve(approve)
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
            for(int i = 0; i < request.getAdmin().size(); i++){
                user = userDAO.findByEmail(request.getAdmin().get(i)).get();
                ClassUser classAdmin = ClassUser.builder()
                        .id(ClassUserCompositeKey.builder()
                                .userId(user.getUserId())
                                .classId(classInfo.getClassId())
                                .build())
                        .userID(user)
                        .classId(classInfo)
                        .userType(user.getRole().getRole().name())
                        .build();

                classUserList.add(classAdmin);
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
    public ResponseEntity<DeactivateClassResponse> deactivateClass(String classCode) {
        Class c = classDAO.findById(classCode).isPresent() ? classDAO.findById(classCode).get() : null;
        if (c != null) {
            c.setDeactivated(true);
            return ResponseEntity.status(200).body(new DeactivateClassResponse("successfully deactivate class with id " + classCode + " (⌐■_■)👍"));
        }
        return ResponseEntity.status(400).body(new DeactivateClassResponse("fail to deactivate class with id " + classCode + " (⌐■⌒■)👎"));
    }

    @Override
    public ResponseEntity<ClassDetailResponse> getClassDetail(String classCode) throws InterruptedException {
        Class c = classDAO.findById(classCode).isPresent() ? classDAO.findById(classCode).get() : null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Map<Integer, List<String>> trainerSyllabusMap = new HashMap<>();
        if (c != null) {
            try {
                log.info(c.getTrainingProgram().getTrainingProgramSyllabus().size());
                TrainingProgram tp = trainingProgramDAO.findById(c.getTrainingProgram().getTrainingProgramCode()).get();

                List<String> locationList = new ArrayList<>();
                List<Location> classLocations = c.getLocations().stream().toList();

                List<String> listDay = new ArrayList<>();
                List<ClassLearningDay> classLearningDays = c.getClassLearningDays().stream().toList();

                List<TrainerDTO> trainerList = new ArrayList<>();
                List<UserClassSyllabus> userClassSyllabuses = c.getUserClassSyllabus().stream().toList();

                List<UserDTO> attendeeList = new ArrayList<>();
                List<UserDTO> adminList = new ArrayList<>();
                List<ClassUser> classUsers = c.getClassUsers().stream().toList();

                List<SyllabusDTO> syllabusList = new ArrayList<>();
                List<TrainingProgramSyllabus> trainingProgramSyllabuses = tp.getTrainingProgramSyllabus().stream().toList();

                List<User> trainers = new ArrayList<>();

                for (int i = 0; i < classLocations.size(); i++) {
                    locationList.add(classLocations.get(i).getLocation());
                }
                for (int i = 0; i < classLearningDays.size(); i++) {
                    listDay.add(convertToMMDDYYYY(classLearningDays.get(i).getEnrollDate().toString().split(" ")[0]));
                }
                for (int i = 0; i < userClassSyllabuses.size(); i++) {
                    List<String> syllabusCodeList;
                    if(trainerSyllabusMap.containsKey(userClassSyllabuses.get(i).getUserId().getUserId())){
                        syllabusCodeList = trainerSyllabusMap.get(userClassSyllabuses.get(i).getUserId().getUserId());
                    }
                    else{
                        syllabusCodeList = new ArrayList<>();
                        trainers.add(userClassSyllabuses.get(i).getUserId());
                    }
                    syllabusCodeList.add(userClassSyllabuses.get(i).getTopicCode().getTopicCode());
                    trainerSyllabusMap.put(userClassSyllabuses.get(i).getUserId().getUserId(), syllabusCodeList);
                }
                for(int i = 0; i < trainers.size(); i++){
                    TrainerDTO trainer = TrainerDTO.builder()
                            .userId(trainers.get(i).getUserId())
                            .userName(trainers.get(i).getName())
                            .userEmail(trainers.get(i).getEmail())
                            .syllabusList(trainerSyllabusMap.get(trainers.get(i).getUserId()))
                            .build();
                    trainerList.add(trainer);
                }
                log.info(trainerSyllabusMap);
                for (int i = 0; i < classUsers.size(); i++) {
                    if(classUsers.get(i).getUserType().equalsIgnoreCase("user")){
                        UserDTO trainee = UserDTO.builder()
                                .userId(classUsers.get(i).getUserID().getUserId())
                                .userName(classUsers.get(i).getUserID().getName())
                                .userEmail(classUsers.get(i).getUserID().getEmail())
                                .build();
                        attendeeList.add(trainee);
                    }
                    else{
                        UserDTO admin = UserDTO.builder()
                                .userId(classUsers.get(i).getUserID().getUserId())
                                .userName(classUsers.get(i).getUserID().getName())
                                .userEmail(classUsers.get(i).getUserID().getEmail())
                                .build();
                        adminList.add(admin);
                    }
                }
                for (int i = 0; i < trainingProgramSyllabuses.size(); i++) {
                    SyllabusDTO syllabus = SyllabusDTO.builder()
                            .numberOfDay(trainingProgramSyllabuses.get(i).getTopicCode().getNumberOfDay())
                            .version(trainingProgramSyllabuses.get(i).getTopicCode().getVersion())
                            .createdDate(convertToMMDDYYYY(trainingProgramSyllabuses.get(i).getTopicCode().getCreatedDate().toString().split(" ")[0]))
                            .createdBy(trainingProgramSyllabuses.get(i).getTopicCode().getCreatedBy().getName())
                            .topicName(trainingProgramSyllabuses.get(i).getTopicCode().getTopicName())
                            .publishStatus(trainingProgramSyllabuses.get(i).getTopicCode().getPublishStatus())
                            .topicCode(trainingProgramSyllabuses.get(i).getTopicCode().getTopicCode())
                            .build();

                    syllabusList.add(syllabus);
                }

                log.info(c.getTimeFrom().toString());
                log.info(c.getTimeTo().toString());

                String timeFrom = c.getTimeFrom().toString();
                timeFrom = timeFrom.substring(0, timeFrom.lastIndexOf(":"));
                String timeTo = c.getTimeTo().toString();
                timeTo = timeTo.substring(0, timeTo.lastIndexOf(":"));


                ClassDetailResponse res = ClassDetailResponse.builder()
                        .classId(classCode)
                        .className(c.getClassName())
                        .createdBy(c.getCreatedBy())
                        .createdDate(convertToMMDDYYYY(c.getCreatedDate().toString().split(" ")[0]))
                        .deactivated(c.isDeactivated())
                        .duration(c.getDuration())
                        .endDate(convertToMMDDYYYY(c.getEndDate().toString().split(" ")[0]))
                        .modifiedBy(c.getModifiedBy())
                        .modifiedDate(c.getModifiedDate() != null ? convertToMMDDYYYY(c.getModifiedDate().toString().split(" ")[0]) : "")
                        .startDate(convertToMMDDYYYY(c.getStartDate().toString().split(" ")[0]))
                        .status(c.getStatus())
                        .timeFrom(timeFrom)
                        .timeTo(timeTo)
                        .approve(c.getApprove())
                        .review(c.getReview())
                        .fsu(c.getFsu())
                        .attendee(c.getAttendee())
                        .attendeeAccepted(Integer.toString(c.getAttendeeAccepted()))
                        .attendeePlanned(Integer.toString(c.getAttendeePlanned()))
                        .attendeeActual(Integer.toString(c.getAttendeeActual()))
                        .trainingProgram(TrainingProgramDTO.builder()
                                .trainingProgramCode(c.getTrainingProgram().getTrainingProgramCode())
                                .trainingProgramName(c.getTrainingProgram().getName())
                                .modifyBy(c.getTrainingProgram().getModifiedBy())
                                .modifyDate(c.getModifiedDate() != null ? convertToMMDDYYYY(c.getTrainingProgram().getModifiedDate().toString().split(" ")[0]) : "")
                                .duration(tp.getDuration())
                                .build())
                        .listDay(listDay)
                        .location(locationList)
                        .trainerList(trainerList)
                        .attendeeList(attendeeList)
                        .syllabusList(syllabusList)
                        .adminList(adminList)
                        .message("found class with id " + classCode)
                        .build();
                return ResponseEntity.status(200).body(res);
            } catch (Exception err) {
                err.printStackTrace();
                return ResponseEntity.status(500).body(null);
            }

        }
        return ResponseEntity.status(400).body(new ClassDetailResponse("class with id " + classCode + " not found"));
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
                return UpdateClassResponse.builder()
                        .status("Update Class failed")
                        .updatedClass(null)
                        .build();
            }
        } else {
            return UpdateClassResponse.builder().status("Class not found").updatedClass(null).build();
        }
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
    public Page<Class> getAllPagenation(Pageable pageable) {

        return classDAO.findAll(pageable);
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
        Date eDate = dateFormat.parse(enrollDate);
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

    @Override
    public UpdateClass3Response updateClass3(UpdateClass3Request updateClass3Request) {

        boolean status = updateClass3Request.isDeleted();
        String topicCode = updateClass3Request.getTopicCode();
        int trainingProgramCode = updateClass3Request.getTrainingProgramCode();

        var syllabus = syllabusDAO.findById(topicCode).orElse(null);
        var trainingProgram = trainingProgramDAO.findById(trainingProgramCode).orElse(null);

        TrainingProgramSyllabus trainingProgramSyllabus = trainingProgramSyllabusDAO.findByIdTopicCodeAndIdTrainingProgramCode(topicCode,trainingProgramCode);
//        TrainingProgramSyllabus topicCode1 = trainingProgramSyllabusDAO.findByIdTopicCode(topicCode);
//        TrainingProgramSyllabus trainingProgram1= trainingProgramSyllabusDAO.findByIdTrainingProgramCode(trainingProgramCode);


        if(trainingProgramSyllabus != null){

//            trainingProgramSyllabus.setDeleted(status);
            trainingProgramSyllabus = trainingProgramSyllabusDAO.save(trainingProgramSyllabus);

            if(trainingProgramSyllabus != null){
                return UpdateClass3Response.builder()
                        .status("Update TrainingProgramSyllabus successful")
                        .updatedClass3(trainingProgramSyllabus)
                        .build();
            }else {
                return UpdateClass3Response.builder()
                        .status("Update TrainingProgramSyllabus failed")
                        .updatedClass3(null)
                        .build();
            }

        }else {
            TrainingProgramSyllabus trainingProgramSyllabus2 =
                    TrainingProgramSyllabus.builder()
                            .id(
                                    SyllabusTrainingProgramCompositeKey.builder()
                                            .topicCode(topicCode)
                                            .trainingProgramCode(trainingProgramCode)
                                            .build())
                            .topicCode(syllabus)
                            .trainingProgramCode(trainingProgram)
//                            .deleted(true)
                            .build();
            trainingProgramSyllabus2 =trainingProgramSyllabusDAO.save(trainingProgramSyllabus2);

            if(trainingProgramSyllabus2 != null){
                return UpdateClass3Response.builder()
                        .status("Update TrainingProgramSyllabus successful")
                        .updatedClass3(trainingProgramSyllabus2)
                        .build();

            }else {
                return UpdateClass3Response.builder()
                        .status("Update TrainingProgramSyllabus failed")
                        .updatedClass3(null)
                        .build();
            }



        }



    }

    @Override
    public ResponseEntity<ResponseObject> deleteAllTrainingProgramSyllabus() {
        try {
            trainingProgramSyllabusDAO.deleteAll(); // Assuming your DAO has a deleteAll method
            return new ResponseEntity<>(new ResponseObject("Success", "All TrainingProgramSyllabus have been deleted successfully.", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject("Error", "Failed to delete TrainingProgramSyllabus.", null), HttpStatus.INTERNAL_SERVER_ERROR);
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


//    public List<SyllabusDTO> getAllSyllabusInTrainingProgram(
//            List<TrainingProgramSyllabus> trainingProgramSyllabusList
//    ) throws InterruptedException {
//        List<SyllabusDTO> syllabusList = new ArrayList<>();
//        int numThreads = calculateNumThreads(trainingProgramSyllabusList.size());
//
//        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
//
//        int batchSize = (int) Math.ceil((double) trainingProgramSyllabusList.size() / numThreads);
//        int fromIndex = 0;
//
//        for (int i = 0; i < numThreads; i++) {
//            int toIndex = Math.min(fromIndex + batchSize, trainingProgramSyllabusList.size());
//
//            List<TrainingProgramSyllabus> tps = trainingProgramSyllabusList.subList(fromIndex, toIndex);
//
//            executorService.submit(() -> {
//                for (int j = 0; j < tps.size(); j++) {
//                    Syllabus syllabus = tps.get(j).getTopicCode();
//                    SyllabusDTO syllabusDTO = SyllabusDTO.builder()
//                            .topicCode(syllabus.getTopicCode())
//                            .numberOfDay(syllabus.getNumberOfDay())
//                            .version(syllabus.getVersion())
//                            .publishStatus(syllabus.getPublishStatus())
//                            .topicName(syllabus.getTopicName())
//                            .createdBy(syllabus.getCreatedBy().getName())
//                            .createdDate(syllabus.getCreatedDate())
//                            .build();
//                    syllabusList.add(syllabusDTO);
//                }
//            });
//
//            fromIndex = toIndex;
//
//        }
//
//        executorService.shutdown();
//        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
//
//        return syllabusList;
//    }

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

    public String convertToMMDDYYYY(String dateStr){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = date.format(outputFormatter);
        return formattedDate;
    }


}
