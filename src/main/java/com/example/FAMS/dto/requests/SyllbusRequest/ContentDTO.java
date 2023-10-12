package com.example.FAMS.dto.requests.SyllbusRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentDTO {

    int contentId;
    String content = "";
    String note = "";
    String deliveryType;
    String trainingFormat;
    int duration;
    List<StandardOutputDTO> standardOutput;
}
