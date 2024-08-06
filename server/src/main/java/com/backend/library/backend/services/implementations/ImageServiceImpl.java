package com.backend.library.backend.services.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.backend.library.backend.services.interfaces.IImageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements IImageService {

    private static final String UPLOAD_DIR = "uploads/";

    /**
     * Uploads an image to the server
     * 
     * @param file The image to upload
     * @return A ResponseEntity containing a success message with the file name
     * @throws IOException If there is an issue uploading the image
     */
    public ResponseEntity<String> uploadImage(MultipartFile file) {
        try {
            String fileName = saveImage(file);
            return ResponseEntity.ok().body("Image uploaded successfully. File name: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    /**
     * Saves the uploaded image to the server
     * 
     * @param file The image to upload
     * @return The name of the saved file
     * @throws IOException If there is an issue saving the image
     */
    private String saveImage(MultipartFile file) throws IOException {
        // Get the original file name
        String fileName = file.getOriginalFilename();

        // Create the upload path if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        Files.createDirectories(uploadPath);

        // Create a new file path with the original file name
        Path filePath = uploadPath.resolve(fileName);

        // Copy the uploaded file to the new path
        Files.copy(file.getInputStream(), filePath);

        // Return the original file name
        return fileName;
    }

}
