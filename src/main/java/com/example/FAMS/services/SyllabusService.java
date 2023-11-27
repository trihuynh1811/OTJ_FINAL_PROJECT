package com.example.FAMS.services;

import com.example.FAMS.dto.requests.SyllbusRequest.UpdateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.UpdateSyllabusOutlineRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusGeneralRequest;
import com.example.FAMS.dto.requests.SyllbusRequest.CreateSyllabusOutlineRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.dto.responses.Syllabus.CreateSyllabusResponse;
import com.example.FAMS.dto.responses.Syllabus.DeleteSyllabusResponse;
import com.example.FAMS.dto.responses.Syllabus.GetSyllabusByPage;
import com.example.FAMS.dto.responses.Syllabus.SyllabusResponse;
import com.example.FAMS.dto.responses.UpdateSyllabusResponse;
import com.example.FAMS.models.Syllabus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public interface SyllabusService {

    public List<SyllabusResponse> getSyllabuses(@Nullable String type);

    ResponseEntity<ResponseObject> getAllActiveSyllabus();

    Syllabus getDetailSyllabus(String topicCode);

    public CreateSyllabusResponse createSyllabus(CreateSyllabusOutlineRequest request);

    public UpdateSyllabusResponse updateSyllabus(CreateSyllabusOutlineRequest request);

    public GetSyllabusByPage paging(int amount, int pageNumber);

//    UpdateSyllabusResponse updateSyllabusOther(UpdateSyllabusGeneralRequest updateSyllabusGeneralRequest, String topicCode);
//    UpdateSyllabusResponse updateSyllabusGeneral(UpdateSyllabusGeneralRequest update , String topicCode);
//    UpdateSyllabusResponse updateSyllabusOutline(UpdateSyllabusOutlineRequest update ,String topicCode);

    Syllabus getSyllabusById(String topicCode);

    DeleteSyllabusResponse deleteSyllabus(String topicCode);



    List<Syllabus> processDataFromCSV(MultipartFile file, String choice, String seperator, String scan, Authentication authentication) throws IOException;

    Syllabus duplicateSyllabusByName(String topicCode, String topicName, Authentication authentication);

    Syllabus duplicateSyllabusByNameAndCode(String topicCode, String topicName, Authentication authentication);

    Syllabus duplicateSyllabus(String topicCode, Authentication authentication);
}
