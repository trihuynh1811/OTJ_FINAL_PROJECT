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
    String assignmentLab = "0%";
    String conceptLecture = "0%";
    String guideReview = "0%";
    String testQuiz = "0%";
    String exam = "0%";
    String quiz = "0%";
    String assignment = "0%";
    String fin = "0%";
    String finalTheory = "0%";
    String finalPractice = "0%";
    String gpa = "0%";
    List<DayDTO> syllabus;
//    List<TrainingMaterialDTO> trainingMaterials;

}
