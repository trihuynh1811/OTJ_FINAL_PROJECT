package com.example.FAMS.dto.responses.Syllabus;


import com.example.FAMS.models.SyllabusObjective;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSyllabusResponse {

    String syllabusName;
    String syllabusCode;
    Long createdOn;
    String createdBy;
    int duration;
    String status;
    List<String> syllabusObjectiveList = new ArrayList<>();
}
