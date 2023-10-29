package com.example.FAMS.services;

import com.example.FAMS.dto.responses.ResponseObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    ResponseObject uploadTrainingMaterial(MultipartFile file) throws IOException;

    byte[] downloadTrainingMaterials(String fileName) throws IOException;

    ResponseObject deleteTrainingMaterial(String fileName);
}
