package com.dj.trip.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageServiceUtils {
    String upload(MultipartFile file);

    void deleteImage(String fileName);

    String getImageUrl(String fileName);
}
