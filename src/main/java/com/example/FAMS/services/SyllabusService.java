package com.example.FAMS.services;

import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.models.Syllabus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SyllabusService {

    public List<Syllabus> getSyllabuses();

    public int createSyllabusGeneral(CreateSyllabusGeneralRequest request, Authentication authentication);
    public void createSyllabusOutline(CreateSyllabusOutlineRequest request, Authentication authentication);

    public void createSyllabusOther(CreateSyllabusGeneralRequest request);
}
