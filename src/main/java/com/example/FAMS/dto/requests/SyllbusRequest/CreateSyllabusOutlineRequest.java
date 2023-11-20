package com.example.FAMS.dto.requests.SyllbusRequest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSyllabusOutlineRequest {
    String topicCode;
    String topicName;
    String version;
    String technicalRequirement;
    String priority;
    String courseObjective;
    String publishStatus = "inactive";
    String trainingPrinciple;
    String creatorEmail;
    String trainingAudience;
    List<DayDTO> syllabus;
//    List<TrainingMaterialDTO> trainingMaterials;

}
