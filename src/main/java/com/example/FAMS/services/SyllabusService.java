package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.models.Syllabus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SyllabusService {

    public List<Syllabus> getSyllabuses();

    List<Syllabus> getDetailSyllabus();

    public Syllabus createSyllabus(String topicName, String topicCode, String version, int numberOfAudience);

    Syllabus updateSyllabus(UpdateSyllabusRequest updatesyllabusRequest);

    Syllabus getSyllabusById(String topicCode);


    List<Syllabus> processDataFromCSV(MultipartFile file) throws IOException;

    Syllabus duplicateSyllabus(String topicCode);
}