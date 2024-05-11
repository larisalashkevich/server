package com.example.sources.controller;

import com.example.sources.model.response.ErrorMessageResponse;
import com.example.sources.model.response.MessageResponse;
import com.example.sources.utils.files.FilePathResolver;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@AllArgsConstructor
@RequestMapping("/api/img")
public class ImageController {

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(FilePathResolver.getFolderPath() + imageName);
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<?> handleFileUpload(@RequestParam("image") MultipartFile file) {
        try {
            String filename = FilePathResolver.saveFile(file);
            return new ResponseEntity<>(new MessageResponse(filename), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new ErrorMessageResponse("FILE", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
