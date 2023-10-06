package com.example.FAMS.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePermissionRequest {

    private String roleName;
    private String syllabus;
    private String trainingProgram;
    private String userclass;
    private String learningMaterial;
    private String userManagement;

}
