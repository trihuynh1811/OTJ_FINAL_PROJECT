package com.example.FAMS.services;

import com.example.FAMS.dto.requests.UpdateSyllabusRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.models.Syllabus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface SyllabusService {

    public List<Syllabus> getSyllabuses();

    public int createSyllabusGeneral(CreateSyllabusGeneralRequest request, Authentication authentication);
    public void createSyllabusOutline(CreateSyllabusOutlineRequest request, Authentication authentication);

    public void createSyllabusOther(CreateSyllabusGeneralRequest request);
    List<Syllabus> getDetailSyllabus();

    Syllabus updateSyllabus(UpdateSyllabusRequest updatesyllabusRequest);

    Syllabus getSyllabusById(String topicCode);


    List<Syllabus> processDataFromCSV(MultipartFile file) throws IOException;

    Syllabus duplicateSyllabus(String topicCode, Authentication authentication);
}
