package com.example.FAMS.dto.responses.Class;

import com.example.FAMS.dto.responses.Class.TrainingProgramDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailResponse {

    private String message = "";
    private String classCode;
    private String className;
    private String duration;
    private String status;
    private String timeFrom;
    private String timeTo;
//    private List<String> location;
    private String location;
    private String fsu;
    private String startDate;
    private String endDate;
    private String admin;
    private String createdBy;
    private String review;
    private String approve;
    private String attendee;
    private String attendeePlanned;
    private String attendeeAccepted;
    private String attendeeActual;
    private String createdDate;
    private String modifiedBy = "";
    private String modifiedDate = null;
    private boolean deactivated = false;

    private List<TrainerDTO> trainerList = new ArrayList<>();
    private List<UserDTO> adminList = new ArrayList<>();
    private List<UserDTO> attendeeList = new ArrayList<>();

    TrainingProgramDTO trainingProgram;

    List<SyllabusDTO> syllabusList = new ArrayList<>();
    List<String> listDay = new ArrayList<>();

    public ClassDetailResponse(String message){
        this.message = message;

    }
}
