package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    String line = "";

    @Override
    public List<Syllabus> getSyllabuses() {
        return syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public List<Syllabus> getDetailSyllabus() {
        return syllabusDAO.findAll();
    }


    @Override
    public Syllabus createSyllabus(String topicName, String topicCode, String version, int numberOfAudience) {
        Syllabus syllabus = Syllabus.builder()
                .version(version)
                .topicCode(topicCode)
                .topicName(topicName)
                .trainingAudience(numberOfAudience)
                .build();

        syllabusDAO.save(syllabus);
        return syllabus;
    }

    @Override
    public Syllabus updateSyllabus(UpdateSyllabusRequest updatesyllabusRequest) {
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(updatesyllabusRequest.getTopicCode());
        Syllabus syllabusexits = optionalSyllabus.orElse(null);
        if (syllabusexits != null) {
            syllabusexits.setTopicName(updatesyllabusRequest.getTopicName());
            syllabusexits.setTechnicalGroup(updatesyllabusRequest.getTechnicalGroup());
            syllabusexits.setVersion(updatesyllabusRequest.getVersion());
            syllabusexits.setTrainingAudience(updatesyllabusRequest.getTrainingAudience());
            syllabusexits.setTopicOutline(updatesyllabusRequest.getTopicOutline());
            syllabusexits.setTrainingMaterials(updatesyllabusRequest.getTrainingMaterials());
            syllabusexits.setPriority(updatesyllabusRequest.getPriority());
            syllabusexits.setPublishStatus(updatesyllabusRequest.getPublishStatus());
            syllabusexits.setCreatedBy(updatesyllabusRequest.getCreatedBy());
            syllabusexits.setCreatedDate(updatesyllabusRequest.getCreatedDate());
            syllabusexits.setModifiedBy(updatesyllabusRequest.getModifiedBy());
            syllabusexits.setModifiedDate(updatesyllabusRequest.getModifiedDate());


            Syllabus syllabusUpdate = syllabusDAO.save(syllabusexits);

            if (syllabusUpdate != null) {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus successful")
                        .updateSyllabus(syllabusUpdate)
                        .build().getUpdateSyllabus();


            } else {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus failed")
                        .updateSyllabus(null)
                        .build().getUpdateSyllabus();

            }


        } else {
            return UpdateSyllabusResponse.builder()
                    .status("Syllabus not found")
                    .updateSyllabus(null)
                    .build().getUpdateSyllabus();

        }

    }

    @Override
    public Syllabus getSyllabusById (String topicCode){
        Optional<Syllabus> optionalSyllabus = syllabusDAO.findById(topicCode);
        return optionalSyllabus.orElse(null);}

    @Override

    public List<Syllabus> loadSyllabusData() {
        List<Syllabus> customerList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/syllabus.csv"));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Syllabus c = new Syllabus();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                c.setTopicCode(data[0]);
                c.setCreatedBy(data[1]);
                c.setCreatedDate(dateFormat.parse(data[2]));
                c.setModifiedBy(data[3]);
                c.setModifiedDate(dateFormat.parse(data[4]));
                c.setPriority(data[5]);
                c.setPublishStatus(data[6]);
                c.setTechnicalGroup(data[7]);
                c.setTopicName(data[8]);
                c.setTopicOutline(data[9]);
                c.setTrainingAudience(Integer.parseInt(data[10]));
                c.setTrainingMaterials(data[11]);
                c.setTrainingPrinciples(data[12]);
                c.setVersion(data[13]);
//                c.setUserID(data[14]);
                customerList.add(c);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return customerList;



        }



    }
