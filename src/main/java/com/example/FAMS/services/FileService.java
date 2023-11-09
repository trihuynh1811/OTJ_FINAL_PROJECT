package com.example.FAMS.services;

import com.amazonaws.HttpMethod;
import org.springframework.scheduling.annotation.Async;

public interface FileService {


    public String generateUrl(String fileName, HttpMethod httpMethod);

    @Async
    public String findByName(String fileName);

    @Async
    public String save(String extension);

}
