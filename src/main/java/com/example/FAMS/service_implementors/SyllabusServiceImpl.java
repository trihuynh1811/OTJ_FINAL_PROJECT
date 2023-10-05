package com.example.FAMS.service_implementors;

import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.services.SyllabusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    @Override
    public List<Syllabus> getSyllabuses(){
        return syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
    }

    @Override
    public List<Syllabus> getDetailSyllabus() {
        return syllabusDAO.findAll();
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
}
