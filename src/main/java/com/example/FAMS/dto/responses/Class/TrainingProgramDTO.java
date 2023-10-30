package com.example.FAMS.dto.responses.Class;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgramDTO {

    String trainingProgramCode;
    String trainingProgramName;
    String modifyDate;
    String modifyBy;
    String duration;
}
