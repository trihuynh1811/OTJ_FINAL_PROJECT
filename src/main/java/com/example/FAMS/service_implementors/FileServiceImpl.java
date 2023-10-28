package com.example.FAMS.service_implementors;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Override
    public ResponseObject uploadTrainingMaterial(MultipartFile file) throws IOException {
        File fileObj = convertMultipartFileToFile(file);
        s3Client.putObject(new PutObjectRequest(bucketName, fileObj.getName(), fileObj));
        fileObj.delete();
        return ResponseObject.builder()
                .status("Successful")
                .message("File " + fileObj.getName() + " uploaded")
                .build();
    }

    @Override
    public byte[] downloadTrainingMaterials(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        byte[] content = IOUtils.toByteArray(inputStream);
        return content;
    }

    @Override
    public ResponseObject deleteTrainingMaterial(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return ResponseObject.builder()
                .status("Success")
                .message("File " + fileName + " removed.")
                .build();
    }


    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(multipartFile.getBytes());
        return convertedFile;
    }
}
