package com.example.FAMS.controllers;

import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileControlller {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> uploadTrainingMaterial(@RequestBody MultipartFile file) {
        ResponseObject response = ResponseObject.builder().build();
        try {
            response = fileService.uploadTrainingMaterial(file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status("Fail")
                    .message(e.getMessage())
                    .build());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadTrainingMaterial(
            @PathVariable(name = "fileName") String fileName
    ) {
        ResponseObject response = ResponseObject.builder().build();
        try {
            byte[] data = fileService.downloadTrainingMaterials(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(data);
            return ResponseEntity.ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header(
                            "Content-disposition",
                            "attachment; filename=\"" + fileName + "\""
                    )
                    .body(byteArrayResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fail to download file");
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<ResponseObject> deleteTrainingMaterial(
            @PathVariable(name = "fileName") String fileName
    ) {
        return ResponseEntity.ok(fileService.deleteTrainingMaterial(fileName));
    }
}
