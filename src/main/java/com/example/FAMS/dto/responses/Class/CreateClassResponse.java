package com.example.FAMS.dto.responses.Class;

import com.example.FAMS.models.Class;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateClassResponse {

    Class createdClass;
    String message;
}
