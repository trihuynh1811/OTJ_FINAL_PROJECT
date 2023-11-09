package com.example.FAMS.dto.responses.Syllabus;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlResponse {
    int status;
    String message;
    List<String> putPresignedUrl;
    List<String> getPresignedUrl;
}
