package com.example.FAMS.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgramDTO {

    int trainingProgramCode;
    String trainingProgramName;
    Date modifyDate;
    String modifyBy;
    Date startDate;
    int duration;
    String status;

}
