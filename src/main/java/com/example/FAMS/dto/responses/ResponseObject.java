package com.example.FAMS.dto.responses;

import com.example.FAMS.models.TrainingProgramSyllabus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    private String status;
    private String message;
    private int numberOfPage;
    private Object payload;



    public ResponseObject(String status, String message, Object payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }
}
