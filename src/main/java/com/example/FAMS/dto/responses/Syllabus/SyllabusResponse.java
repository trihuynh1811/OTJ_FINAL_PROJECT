package com.example.FAMS.dto.responses.Syllabus;


import com.example.FAMS.dto.responses.Class.UserDTO;
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
public class SyllabusResponse {

    int responseStatus = 0;
    String responseMessage = "";
    String syllabusName = "";
    String syllabusCode = "";
    String createdOn = "";
    UserDTO createdBy = null;
    String duration = "";
    String syllabusStatus = "";
    String level = "";
    String attendeeNumber = "";
    String technicalRequirement = "";
    String courseObjective = "";
    String trainingPrinciple = "";
    String modifiedOn = "";
    UserDTO modifiedBy = null;
    String version = "";
    boolean deleted = false;
    List<String> syllabusObjectiveList = new ArrayList<>();
    List<DayDTO> dayList = new ArrayList<>();
}
