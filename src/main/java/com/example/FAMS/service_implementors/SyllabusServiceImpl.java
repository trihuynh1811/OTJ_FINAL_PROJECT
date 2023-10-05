package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    @Override
    public List<Syllabus> getSyllabuses(){
        return syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public Syllabus createSyllabus(String topicName, String topicCode, String version, int numberOfAudience){
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
        if(syllabusexits!= null){
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

            if(syllabusUpdate != null){
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus successful")
                        .updateSyllabus(syllabusUpdate)
                        .build().getUpdateSyllabus();


            }else {
                return UpdateSyllabusResponse.builder()
                        .status("Update Syllbus failed")
                        .updateSyllabus(null)
                        .build().getUpdateSyllabus();

            }


        }else{
            return UpdateSyllabusResponse.builder()
                    .status("Syllabus not found")
                    .updateSyllabus(null)
                    .build().getUpdateSyllabus();

        }

    }

}
