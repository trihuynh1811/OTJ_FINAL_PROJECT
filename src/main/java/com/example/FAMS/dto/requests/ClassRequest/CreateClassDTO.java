package com.example.FAMS.dto.requests.ClassRequest;

import com.example.FAMS.models.TrainingProgram;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClassDTO {

    private String classId;
    private String className;
    private int duration;
    private String timeFrom;
    private String timeTo;
    private String status;
    private String location;
    private String fsu;
    private String startDate;
    private String endDate;
    private String attendee;
    private int attendeePlanned;
    private int attendeeAccepted;
    private int attendeeActual;
    private int TrainingProgram;
}
