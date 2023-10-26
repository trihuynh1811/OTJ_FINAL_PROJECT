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

     String classId;
     String className;
     int duration;
     String timeFrom;
     String timeTo;
     String status;
     String location;
     String fsu;
     String startDate;
     String endDate;
     String attendee;
     int attendeePlanned;
     int attendeeAccepted;
     int attendeeActual;
     int TrainingProgram;
}
