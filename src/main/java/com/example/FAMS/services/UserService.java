package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ResposeObject;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResposeObject> getAll();
}
