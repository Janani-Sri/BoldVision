package com.boldvision.ocr.controller;

import com.boldvision.ocr.model.ImageDetails;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.service.AuthenticationService;
import com.boldvision.ocr.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(ImageController.class);

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleImageUpload(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring("Bearer ".length()).trim();
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            User user = authenticationService.getUser(token);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            String content = imageService.extractContentFromImage(imageBytes);
//            byte[] reduced_image = ImageUtil.reduceNoise(imageBytes);
//            String boldContent = imageService.extractBoldContent(content);
            String boldContent = imageService.extractContentFromImage(imageBytes);

            ImageDetails imageDetails = new ImageDetails(base64Image, content, boldContent, user);
            ImageDetails savedImage = imageService.saveImage(imageDetails);
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("image_details", savedImage);
            return ResponseEntity.ok(jsonResponse);
        } catch (IOException e) {
            logger.error("IOException in handleImageUpload ", e);
        } catch (Exception e) {
            logger.error("Exception in handleImageUpload ", e);
        }
        return null;
    }


    @GetMapping("/display/{imageId}")
    public ResponseEntity<Map<String, Object>> displayImage(@PathVariable int imageId) {
        Optional<ImageDetails> imageDetailsOptional = imageService.getImageById(imageId);
        if (imageDetailsOptional.isPresent()) {
            ImageDetails imageDetails = imageDetailsOptional.get();

            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("imageFile", imageDetails.getImageFile());
            jsonResponse.put("content", imageDetails.getContent());
            jsonResponse.put("boldContent", imageDetails.getBoldContent());
            return ResponseEntity.ok(jsonResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/display/all")
    public ResponseEntity<Map<String, Object>> displayImage(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        return imageService.getAllImagesByUser(token);
    }
}
