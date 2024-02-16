package com.boldvision.ocr.controller;
import com.boldvision.ocr.dto.ImageUploadDto;
import com.boldvision.ocr.exceptions.CustomException;
import com.boldvision.ocr.model.ImageDetails;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.repository.UserRepository;
import com.boldvision.ocr.service.AuthenticationService;
import com.boldvision.ocr.service.ImageService;
import com.boldvision.ocr.service.UserService;
//import net.sourceforge.tess4j.TesseractException;
import com.boldvision.ocr.util.ImageUtil;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    UserService userService;
    @Autowired
    UserRepository userRepository;
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
            logger.info("extracted content is :: ", content);
            logger.info("extracted bold content is :: ", boldContent);

            ImageDetails imageDetails = new ImageDetails(base64Image, content, boldContent, user);
            ImageDetails savedImage = imageService.saveImage(imageDetails);
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("image_details", savedImage);
            return ResponseEntity.ok(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting image content" + e);
        }
        return null;
    }


    @GetMapping("/display/{imageId}")
    public ResponseEntity<Map<String, Object>> displayImage(@PathVariable int imageId) {
        Optional<ImageDetails> imageDetailsOptional = imageService.getImageById(imageId);
        if (imageDetailsOptional.isPresent()) {
            ImageDetails imageDetails = imageDetailsOptional.get();

            // Build a JSON response with image information
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("id", imageDetails.getId());
            jsonResponse.put("userId", imageDetails.getUser().getId());
            jsonResponse.put("imageFile", imageDetails.getImageFile()); // Convert to base64
            jsonResponse.put("content", imageDetails.getContent());
            jsonResponse.put("boldContent", imageDetails.getBoldContent());
            // Add other image details as needed

            return ResponseEntity.ok(jsonResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/display/all")
    public ResponseEntity<Map<String, Object>> displayImage(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("authorization header :: "+ authorizationHeader);
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        System.out.println("jananniiiiiiii token :: " + token);
        logger.info("authorization token received : ", token);
        return imageService.getAllImagesByUser(token);
    }
}
