package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.StandardOutputDTO;
import com.example.FAMS.models.*;
import com.example.FAMS.models.composite_key.SyllabusStandardOutputCompositeKey;
import com.example.FAMS.models.composite_key.SyllabusTrainingUnitCompositeKey;
import com.example.FAMS.models.composite_key.TrainingContentLearningObjectiveCompositeKey;
import com.example.FAMS.repositories.*;
import com.example.FAMS.services.SyllabusService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Syllabus> getSyllabuses() {
//        log.info(userDAO.findAll());
        List<Syllabus> syllabusList = syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
        log.info(trainingUnitDAO.countDayNumberBySyllabus_TopicCode("lmao"));
        return syllabusList;
    }

    @Override
    public int createSyllabusGeneral(CreateSyllabusGeneralRequest request, Authentication authentication) {
        if (syllabusDAO.findById(request.getTopicCode()).isPresent() && !request.isUpdated()) {
            return 1;
        }

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

            return 0;
        }

        syllabus = Syllabus.builder()
                .topicCode(request.getTopicCode())
                .topicName(request.getTopicName())
                .trainingAudience(request.getTrainingAudience())
                .courseObjective(request.getCourseObjective())
                .technicalGroup(request.getTechnicalRequirement())
                .publishStatus(request.getPublishStatus())
                .priority(request.getPriority())
                .version(request.getVersion())
                .createdBy(getCreator(authentication).getName())
                .createdDate(new Date())
                .userID(getCreator(authentication))
                .modifiedDate(new Date())
                .modifiedBy(getCreator(authentication).getName())
                .build();

        syllabusDAO.save(syllabus);

        return 0;
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
                            .content(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getContent())
                            .trainingFormat(request.getSyllabus().get(i).getUnits().get(j).getContents().get(z).getTrainingFormat())
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
                    for(Map.Entry<Integer, List<StandardOutputDTO>> entry: learningObjectiveMap.entrySet()){
                        log.info(entry.getKey());
                        log.info(entry.getValue().size());
                        for(int k = 0; k < entry.getValue().size(); k++){
                            if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue().get(k).getOutputCode())) {
                                standardOutput = standardOutputDAO.findById(entry.getValue().get(k).getOutputCode().toUpperCase()).get();
                            }

                            if(!syllabusObjectiveMap.containsKey(entry.getValue().get(k).getOutputCode())){
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
            for(Map.Entry<Integer, List<StandardOutputDTO>> entry: learningObjectiveMap.entrySet()){
                log.info(entry.getKey());
                log.info(entry.getValue().size());
                for(int k = 0; k < entry.getValue().size(); k++){
                    if (!standardOutput.getOutputCode().equalsIgnoreCase(entry.getValue().get(k).getOutputCode())) {
                        standardOutput = standardOutputDAO.findById(entry.getValue().get(k).getOutputCode().toUpperCase()).get();
                    }

                    if(!syllabusObjectiveMap.containsKey(entry.getValue().get(k).getOutputCode())){
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
    public void createSyllabusOther(CreateSyllabusGeneralRequest request) {
        Syllabus syllabus = syllabusDAO.findById(request.getTopicCode()).get();

        syllabus.setTrainingPrinciples(request.getTrainingPrinciple());
        syllabusDAO.save(syllabus);
    }


    public User getCreator(Authentication authentication) {
        Object creator = authentication.getPrincipal();
        if (creator instanceof User) {
            return (User) creator;
        }
        return null;
    }
}
