package com.example.FAMS.dto.responses.Syllabus;

import com.example.FAMS.dto.responses.Syllabus.GetAllSyllabusResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSyllabusByPage {

    int status;
    String message;
    int pageNumber;
    int totalNumberOfPages;
    List<GetAllSyllabusResponse> syllabusList;
}
