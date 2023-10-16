package com.example.FAMS.dto.responses;

import com.example.FAMS.models.Class;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClassResponse {
    private String status;
    private Class updatedClass;
}
