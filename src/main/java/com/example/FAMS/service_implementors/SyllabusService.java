package com.example.FAMS.service_implementors;

import com.example.FAMS.models.Syllabus;
import com.example.FAMS.repositories.SyllabusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyllabusService {

    @Autowired
    SyllabusDAO syllabusDAO;

    public List<Syllabus> getSyllabuses(){
        return syllabusDAO.findTop1000ByOrderByCreatedDateDesc();
    }

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
