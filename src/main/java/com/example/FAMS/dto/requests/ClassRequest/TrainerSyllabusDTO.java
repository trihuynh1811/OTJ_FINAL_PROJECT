package com.example.FAMS.dto.requests.ClassRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerSyllabusDTO {

    String trainerEmail;
    List<String> syllabusCode;
}
