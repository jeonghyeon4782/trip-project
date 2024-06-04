package com.dj.trip.domain.image.controller;

import com.dj.trip.domain.image.dto.UploadImageResponse;
import com.dj.trip.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public UploadImageResponse upload(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    @DeleteMapping
    public void delete(@RequestParam("fileName") String fileName) {
        imageService.deleteImage(fileName);
    }
}
