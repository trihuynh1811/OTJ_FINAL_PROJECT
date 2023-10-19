package com.example.FAMS.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgramDTO {
    private String name;
    private Date startDate;
    private int duration;
    private String status;
}
