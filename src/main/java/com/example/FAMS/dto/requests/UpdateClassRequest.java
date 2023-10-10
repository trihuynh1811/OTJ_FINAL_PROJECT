package com.example.FAMS.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassRequest {
    private int classId;
    private String className;
    private String classCode;
    private String duration;
    private String status;
    private String location;
    private String fsu;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
}
