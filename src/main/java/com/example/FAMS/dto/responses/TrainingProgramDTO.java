package com.example.FAMS.dto.responses;

import com.example.FAMS.models.TrainingProgramSyllabus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgramDTO {

    String trainingProgramName;
    String trainerGmail;
    String[] topicCode;
    int duration;
    String status;
}
