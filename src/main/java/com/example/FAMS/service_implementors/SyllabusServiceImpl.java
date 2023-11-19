package com.example.FAMS.service_implementors;

import com.amazonaws.HttpMethod;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.StandardOutputDTO;
import com.example.FAMS.dto.responses.Syllabus.*;
import com.example.FAMS.dto.requests.SyllbusRequest.*;
import com.example.FAMS.models.*;
import com.example.FAMS.models.composite_key.SyllabusStandardOutputCompositeKey;
import com.example.FAMS.models.composite_key.SyllabusTrainingUnitCompositeKey;
import com.example.FAMS.models.composite_key.SyllabusTrainingUnitTrainingContentCompositeKey;
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

import javax.annotation.Nullable;
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

//    @Autowired
//    LearningObjectiveDAO learningObjectiveDAO;

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
    public List<GetAllSyllabusResponse> getSyllabuses(@Nullable String type) {
        List<Syllabus> syllabusList = type.equalsIgnoreCase("All") ?
                syllabusDAO.findTop1000ByOrderByCreatedDateDesc() : syllabusDAO.findTop1000ByDeletedOrderByCreatedDateDesc(false);
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
            List<Syllabus> syllabusList = syllabusDAO.findTop1000ByDeletedOrderByCreatedDateDesc(false);
            System.out.println(syllabusList);
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
        Syllabus syllabus = syllabusDAO.findById(topicCode).get();

        syllabus.getTu().stream().toList();
        for(int i = 0; i < syllabus.getTu().stream().toList().size(); i++){
            syllabus.getTu().stream().toList().get(i).getTrainingContents();
            log.info(syllabus);
        }

//        syllabus.getTrainingMaterials().stream().toList().get(0).getMaterial();


        return syllabus;
    }




    @Override
    public CreateSyllabusResponse createSyllabus(CreateSyllabusOutlineRequest request) {

        if (syllabusDAO.findById(request.getTopicCode()).isPresent()) {
            return CreateSyllabusResponse.builder()
                    .status(1)
                    .message("syllabus with id " + request.getTopicCode() + " already existed.")
                    .url(null)
                    .build();
        }
        int unitBatchSize = 10;
        int contentBatchSize = 10;
        String putPresignedUrl = "";
        String getPresignedUrl = "";
        List<TrainingUnit> unitList = new ArrayList<>();
        List<TrainingContent> contentList = new ArrayList<>();
        List<TrainingContent> savedContentList;
        List<SyllabusObjective> syllabusObjectiveList = new ArrayList<>();
        List<TrainingMaterial> trainingMaterialList = new ArrayList<>();
        LinkedHashMap<Integer, String> learningObjectiveMap = new LinkedHashMap<>();
        List<PresignedUrlResponse> presignedUrlResponseList = new ArrayList<>();
        Map<String, String> syllabusObjectiveMap = new HashMap<>();
//        List<LearningObjective> learningObjectiveList = new ArrayList<>();

        User creator = userDAO.findByEmail(request.getCreatorEmail()).get();
        try {
            Syllabus syllabus = Syllabus.builder()
                    .topicCode(request.getTopicCode())
                    .topicName(request.getTopicName())
                    .trainingAudience(Integer.parseInt(request.getTrainingAudience()))
                    .courseObjective(request.getCourseObjective())
                    .technicalGroup(request.getTechnicalRequirement())
                    .publishStatus(request.getPublishStatus())
                    .priority(request.getPriority())
                    .version(request.getVersion())
                    .createdBy(creator)
                    .createdDate(new Date())
                    .trainingPrinciples(request.getTrainingPrinciple())
                    .modifiedDate(new Date())
                    .modifiedBy(creator.getEmail())
                    .build();

            syllabusDAO.save(syllabus);

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
                        StandardOutput outputCode = standardOutputDAO.findById(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getStandardOutput()).get();


                        TrainingContent trainingContent = TrainingContent.builder()
                                .id(SyllabusTrainingUnitTrainingContentCompositeKey.builder()
                                        .contentCode(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContentId())
                                        .id(SyllabusTrainingUnitCompositeKey.builder()
                                                .tCode(request.getTopicCode())
                                                .uCode(trainingUnit.getId().getUCode())
                                                .build())
                                        .build())
                                .unitCode(trainingUnit)
                                .deliveryType(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDeliveryType())
                                .note(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getNote())
                                .content_name(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContent())
                                .trainingFormat(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).isOnline())
                                .duration(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDuration())
                                .outputCode(outputCode)
                                .build();

                        learningObjectiveMap.put(contentList.size(), request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getStandardOutput());
                        contentList.add(trainingContent);

                        List<String> putPresignedUrlList = new ArrayList<>();
                        List<String> getPresginedUrlList = new ArrayList<>();
                        for (int x = 0; x < request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().size(); x++) {

                            putPresignedUrl = fileService.generateUrl(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x) + "_"
                                    + syllabus.getTopicCode() + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode(), HttpMethod.PUT);
                            getPresignedUrl = fileService.generateUrl(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x) + "_"
                                    + syllabus.getTopicCode() + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode(), HttpMethod.GET);

                            TrainingMaterial trainingMaterial = TrainingMaterial.builder()
                                    .material(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x))
                                    .source(getPresignedUrl)
                                    .trainingContent(trainingContent)
//                                    .syllabus(syllabus)
                                    .build();

                            trainingMaterialList.add(trainingMaterial);
                            getPresginedUrlList.add(getPresignedUrl);
                            putPresignedUrlList.add(putPresignedUrl);
                        }
                        PresignedUrlResponse res = PresignedUrlResponse.builder()
                                .getPresignedUrl(getPresginedUrlList)
                                .putPresignedUrl(putPresignedUrlList)
                                .id("c" + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode())
                                .build();

                        log.info(res);

                        presignedUrlResponseList.add(res);
                    }


                    if (contentList.size() >= contentBatchSize && unitList.size() >= unitBatchSize) {
                        log.info("content list size: " + contentList.size());
                        trainingUnitDAO.saveAll(unitList);
                        savedContentList = trainingContentDAO.saveAll(contentList);
                        trainingMaterialDAO.saveAll(trainingMaterialList);
                        contentList.clear();
                        unitList.clear();
                        trainingMaterialList.clear();
                        for (Map.Entry<Integer, String> entry : learningObjectiveMap.entrySet()) {
                            log.info(entry.getKey());
                            log.info(entry.getValue());
                            if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue())) {
                                standardOutput = standardOutputDAO.findById(entry.getValue().toUpperCase()).get();
                            }

                            if (!syllabusObjectiveMap.containsKey(entry.getValue())) {
                                SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                        .id(SyllabusStandardOutputCompositeKey.builder()
                                                .outputCode(entry.getValue())
                                                .topicCode(syllabus.getTopicCode())
                                                .build())
                                        .outputCode(standardOutput)
                                        .topicCode(syllabus)
                                        .build();
                                syllabusObjectiveList.add(syllabusObjective);
                                syllabusObjectiveMap.put(entry.getValue(), syllabus.getTopicCode());
                            }
                            log.info(standardOutput.getOutputCode());
//                                LearningObjective learningObjective = LearningObjective.builder()
//                                        .contentCode(savedContentList.get(entry.getKey()))
//                                        .outputCode(standardOutput)
//                                        .description(standardOutput.getDescription())
//                                        .name(standardOutput.getOutputName())
//                                        .type("fpt dogshit")
//                                        .build();
//                                learningObjectiveList.add(learningObjective);
//                            log.info(learningObjectiveList);
                            log.info(savedContentList.get(entry.getKey()));
//                            learningObjectiveDAO.saveAll(learningObjectiveList);
//                            learningObjectiveList.clear();
//                            learningObjectiveList = new ArrayList<>();
                        }


                        learningObjectiveMap.clear();
                    }

                }

            }

            log.info("saved all training unit");

            if (!unitList.isEmpty()) {
                trainingUnitDAO.saveAll(unitList);
                unitList.clear();
                log.info(learningObjectiveMap);
                savedContentList = trainingContentDAO.saveAll(contentList);
                trainingMaterialDAO.saveAll(trainingMaterialList);
                contentList.clear();
                trainingMaterialList.clear();
                for (Map.Entry<Integer, String> entry : learningObjectiveMap.entrySet()) {
                    log.info(entry.getKey());
                    log.info(entry.getValue());
                    if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue())) {
                        standardOutput = standardOutputDAO.findById(entry.getValue().toUpperCase()).get();
                    }

                    if (!syllabusObjectiveMap.containsKey(entry.getValue())) {
                        SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                .id(SyllabusStandardOutputCompositeKey.builder()
                                        .outputCode(entry.getValue())
                                        .topicCode(syllabus.getTopicCode())
                                        .build())
                                .outputCode(standardOutput)
                                .topicCode(syllabus)
                                .build();
                        syllabusObjectiveList.add(syllabusObjective);
                        syllabusObjectiveMap.put(entry.getValue(), syllabus.getTopicCode());
                    }
                    log.info(standardOutput.getOutputCode());
//                        LearningObjective learningObjective = LearningObjective.builder()
//                                .contentCode(savedContentList.get(entry.getKey()))
//                                .outputCode(standardOutput)
//                                .description(standardOutput.getDescription())
//                                .name(standardOutput.getOutputName())
//                                .type("fpt dogshit")
//                                .build();
//                        learningObjectiveList.add(learningObjective);
//                    log.info(learningObjectiveList);
                    log.info(savedContentList.get(entry.getKey()));

                }
//                learningObjectiveDAO.saveAll(learningObjectiveList);
//                learningObjectiveList.clear();
//                learningObjectiveList = new ArrayList<>();
            }
            log.info(syllabusObjectiveMap);
            syllabusObjectiveDAO.saveAll(syllabusObjectiveList);

            int numberOfDay = trainingUnitDAO.countDayNumberBySyllabus_TopicCode(syllabus.getTopicCode());

            syllabus.setNumberOfDay(numberOfDay);
            syllabusDAO.save(syllabus);

//            for (int i = 0; i < request.getTrainingMaterials().size(); i++) {
//                for (int j = 0; j < request.getTrainingMaterials().get(i).getFiles().getFileName().size(); j++) {
//                    String putPresignedUrl = fileService.generateUrl(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j) + "_"
//                            + request.getTopicCode() + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getUnitCode()) + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getContentCode()), HttpMethod.PUT);
//                    String getPresignedUrl = fileService.generateUrl(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j) + "_" + request.getTopicCode() + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getUnitCode()) + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getContentCode()), HttpMethod.GET);
//
//                    syllabus = syllabusDAO.findById(request.getTopicCode()).get();
//
//                    TrainingUnit trainingUnit = trainingUnitDAO.findById(
//                            SyllabusTrainingUnitCompositeKey.builder()
//                                    .tCode(syllabus.getTopicCode())
//                                    .uCode(request.getTrainingMaterials().get(i).getFiles().getUnitCode())
//                                    .build()).get();
//
//                    TrainingContent trainingContent = trainingContentDAO.findByContentIdAndUnitCode_Id(
//                            request.getTrainingMaterials().get(i).getFiles().getContentCode(),
//                            SyllabusTrainingUnitCompositeKey.builder()
//                                    .tCode(syllabus.getTopicCode())
//                                    .uCode(request.getTrainingMaterials().get(i).getFiles().getUnitCode())
//                                    .build()
//                    );
//
//                    TrainingMaterial trainingMaterial = TrainingMaterial.builder()
//                            .material(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j))
//                            .source(getPresignedUrl)
//                            .trainingContent(trainingContent)
//                            .trainingUnit(trainingUnit)
//                            .syllabus(syllabus)
//                            .build();
//
//                    trainingMaterialList.add(trainingMaterial);
//                }
//            }

            return CreateSyllabusResponse.builder()
                    .status(0)
                    .message("Create syllabus successfully.")
                    .url(presignedUrlResponseList)
                    .build();

//        for(Map.Entry<String, String> entry: syllabusObjectiveMap.entrySet()){
//            LearningObjective learningObjective = learningObjectiveDAO.findById(entry.getKey()).get();
//            syllabus.getLearningObjectives().add(learningObjective);
////            learningObjective.getSyllabus().add(syllabus);
//
//            syllabusDAO.save(syllabus);
//            learningObjectiveList.add(learningObjective);
//        }
//        learningObjectiveDAO.saveAll(learningObjectiveList);
        } catch (Exception err) {
            err.printStackTrace();
            return CreateSyllabusResponse.builder()
                    .status(-1)
                    .message("Server error, have to fix some shit again :(.")
                    .url(null)
                    .build();
        }
    }

    @Override
    public UpdateSyllabusResponse updateSyllabus(CreateSyllabusOutlineRequest request){
        Syllabus syllabus = syllabusDAO.findById(request.getTopicCode()).isEmpty() ? null : syllabusDAO.findById(request.getTopicCode()).get();
        if (syllabus == null) {
            return UpdateSyllabusResponse.builder()
                    .status(1)
                    .message("syllabus with id " + request.getTopicCode() + " don't exist.")
                    .url(null)
                    .build();
        }
        int unitBatchSize = 10;
        int contentBatchSize = 10;
        String putPresignedUrl = "";
        String getPresignedUrl = "";
        List<TrainingUnit> unitList = trainingUnitDAO.findBySyllabus_TopicCode(request.getTopicCode());
        List<TrainingContent> contentList = trainingContentDAO.findByUnitCode_Syllabus_TopicCode(request.getTopicCode());
        List<TrainingContent> savedContentList;
        List<SyllabusObjective> syllabusObjectiveList = syllabusObjectiveDAO.findByTopicCode_TopicCode(request.getTopicCode());
        List<TrainingMaterial> trainingMaterialList = trainingMaterialDAO.findByTrainingContent_UnitCode_Syllabus_TopicCode(request.getTopicCode());
        LinkedHashMap<Integer, String> learningObjectiveMap = new LinkedHashMap<>();
        List<PresignedUrlResponse> presignedUrlResponseList = new ArrayList<>();
        Map<String, String> syllabusObjectiveMap = new HashMap<>();

        User creator = userDAO.findByEmail(request.getCreatorEmail()).get();
        try {

                    trainingUnitDAO.deleteAll(unitList);
                    trainingContentDAO.deleteAll(contentList);
                    syllabusObjectiveDAO.deleteAll(syllabusObjectiveList);
                    trainingMaterialDAO.deleteAll(trainingMaterialList);

                    unitList.clear();
                    contentList.clear();
                    syllabusObjectiveList.clear();
                    trainingMaterialList.clear();

                    syllabus.setTopicName(request.getTopicName());
                    syllabus.setTrainingAudience(Integer.parseInt(request.getTrainingAudience()));
                    syllabus.setCourseObjective(request.getCourseObjective());
                    syllabus.setTechnicalGroup(request.getTechnicalRequirement());
                    syllabus.setPublishStatus(request.getPublishStatus());
                    syllabus.setPriority(request.getPriority());
                    syllabus.setVersion(request.getVersion());
                    syllabus.setTrainingPrinciples(request.getTrainingPrinciple());
                    syllabus.setModifiedDate(new Date());
                    syllabus.setModifiedBy(creator.getEmail());

            syllabusDAO.save(syllabus);

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
                        StandardOutput outputCode = standardOutputDAO.findById(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getStandardOutput()).get();


                        TrainingContent trainingContent = TrainingContent.builder()
                                .id(SyllabusTrainingUnitTrainingContentCompositeKey.builder()
                                        .contentCode(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContentId())
                                        .id(SyllabusTrainingUnitCompositeKey.builder()
                                                .tCode(request.getTopicCode())
                                                .uCode(trainingUnit.getId().getUCode())
                                                .build())
                                        .build())
                                .unitCode(trainingUnit)
//                                .contentId(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContentId())
                                .deliveryType(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDeliveryType())
                                .note(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getNote())
                                .content_name(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContent())
                                .trainingFormat(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).isOnline())
                                .duration(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getDuration())
                                .outputCode(outputCode)
                                .build();

                        learningObjectiveMap.put(contentList.size(), request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getStandardOutput());
                        contentList.add(trainingContent);

                        List<String> putPresignedUrlList = new ArrayList<>();
                        List<String> getPresginedUrlList = new ArrayList<>();
                        for (int x = 0; x < request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().size(); x++) {

                            putPresignedUrl = fileService.generateUrl(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x) + "_"
                                    + syllabus.getTopicCode() + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode(), HttpMethod.PUT);
                            getPresignedUrl = fileService.generateUrl(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x) + "_"
                                    + syllabus.getTopicCode() + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode(), HttpMethod.GET);

                            TrainingMaterial trainingMaterial = TrainingMaterial.builder()
                                    .material(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingMaterials().get(x))
                                    .source(getPresignedUrl)
                                    .trainingContent(trainingContent)
//                                    .syllabus(syllabus)
                                    .build();

                            trainingMaterialList.add(trainingMaterial);
                            getPresginedUrlList.add(getPresignedUrl);
                            putPresignedUrlList.add(putPresignedUrl);
                        }
                        PresignedUrlResponse res = PresignedUrlResponse.builder()
                                .getPresignedUrl(getPresginedUrlList)
                                .putPresignedUrl(putPresignedUrlList)
                                .id("c" + Integer.toString(request.getSyllabus().get(i).getUnits().get(j).getUnitCode()) + trainingContent.getId().getContentCode())
                                .build();

                        log.info(res);

                        presignedUrlResponseList.add(res);
                    }


                    if (contentList.size() >= contentBatchSize && unitList.size() >= unitBatchSize) {
                        log.info("content list size: " + contentList.size());
                        trainingUnitDAO.saveAll(unitList);
                        savedContentList = trainingContentDAO.saveAll(contentList);
                        trainingMaterialDAO.saveAll(trainingMaterialList);
                        contentList.clear();
                        unitList.clear();
                        trainingMaterialList.clear();
                        for (Map.Entry<Integer, String> entry : learningObjectiveMap.entrySet()) {
                            log.info(entry.getKey());
                            log.info(entry.getValue());
                            if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue())) {
                                standardOutput = standardOutputDAO.findById(entry.getValue().toUpperCase()).get();
                            }

                            if (!syllabusObjectiveMap.containsKey(entry.getValue())) {
                                SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                        .id(SyllabusStandardOutputCompositeKey.builder()
                                                .outputCode(entry.getValue())
                                                .topicCode(syllabus.getTopicCode())
                                                .build())
                                        .outputCode(standardOutput)
                                        .topicCode(syllabus)
                                        .build();
                                syllabusObjectiveList.add(syllabusObjective);
                                syllabusObjectiveMap.put(entry.getValue(), syllabus.getTopicCode());
                            }
                            log.info(standardOutput.getOutputCode());
//                                LearningObjective learningObjective = LearningObjective.builder()
//                                        .contentCode(savedContentList.get(entry.getKey()))
//                                        .outputCode(standardOutput)
//                                        .description(standardOutput.getDescription())
//                                        .name(standardOutput.getOutputName())
//                                        .type("fpt dogshit")
//                                        .build();
//                                learningObjectiveList.add(learningObjective);
//                            log.info(learningObjectiveList);
                            log.info(savedContentList.get(entry.getKey()));
//                            learningObjectiveDAO.saveAll(learningObjectiveList);
//                            learningObjectiveList.clear();
//                            learningObjectiveList = new ArrayList<>();
                        }


                        learningObjectiveMap.clear();
                    }

                }

            }

            log.info("saved all training unit");

            if (!unitList.isEmpty()) {
                trainingUnitDAO.saveAll(unitList);
                unitList.clear();
                log.info(learningObjectiveMap);
                savedContentList = trainingContentDAO.saveAll(contentList);
                trainingMaterialDAO.saveAll(trainingMaterialList);
                contentList.clear();
                trainingMaterialList.clear();
                for (Map.Entry<Integer, String> entry : learningObjectiveMap.entrySet()) {
                    log.info(entry.getKey());
                    log.info(entry.getValue());
                    if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue())) {
                        standardOutput = standardOutputDAO.findById(entry.getValue().toUpperCase()).get();
                    }

                    if (!syllabusObjectiveMap.containsKey(entry.getValue())) {
                        SyllabusObjective syllabusObjective = SyllabusObjective.builder()
                                .id(SyllabusStandardOutputCompositeKey.builder()
                                        .outputCode(entry.getValue())
                                        .topicCode(syllabus.getTopicCode())
                                        .build())
                                .outputCode(standardOutput)
                                .topicCode(syllabus)
                                .build();
                        syllabusObjectiveList.add(syllabusObjective);
                        syllabusObjectiveMap.put(entry.getValue(), syllabus.getTopicCode());
                    }
                    log.info(standardOutput.getOutputCode());
//                        LearningObjective learningObjective = LearningObjective.builder()
//                                .contentCode(savedContentList.get(entry.getKey()))
//                                .outputCode(standardOutput)
//                                .description(standardOutput.getDescription())
//                                .name(standardOutput.getOutputName())
//                                .type("fpt dogshit")
//                                .build();
//                        learningObjectiveList.add(learningObjective);
//                    log.info(learningObjectiveList);
                    log.info(savedContentList.get(entry.getKey()));

                }
//                learningObjectiveDAO.saveAll(learningObjectiveList);
//                learningObjectiveList.clear();
//                learningObjectiveList = new ArrayList<>();
            }
            log.info(syllabusObjectiveMap);
            syllabusObjectiveDAO.saveAll(syllabusObjectiveList);

            int numberOfDay = trainingUnitDAO.countDayNumberBySyllabus_TopicCode(syllabus.getTopicCode());

            syllabus.setNumberOfDay(numberOfDay);
            syllabusDAO.save(syllabus);

//            for (int i = 0; i < request.getTrainingMaterials().size(); i++) {
//                for (int j = 0; j < request.getTrainingMaterials().get(i).getFiles().getFileName().size(); j++) {
//                    String putPresignedUrl = fileService.generateUrl(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j) + "_"
//                            + request.getTopicCode() + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getUnitCode()) + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getContentCode()), HttpMethod.PUT);
//                    String getPresignedUrl = fileService.generateUrl(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j) + "_" + request.getTopicCode() + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getUnitCode()) + Integer.toString(request.getTrainingMaterials().get(i).getFiles().getContentCode()), HttpMethod.GET);
//
//                    syllabus = syllabusDAO.findById(request.getTopicCode()).get();
//
//                    TrainingUnit trainingUnit = trainingUnitDAO.findById(
//                            SyllabusTrainingUnitCompositeKey.builder()
//                                    .tCode(syllabus.getTopicCode())
//                                    .uCode(request.getTrainingMaterials().get(i).getFiles().getUnitCode())
//                                    .build()).get();
//
//                    TrainingContent trainingContent = trainingContentDAO.findByContentIdAndUnitCode_Id(
//                            request.getTrainingMaterials().get(i).getFiles().getContentCode(),
//                            SyllabusTrainingUnitCompositeKey.builder()
//                                    .tCode(syllabus.getTopicCode())
//                                    .uCode(request.getTrainingMaterials().get(i).getFiles().getUnitCode())
//                                    .build()
//                    );
//
//                    TrainingMaterial trainingMaterial = TrainingMaterial.builder()
//                            .material(request.getTrainingMaterials().get(i).getFiles().getFileName().get(j))
//                            .source(getPresignedUrl)
//                            .trainingContent(trainingContent)
//                            .trainingUnit(trainingUnit)
//                            .syllabus(syllabus)
//                            .build();
//
//                    trainingMaterialList.add(trainingMaterial);
//                }
//            }

            return UpdateSyllabusResponse.builder()
                    .message("update syllabus with id: " + request.getTopicCode() + " successfully.")
                    .status(0)
                    .url(presignedUrlResponseList)
                    .build();

//        for(Map.Entry<String, String> entry: syllabusObjectiveMap.entrySet()){
//            LearningObjective learningObjective = learningObjectiveDAO.findById(entry.getKey()).get();
//            syllabus.getLearningObjectives().add(learningObjective);
////            learningObjective.getSyllabus().add(syllabus);
//
//            syllabusDAO.save(syllabus);
//            learningObjectiveList.add(learningObjective);
//        }
//        learningObjectiveDAO.saveAll(learningObjectiveList);
        } catch (Exception err) {
            err.printStackTrace();
            return UpdateSyllabusResponse.builder()
                    .status(-1)
                    .message("Server error, have to fix some shit again :(.")
                    .url(null)
                    .build();
        }

    }


//    @Override
//    public UpdateSyllabusResponse updateSyllabusOther(UpdateSyllabusGeneralRequest updateSyllabusGeneralRequest, String topicCode) {
//        Syllabus syllabus = syllabusDAO.findById(topicCode).get();
//        if (syllabus != null) {
//            syllabus.setTrainingPrinciples(updateSyllabusGeneralRequest.getTrainingPrinciple());
//            Syllabus syllabusUpdate = syllabusDAO.save(syllabus);
//            if (syllabusUpdate != null) {
//                return UpdateSyllabusResponse.builder()
//                        .status("Update Syllbus successful")
//                        .updateSyllabus(syllabusUpdate)
//                        .build();
//
//
//            } else {
//                return UpdateSyllabusResponse.builder()
//                        .status("Update Syllbus failed")
//                        .updateSyllabus(null)
//                        .build();
//
//            }
//
//        } else {
//            return UpdateSyllabusResponse.builder()
//                    .status("Syllabus not found")
//                    .updateSyllabus(null)
//                    .build();
//
//        }
//    }
//
//    @Override
//    public UpdateSyllabusResponse updateSyllabusGeneral(UpdateSyllabusGeneralRequest update, String topicCode) {
//        Syllabus syllabus = syllabusDAO.findById(topicCode).get();
//        if (syllabus != null) {
//            syllabus.setTopicName(update.getTopicName());
//            syllabus.setVersion(update.getVersion());
//            syllabus.setTechnicalGroup(update.getTechnicalRequirement());
//            syllabus.setPriority(update.getPriority());
//            syllabus.setCourseObjective(update.getCourseObjective());
//            syllabus.setPublishStatus(update.getPublishStatus());
//            syllabus.setTrainingAudience(update.getTrainingAudience());
//            Syllabus syllabusUpdate = syllabusDAO.save(syllabus);
//            if (syllabusUpdate != null) {
//                return UpdateSyllabusResponse.builder()
//                        .status("Update Syllbus successful")
//                        .updateSyllabus(syllabusUpdate)
//                        .build();
//
//
//            } else {
//                return UpdateSyllabusResponse.builder()
//                        .status("Update Syllbus failed")
//                        .updateSyllabus(null)
//                        .build();
//
//            }
//        }
//
//        return UpdateSyllabusResponse.builder()
//                .status("Syllabus not found")
//                .updateSyllabus(null)
//                .build();
//
//
//    }
//
//    @Override
//    public UpdateSyllabusResponse updateSyllabusOutline(UpdateSyllabusOutlineRequest update, String topicCode) {
//        return null;
//    }


    @Override
    public Syllabus getSyllabusById(String topicCode) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        return optionalSyllabus.orElse(null);
    }

    @Override
    public DeleteSyllabusResponse deleteSyllabus(String topicCode) {
        Syllabus existedSyllabus = syllabusDAO.findById(topicCode).isPresent() ? syllabusDAO.findById(topicCode).get() : null;

        try {
            if (existedSyllabus != null) {
                existedSyllabus.setDeleted(true);
                syllabusDAO.save(existedSyllabus);
                return DeleteSyllabusResponse.builder()
                        .status(0)
                        .message("Successfully delete syllabus with id: " + topicCode)
                        .build();
            }
            return DeleteSyllabusResponse.builder()
                    .status(1)
                    .message("Can't find syllabus with id: " + topicCode)
                    .build();
        } catch (Exception err) {
            err.printStackTrace();
            return DeleteSyllabusResponse.builder()
                    .status(-1)
                    .message("Fail to delete syllabus with id: " + topicCode)
                    .build();
        }
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
//                .trainingMaterials(originalSyllabus.getTrainingMaterials())
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

//    public PresignedUrlResponse generatePresignedUrl(List<String> files, String topicCode, int unitCode, int contentCode) {
//        List<String> putPresignedUrlList = new ArrayList<>();
//        List<String> getPresignedUrlList = new ArrayList<>();
//        try {
//            for (int i = 0; i < files.size(); i++) {
//                String putPresignedUrl = fileService.generateUrl(files.get(i) + "_" + topicCode + Integer.toString(unitCode) + Integer.toString(contentCode), HttpMethod.PUT);
//                String getPresignedUrl = fileService.generateUrl(files.get(i) + "_" + topicCode + Integer.toString(unitCode) + Integer.toString(contentCode), HttpMethod.GET);
//                putPresignedUrlList.add(putPresignedUrl);
//                getPresignedUrlList.add(getPresignedUrl);
//            }
//            return PresignedUrlResponse.builder()
//                    .putPresignedUrl(putPresignedUrlList)
//                    .getPresignedUrl(getPresignedUrlList)
//                    .message("successfully generate presigned url.")
//                    .status(0)
//                    .build();
//        } catch (Exception err) {
//            err.printStackTrace();
//            return PresignedUrlResponse.builder()
//                    .putPresignedUrl(putPresignedUrlList)
//                    .getPresignedUrl(getPresignedUrlList)
//                    .message("fail to generate presigned url.")
//                    .status(-1)
//                    .build();
//        }
//
//    }

}

