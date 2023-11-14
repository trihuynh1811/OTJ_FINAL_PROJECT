package com.example.FAMS.service_implementors;

import com.amazonaws.HttpMethod;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.StandardOutputDTO;
import com.example.FAMS.dto.responses.Syllabus.GetSyllabusByPage;
import com.example.FAMS.dto.responses.Syllabus.GetAllSyllabusResponse;
import com.example.FAMS.dto.responses.Syllabus.PresignedUrlResponse;
import com.example.FAMS.models.*;
import com.example.FAMS.models.composite_key.SyllabusStandardOutputCompositeKey;
import com.example.FAMS.models.composite_key.SyllabusTrainingUnitCompositeKey;
import com.example.FAMS.repositories.*;
import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.services.FileService;
import com.example.FAMS.services.SyllabusService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.base.Strings;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.*;

@Service
@Log4j2
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    @Autowired
    TrainingUnitDAO trainingUnitDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    StandardOutputDAO standardOutputDAO;

    @Autowired
    TrainingContentDAO trainingContentDAO;

    @Autowired
    LearningObjectiveDAO learningObjectiveDAO;

    @Autowired
    SyllabusObjectiveDAO syllabusObjectiveDAO;

    @Autowired
    UserClassSyllabusDAO userSyllabusDAO;

    @Autowired
    FileService fileService;

    String line = "";

    @Autowired
    TrainingMaterialDAO trainingMaterialDAO;

    @Override
    public List<GetAllSyllabusResponse> getSyllabuses() {
//        log.info(userDAO.findAll());
//        log.info(trainingUnitDAO.countDayNumberBySyllabus_TopicCode("lmao"));
        List<Syllabus> syllabusList = syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
        List<GetAllSyllabusResponse> syllabuses = new ArrayList<>();
        List<String> objectiveList = null;
        List<SyllabusObjective> syllabusObjectiveList = null;

        for (int i = 0; i < syllabusList.size(); i++) {
            objectiveList = new ArrayList<>();
            syllabusObjectiveList = syllabusList.get(i).getSyllabusObjectives().stream().toList();
            for (int j = 0; j < syllabusObjectiveList.size(); j++) {
                objectiveList.add(syllabusObjectiveList.get(j).getOutputCode().getOutputCode());
            }
            GetAllSyllabusResponse res = GetAllSyllabusResponse.builder()
                    .syllabusName(syllabusList.get(i).getTopicName())
                    .syllabusCode(syllabusList.get(i).getTopicCode())
                    .createdOn(syllabusList.get(i).getCreatedDate().getTime())
                    .createdBy(syllabusList.get(i).getCreatedBy().getName())
                    .duration(syllabusList.get(i).getNumberOfDay())
                    .status(syllabusList.get(i).getPublishStatus())
                    .syllabusObjectiveList(objectiveList)
                    .build();

            syllabuses.add(res);
        }

        return syllabuses;
    }

    @Override
    public GetSyllabusByPage paging(int amount, int pageNumber) {
        try {
            List<Syllabus> syllabusList = syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
            List<GetAllSyllabusResponse> syllabuses = new ArrayList<>();
            List<String> objectiveList = null;
            List<SyllabusObjective> syllabusObjectiveList = null;
            int totalNumberOfPages = syllabusList.size() == amount ? 1 : syllabusList.size() % amount == 0 ? syllabusList.size() / amount : (syllabusList.size() / amount) + 1;
            log.info(syllabusList.size() / amount);
            if (pageNumber < 0 || pageNumber > totalNumberOfPages) {
                return GetSyllabusByPage.builder()
                        .message("found 0 result.")
                        .totalNumberOfPages(totalNumberOfPages)
                        .status(1)
                        .pageNumber(pageNumber)
                        .syllabusList(syllabuses)
                        .build();
            }

            if (amount > syllabusList.size()) {
                for (int i = 0; i < syllabusList.size(); i++) {
                    objectiveList = new ArrayList<>();
                    syllabusObjectiveList = syllabusList.get(i).getSyllabusObjectives().stream().toList();
                    for (int j = 0; j < syllabusObjectiveList.size(); j++) {
                        objectiveList.add(syllabusObjectiveList.get(j).getOutputCode().getOutputCode());
                    }
                    GetAllSyllabusResponse res = GetAllSyllabusResponse.builder()
                            .syllabusName(syllabusList.get(i).getTopicName())
                            .syllabusCode(syllabusList.get(i).getTopicCode())
                            .createdOn(syllabusList.get(i).getCreatedDate().getTime())
                            .createdBy(syllabusList.get(i).getCreatedBy().getName())
                            .duration(syllabusList.get(i).getNumberOfDay())
                            .status(syllabusList.get(i).getPublishStatus())
                            .syllabusObjectiveList(objectiveList)
                            .build();

                    syllabuses.add(res);
                }

                return GetSyllabusByPage.builder()
                        .message("found " + syllabuses.size() + " result.")
                        .totalNumberOfPages(1)
                        .status(0)
                        .pageNumber(1)
                        .syllabusList(syllabuses)
                        .build();

            }
            int maxContent = pageNumber * amount;
            int pageTo = Math.min(maxContent, syllabusList.size());
            int pageFrom = pageNumber * amount > syllabusList.size() ? maxContent - amount : pageTo - amount;
            log.info(maxContent);
            log.info("syllabus size: " + syllabusList.size());
            log.info(pageNumber * amount > syllabusList.size());
            log.info(pageTo - ((pageNumber * amount) - syllabusList.size()));
            log.info("page from: " + pageFrom);
            log.info("page to: " + pageTo);
            List<Syllabus> syllabusSubList = syllabusList.subList(pageFrom, pageTo);

            for (int i = 0; i < syllabusSubList.size(); i++) {
                objectiveList = new ArrayList<>();
                syllabusObjectiveList = syllabusSubList.get(i).getSyllabusObjectives().stream().toList();
                for (int j = 0; j < syllabusObjectiveList.size(); j++) {
                    objectiveList.add(syllabusObjectiveList.get(j).getOutputCode().getOutputCode());
                }
                GetAllSyllabusResponse res = GetAllSyllabusResponse.builder()
                        .syllabusName(syllabusSubList.get(i).getTopicName())
                        .syllabusCode(syllabusSubList.get(i).getTopicCode())
                        .createdOn(syllabusSubList.get(i).getCreatedDate().getTime())
                        .createdBy(syllabusSubList.get(i).getCreatedBy().getName())
                        .duration(syllabusSubList.get(i).getNumberOfDay())
                        .status(syllabusSubList.get(i).getPublishStatus())
                        .syllabusObjectiveList(objectiveList)
                        .build();

                syllabuses.add(res);
            }
            return GetSyllabusByPage.builder()
                    .message("found " + syllabuses.size() + " result.")
                    .totalNumberOfPages(totalNumberOfPages)
                    .status(0)
                    .pageNumber(pageNumber)
                    .syllabusList(syllabuses)
                    .build();

        } catch (Exception err) {
            err.printStackTrace();
            return GetSyllabusByPage.builder()
                    .message("found 0 result.")
                    .totalNumberOfPages(0)
                    .status(-1)
                    .pageNumber(pageNumber)
                    .syllabusList(null)
                    .build();
        }
    }

    @Override
    public Syllabus getDetailSyllabus(String topicCode) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        return optionalSyllabus.orElse(null);
    }


    @Override
    public int createSyllabusGeneral(CreateSyllabusGeneralRequest request, Authentication authentication) {
        if (syllabusDAO.findById(request.getTopicCode()).isPresent() && !request.isUpdated()) {
            return 1;
        }
        try {
            Syllabus syllabus = null;
            if (request.isUpdated()) {
                syllabus = syllabusDAO.findById(request.getTopicCode()).get();

                syllabus.setTopicName(request.getTopicName());
                syllabus.setModifiedDate(new Date());
                syllabus.setModifiedBy(getCreator(authentication).getName());
                syllabus.setPriority(request.getPriority());
                syllabus.setCourseObjective(request.getCourseObjective());
                syllabus.setTechnicalGroup(request.getTechnicalRequirement());
                syllabus.setTrainingAudience(request.getTrainingAudience());
                syllabus.setVersion(request.getVersion());
                syllabus.setPublishStatus(request.getPublishStatus());

                syllabusDAO.save(syllabus);

                return 2;
            }

            User user = getCreator(authentication);
            syllabus = Syllabus.builder()
                    .topicCode(request.getTopicCode())
                    .topicName(request.getTopicName())
                    .trainingAudience(request.getTrainingAudience())
                    .courseObjective(request.getCourseObjective())
                    .technicalGroup(request.getTechnicalRequirement())
                    .publishStatus(request.getPublishStatus())
                    .priority(request.getPriority())
                    .version(request.getVersion())
                    .createdBy(user)
                    .createdDate(new Date())
                    .modifiedDate(new Date())
                    .modifiedBy(getCreator(authentication).getName())
                    .build();

            syllabusDAO.save(syllabus);

            return 0;
        } catch (Exception err) {
            err.printStackTrace();
            return -1;
        }

    }

    @Override
    public void createSyllabusOutline(CreateSyllabusOutlineRequest request, Authentication authentication) {
        int unitBatchSize = 10;
        int contentBatchSize = 10;
        List<TrainingUnit> unitList = new ArrayList<>();
        List<TrainingContent> contentList = new ArrayList<>();
        List<TrainingContent> savedContentList;
        List<SyllabusObjective> syllabusObjectiveList = new ArrayList<>();
        LinkedHashMap<Integer, List<StandardOutputDTO>> learningObjectiveMap = new LinkedHashMap<>();
        Map<String, String> syllabusObjectiveMap = new HashMap<>();
        List<LearningObjective> learningObjectiveList = new ArrayList<>();

        Syllabus syllabus = syllabusDAO.findById(request.getTopicCode()).get();
        StandardOutput standardOutput = standardOutputDAO.findById("H4SD").get();

        for (int i = 0; i < request.getSyllabus().size(); i++) {
            for (int j = 0; j < request.getSyllabus().get(i).getUnits().size(); j++) {
                TrainingUnit trainingUnit = TrainingUnit.builder()
                        .unitName(request.getSyllabus().get(i).getUnits().get(j).getUnitName())
                        .dayNumber(request.getSyllabus().get(i).getDayNumber())
                        .id(SyllabusTrainingUnitCompositeKey.builder()
                                .uCode(request.getSyllabus().get(i).getUnits().get(j).getUnitCode())
                                .tCode(request.getTopicCode())
                                .build())
                        .syllabus(syllabus)
                        .build();

                unitList.add(trainingUnit);
                for (int z = 0; z < request.getSyllabus().get(i).getUnits().get(j).getContents().size(); z++) {
                    List<StandardOutputDTO> standardOutputDTO = request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getStandardOutput();


                    TrainingContent trainingContent = TrainingContent.builder()
                            .unitCode(trainingUnit)
                            .deliveryType(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDeliveryType())
                            .note(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getNote())
                            .content_name(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContent())
                            .trainingFormat(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).isOnline())
                            .duration(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDuration())
                            .build();

                    learningObjectiveMap.put(contentList.size(), standardOutputDTO);
                    contentList.add(trainingContent);
                }


                if (contentList.size() >= contentBatchSize && unitList.size() >= unitBatchSize) {
                    log.info("content list size: " + contentList.size());
                    trainingUnitDAO.saveAll(unitList);
                    savedContentList = trainingContentDAO.saveAll(contentList);
                    contentList.clear();
                    unitList.clear();
                    for (Map.Entry<Integer, List<StandardOutputDTO>> entry : learningObjectiveMap.entrySet()) {
                        log.info(entry.getKey());
                        log.info(entry.getValue().size());
                        for (int k = 0; k < entry.getValue().size(); k++) {
                            if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue().get(k).getOutputCode())) {
                                standardOutput = standardOutputDAO.findById(entry.getValue().get(k).getOutputCode().toUpperCase()).get();
                            }

                            if (!syllabusObjectiveMap.containsKey(entry.getValue().get(k).getOutputCode())) {
                                SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                        .id(SyllabusStandardOutputCompositeKey.builder()
                                                .outputCode(entry.getValue().get(k).getOutputCode())
                                                .topicCode(syllabus.getTopicCode())
                                                .build())
                                        .outputCode(standardOutput)
                                        .topicCode(syllabus)
                                        .build();
                                syllabusObjectiveList.add(syllabusObjective);
                                syllabusObjectiveMap.put(entry.getValue().get(k).getOutputCode(), syllabus.getTopicCode());
                            }
                            log.info(standardOutput.getOutputCode());
                            LearningObjective learningObjective = LearningObjective.builder()
                                    .contentCode(savedContentList.get(entry.getKey()))
                                    .outputCode(standardOutput)
                                    .description(standardOutput.getDescription())
                                    .name(standardOutput.getOutputName())
                                    .type("fpt dogshit")
                                    .build();
                            learningObjectiveList.add(learningObjective);
                        }
                        log.info(learningObjectiveList);
                        log.info(savedContentList.get(entry.getKey()));
                        learningObjectiveDAO.saveAll(learningObjectiveList);
                        learningObjectiveList.clear();
                        learningObjectiveList = new ArrayList<>();
                    }


                    learningObjectiveMap.clear();
                }

            }

        }

        log.info("saved all training unit");

        if (!unitList.isEmpty()) {
            trainingUnitDAO.saveAll(unitList);
            unitList.clear();
        }

        if (!contentList.isEmpty()) {
            savedContentList = trainingContentDAO.saveAll(contentList);
            for (Map.Entry<Integer, List<StandardOutputDTO>> entry : learningObjectiveMap.entrySet()) {
                log.info(entry.getKey());
                log.info(entry.getValue().size());
                for (int k = 0; k < entry.getValue().size(); k++) {
                    if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue().get(k).getOutputCode())) {
                        standardOutput = standardOutputDAO.findById(entry.getValue().get(k).getOutputCode().toUpperCase()).get();
                    }

                    if (!syllabusObjectiveMap.containsKey(entry.getValue().get(k).getOutputCode())) {
                        SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                .id(SyllabusStandardOutputCompositeKey.builder()
                                        .outputCode(entry.getValue().get(k).getOutputCode())
                                        .topicCode(syllabus.getTopicCode())
                                        .build())
                                .outputCode(standardOutput)
                                .topicCode(syllabus)
                                .build();
                        syllabusObjectiveList.add(syllabusObjective);
                        syllabusObjectiveMap.put(entry.getValue().get(k).getOutputCode(), syllabus.getTopicCode());
                    }
                    log.info(standardOutput.getOutputCode());
                    LearningObjective learningObjective = LearningObjective.builder()
                            .contentCode(savedContentList.get(entry.getKey()))
                            .outputCode(standardOutput)
                            .description(standardOutput.getDescription())
                            .name(standardOutput.getOutputName())
                            .type("fpt dogshit")
                            .build();
                    learningObjectiveList.add(learningObjective);
                }
                log.info(learningObjectiveList);
                log.info(savedContentList.get(entry.getKey()));

            }
            learningObjectiveDAO.saveAll(learningObjectiveList);
            learningObjectiveList.clear();
            learningObjectiveList = new ArrayList<>();
        }
        log.info(syllabusObjectiveMap);
        syllabusObjectiveDAO.saveAll(syllabusObjectiveList);

        int numberOfDay = trainingUnitDAO.countDayNumberBySyllabus_TopicCode(syllabus.getTopicCode());

        syllabus.setNumberOfDay(numberOfDay);
        syllabusDAO.save(syllabus);
//        for(Map.Entry<String, String> entry: syllabusObjectiveMap.entrySet()){
//            LearningObjective learningObjective = learningObjectiveDAO.findById(entry.getKey()).get();
//            syllabus.getLearningObjectives().add(learningObjective);
////            learningObjective.getSyllabus().add(syllabus);
//
//            syllabusDAO.save(syllabus);
//            learningObjectiveList.add(learningObjective);
//        }
//        learningObjectiveDAO.saveAll(learningObjectiveList);

    }

    @Override
    public int createSyllabusOther(CreateSyllabusGeneralRequest request) {

        try {
            Syllabus syllabus = syllabusDAO.findById(request.getTopicCode()).get();

            syllabus.setTrainingPrinciples(request.getTrainingPrinciple());
            syllabusDAO.save(syllabus);
            return 0;
        } catch (Exception err) {
            err.printStackTrace();
            return -1;
        }

    }

    @Override
    public UpdateSyllabusResponse updateSyllabus(UpdateSyllabusRequest updatesyllabusRequest, String topicCode) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        User user = userDAO.findByEmail(updatesyllabusRequest.getCreatedBy()).get();
        Syllabus syllabusexits = optionalSyllabus.orElse(null);
        if (syllabusexits != null) {
            syllabusexits.setTopicName(updatesyllabusRequest.getTopicName());
            syllabusexits.setTechnicalGroup(updatesyllabusRequest.getTechnicalGroup());
            syllabusexits.setVersion(updatesyllabusRequest.getVersion());
            syllabusexits.setTrainingAudience(updatesyllabusRequest.getTrainingAudience());
            syllabusexits.setTopicOutline(updatesyllabusRequest.getTopicOutline());
//            syllabusexits.setTrainingMaterials(updatesyllabusRequest.getTrainingMaterials());
            syllabusexits.setPriority(updatesyllabusRequest.getPriority());
            syllabusexits.setPublishStatus(updatesyllabusRequest.getPublishStatus());
            syllabusexits.setCreatedBy(user);
            syllabusexits.setCreatedDate(updatesyllabusRequest.getCreatedDate());
            syllabusexits.setModifiedBy(updatesyllabusRequest.getModifiedBy());
            syllabusexits.setModifiedDate(updatesyllabusRequest.getModifiedDate());


            Syllabus syllabusUpdate = syllabusDAO.save(syllabusexits);

            if (syllabusUpdate != null) {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus successful")
                        .updateSyllabus(syllabusUpdate)
                        .build();


            } else {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus failed")
                        .updateSyllabus(null)
                        .build();

            }


        } else {
            return UpdateSyllabusResponse.builder()
                    .status("Syllabus not found")
                    .updateSyllabus(null)
                    .build();

        }

    }

    @Override
    public Syllabus getSyllabusById(String topicCode) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        return optionalSyllabus.orElse(null);
    }

    @Override
    public List<Syllabus> processDataFromCSV(MultipartFile file, String choice, Authentication authentication) throws IOException {

        List<Syllabus> syllabusList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            // Mảng các đối tượng SimpleDateFormat với các định dạng khác nhau
            SimpleDateFormat[] dateFormats = {
                    new SimpleDateFormat("dd/MM/yyyy"),
                    new SimpleDateFormat("dd-MM-yyyy")
            };

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Đây là dòng đầu tiên, bỏ qua nó
                    continue;
                }
                String[] data = line.split(",");
                Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(data[0]);
                Syllabus syllabusexits = optionalSyllabus.orElse(null);
                if (choice.equals("Replace")) {

                    if (syllabusexits != null) {
                        syllabusexits.setCreatedBy(getCreator(authentication));
                        // Chuyển đổi từ chuỗi ngày thành Date và chỉ lấy phần ngày
                        syllabusexits.setCreatedDate(parseDate(data[1], dateFormats));
                        syllabusexits.setModifiedBy(getCreator(authentication).getName());
                        syllabusexits.setModifiedDate(parseDate(data[2], dateFormats));
                        syllabusexits.setPriority(data[3]);
                        syllabusexits.setPublishStatus(data[4]);
                        syllabusexits.setTechnicalGroup(data[5]);
                        syllabusexits.setTopicName(data[6]);
                        syllabusexits.setTopicOutline(data[7]);
                        syllabusexits.setTrainingAudience(Integer.parseInt(data[8]));
//                        syllabusexits.setTrainingMaterials(data[9]);
                        syllabusexits.setTrainingPrinciples(data[9]);
                        syllabusexits.setVersion(data[10]);
                        syllabusexits.setCourseObjective(data[11]);
//                        syllabusexits.setUserID(getCreator(authentication));
                        syllabusDAO.save(syllabusexits);
                    } else {
                        Syllabus c = new Syllabus();

                        c.setTopicCode(data[0]);
                        c.setCreatedBy(getCreator(authentication));
                        // Chuyển đổi từ chuỗi ngày thành Date và chỉ lấy phần ngày
                        c.setCreatedDate(parseDate(data[1], dateFormats));
                        c.setModifiedBy(getCreator(authentication).getName());
                        c.setModifiedDate(parseDate(data[2], dateFormats));
                        c.setPriority(data[3]);
                        c.setPublishStatus(data[4]);
                        c.setTechnicalGroup(data[5]);
                        c.setTopicName(data[6]);
                        c.setTopicOutline(data[7]);
                        c.setTrainingAudience(Integer.parseInt(data[8]));
//                        c.setTrainingMaterials(data[9]);
                        c.setTrainingPrinciples(data[9]);
                        c.setVersion(data[10]);
                        c.setCourseObjective(data[11]);
//                        c.setUserID(getCreator(authentication));
                        syllabusList.add(c);
                        syllabusDAO.saveAll(syllabusList);
                    }
                } else if (choice.equals("Skip")) {
                    if (syllabusexits != null) {

                    } else {
                        Syllabus c = new Syllabus();

                        c.setTopicCode(data[0]);
                        c.setCreatedBy(getCreator(authentication));
                        // Chuyển đổi từ chuỗi ngày thành Date và chỉ lấy phần ngày
                        c.setCreatedDate(parseDate(data[1], dateFormats));
                        c.setModifiedBy(getCreator(authentication).getName());
                        c.setModifiedDate(parseDate(data[2], dateFormats));
                        c.setPriority(data[3]);
                        c.setPublishStatus(data[4]);
                        c.setTechnicalGroup(data[5]);
                        c.setTopicName(data[6]);
                        c.setTopicOutline(data[7]);
                        c.setTrainingAudience(Integer.parseInt(data[8]));
//                        c.setTrainingMaterials(data[9]);
                        c.setTrainingPrinciples(data[9]);
                        c.setVersion(data[10]);
                        c.setCourseObjective(data[11]);
//                        c.setUserID(getCreator(authentication));
                        syllabusList.add(c);
                        syllabusDAO.saveAll(syllabusList);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return syllabusList;
    }

    private java.sql.Date parseDate(String dateString, SimpleDateFormat[] dateFormats) throws ParseException {
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                Date parsedDate = dateFormat.parse(dateString);
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                // Nếu không thành công, thử định dạng khác
                log.info(dateFormat.toPattern() + " not equal " + dateString);
            }
        }
        throw new ParseException("Không thể chuyển đổi ngày", 0);
    }


    public void downloadCSV() throws IOException {
        String computerAccountName = System.getProperty("user.name");

        File csvFile = new File("C:/Users/" + computerAccountName + "/Downloads/Template.csv");

        // Sử dụng FileWriter để ghi vào tệp CSV
        FileWriter fileWriter = new FileWriter(csvFile, false); // Use false to overwrite the file

        // Sử dụng BufferedWriter để ghi dữ liệu vào tệp CSV
        BufferedWriter out = new BufferedWriter(fileWriter);

        // Thêm nội dung vào tệp
        out.write("topic_code, created_date, Modified Date, priority, publishStatus, technicalGroup, topic_name, topicOutline, TrainingAudience, TrainingPrinciples, Version, CourseObjective");
        out.newLine(); // Xuống dòng
        // Đóng BufferedWriter
        out.close();
    }

    @Override
    public Syllabus duplicateSyllabus(String topicCode, Authentication authentication) {
        int tuanSoiMapDit = syllabusDAO.countByTopicCodeLike(topicCode + "%");
        Syllabus originalSyllabus = syllabusDAO.findById(topicCode).get();

        topicCode = originalSyllabus.getTopicCode();
        String topicCodeClone = topicCode + "_" + tuanSoiMapDit;


        Syllabus duplicatedSyllabus = Syllabus.builder()
                .topicName(originalSyllabus.getTopicName())
                .trainingPrinciples(originalSyllabus.getTrainingPrinciples())
                .version(originalSyllabus.getVersion())
                .technicalGroup(originalSyllabus.getTechnicalGroup())
                .trainingAudience(originalSyllabus.getTrainingAudience())
                .topicOutline(originalSyllabus.getTopicOutline())
                .trainingMaterials(originalSyllabus.getTrainingMaterials())
                .priority(originalSyllabus.getPriority())
                .publishStatus(originalSyllabus.getPublishStatus())
                .createdBy(originalSyllabus.getCreatedBy())
                .createdDate(new Date())
                .modifiedBy(originalSyllabus.getModifiedBy())
                .modifiedDate(new Date())
                .courseObjective(originalSyllabus.getCourseObjective())
                .topicCode(topicCodeClone)
                .build();

        syllabusDAO.save(duplicatedSyllabus);

        return duplicatedSyllabus;
    }

    public Syllabus saveSyllabus(Syllabus syllabus) {
        return syllabusDAO.save(syllabus);
    }

    public List<Syllabus> searchSyllabus(String createdDate, String searchValue) {
        List<Syllabus> syllabusList = syllabusDAO.findAll();
        if (!Strings.isNullOrEmpty(createdDate)) {
            syllabusList = syllabusList.stream().filter(n -> {
                return new SimpleDateFormat("yyyy-MM-dd").format(n.getCreatedDate()).equals(createdDate);
            }).collect(Collectors.toList());
        }
        if (!Strings.isNullOrEmpty(searchValue)) {
            syllabusList = syllabusList.stream().filter(n -> n.getTopicName().trim().toLowerCase().contains(searchValue.trim().toLowerCase())
                    || n.getTopicCode().trim().toLowerCase().contains(searchValue.trim().toLowerCase())).collect(Collectors.toList());
        }
        return syllabusList;
    }

    public User getCreator(Authentication authentication) {
        Object creator = authentication.getPrincipal();
        if (creator instanceof User) {
            return (User) creator;
        }
        return null;
    }

    public PresignedUrlResponse generatePresignedUrl(List<String> files) {
        List<String> putPresignedUrlList = new ArrayList<>();
        List<String> getPresignedUrlList = new ArrayList<>();
        try {
            for (int i = 0; i < files.size(); i++) {
                String putPresignedUrl = fileService.generateUrl(files.get(i), HttpMethod.PUT);
                String getPresignedUrl = fileService.generateUrl(files.get(i), HttpMethod.GET);
                putPresignedUrlList.add(putPresignedUrl);
                getPresignedUrlList.add(getPresignedUrl);
            }
            return PresignedUrlResponse.builder()
                    .putPresignedUrl(putPresignedUrlList)
                    .getPresignedUrl(getPresignedUrlList)
                    .message("successfully generate presigned url.")
                    .status(0)
                    .build();
        } catch (Exception err) {
            err.printStackTrace();
            return PresignedUrlResponse.builder()
                    .putPresignedUrl(putPresignedUrlList)
                    .getPresignedUrl(getPresignedUrlList)
                    .message("fail to generate presigned url.")
                    .status(-1)
                    .build();
        }

    }

}

