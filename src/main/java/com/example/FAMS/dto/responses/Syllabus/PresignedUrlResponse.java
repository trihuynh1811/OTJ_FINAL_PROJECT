package com.example.FAMS.dto.responses.Syllabus;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresignedUrlResponse {

    String id;
    List<String> putPresignedUrl;
    List<String> getPresignedUrl;
}
