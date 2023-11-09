package com.example.FAMS.dto.responses.Class;

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
public class GetClassesByPage {

    int status;
    String message;
    int pageNumber;
    int totalNumberOfPages;
    List<ClassDetailResponse> classList;
}
