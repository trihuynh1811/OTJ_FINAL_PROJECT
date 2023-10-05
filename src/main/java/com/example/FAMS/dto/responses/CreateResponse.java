package com.example.FAMS.dto.responses;

import com.example.FAMS.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResponse {
    private String status;
    private String password;
   @JsonProperty(namespace = "created_user")
    private User createdUser;
}
