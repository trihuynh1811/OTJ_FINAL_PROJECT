package com.example.FAMS.services;

import com.example.FAMS.dto.requests.SyllbusRequest.UpdateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.UpdateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.responses.Syllabus.DeleteSyllabusResponse;
import com.example.FAMS.dto.responses.Syllabus.GetSyllabusByPage;
import com.example.FAMS.dto.responses.Syllabus.GetAllSyllabusResponse;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface SyllabusService {

    public List<GetAllSyllabusResponse> getSyllabuses();

    Syllabus getDetailSyllabus(String topicCode);

    public int createSyllabusGeneral(CreateSyllabusGeneralRequest request, Authentication authentication);
    public void createSyllabusOutline(CreateSyllabusOutlineRequest request, Authentication authentication);

    public int createSyllabusOther(CreateSyllabusGeneralRequest request);

    public GetSyllabusByPage paging(int amount, int pageNumber);

    UpdateSyllabusResponse updateSyllabusOther(UpdateSyllabusGeneralRequest updateSyllabusGeneralRequest, String topicCode);
    UpdateSyllabusResponse updateSyllabusGeneral(UpdateSyllabusGeneralRequest update , String topicCode);
    UpdateSyllabusResponse updateSyllabusOutline(UpdateSyllabusOutlineRequest update ,String topicCode);
    Syllabus getSyllabusById(String topicCode);

    DeleteSyllabusResponse deleteSyllabus(String topicCode);


    List<Syllabus> processDataFromCSV(MultipartFile file, String choice, Authentication authentication) throws IOException;

    Syllabus duplicateSyllabus(String topicCode, Authentication authentication);
}
