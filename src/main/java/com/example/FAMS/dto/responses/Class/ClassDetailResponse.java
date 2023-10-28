package com.example.FAMS.dto.responses.Class;

import com.example.FAMS.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassDetailResponse {

    private String message = "";
    private String classId;
    private String className;
    private String duration;
    private String status;
    private Time timeFrom;
    private Time timeTo;
    private String location;
    private String fsu;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy = "";
    private Date modifiedDate = null;
    private boolean deactivated = false;

    private List<UserDTO> trainerList = new ArrayList<>();
    private List<UserDTO> adminList = new ArrayList<>();

    TrainingProgramDTO trainingProgram;

    List<SyllabusDTO> syllabusList = new ArrayList<>();

    public ClassDetailResponse(String message){
        this.message = message;

    }
}
