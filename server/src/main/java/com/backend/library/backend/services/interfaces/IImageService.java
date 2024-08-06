package com.backend.library.backend.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    public ResponseEntity<String> uploadImage(MultipartFile file);
}
