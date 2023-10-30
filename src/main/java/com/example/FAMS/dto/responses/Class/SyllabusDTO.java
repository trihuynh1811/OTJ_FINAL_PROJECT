package com.example.FAMS.dto.responses.Class;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusDTO {

    private String topicCode;

    private String topicName;

    private int numberOfDay = 1;

    private String version;

    private String publishStatus;

    private String createdBy;

    private Date createdDate;
}
