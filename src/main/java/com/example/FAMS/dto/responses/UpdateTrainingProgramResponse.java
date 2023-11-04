package com.example.FAMS.dto.responses;

import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.TrainingProgram;
import com.example.FAMS.models.TrainingProgramSyllabus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainingProgramResponse {
    private String messager;
    private TrainingProgram updateTrainingProgram;
//    private TrainingProgramSyllabus trainingProgramSyllabus;

}