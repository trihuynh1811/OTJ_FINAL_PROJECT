package com.example.FAMS.dto.requests.ClassRequest;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClassDTO {

     String classCode;
     String nameClass;
     String totalTimeLearning;
     String classTimeFrom;
     String classTimeTo;
     String status;
     List<String> location;
     String fsu;
     String startDate;
     String endDate;
     String created;
     String review;
     String approve;
     String attendee;
     String attendeePlanned;
     String attendeeAccepted;
     String attendeeActual;
     String trainingProgram;
     List<String> attendeeList;
     List<String> listDay;
     List<TrainerSyllabusDTO> trainer;
}
