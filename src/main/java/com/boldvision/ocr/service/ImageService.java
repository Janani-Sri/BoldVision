package com.boldvision.ocr.service;

import com.boldvision.ocr.model.ImageDetails;
import com.boldvision.ocr.model.User;
import com.boldvision.ocr.repository.ImageRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.loadLibrary;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public ImageDetails saveImage(ImageDetails imageDetails){
        return imageRepository.save(imageDetails);
    }

    public List<ImageDetails> getAllImages(int user_id){
        return imageRepository.findByUserId(user_id);
    }

    public Optional<ImageDetails> getImageById(int id){
        return imageRepository.findById(id);
    }

    public String extractContentFromImage(byte[] image_bytes) throws IOException, TesseractException {
        Path tempFile = Files.createTempFile("tempImage", ".png");
        Files.write(tempFile, image_bytes);
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/opt/local/share/tessdata");
            tesseract.setTessVariable("user_defined_dpi", "300");
            return tesseract.doOCR(tempFile.toFile());
        } finally {
            Files.delete(tempFile);
        }
    }

    public String extractBoldContent(String content){
        Pattern boldPattern = Pattern.compile("\\b[A-Z]+\\b");

        Matcher matcher = boldPattern.matcher(content);
        StringBuilder boldWords = new StringBuilder();

        while (matcher.find()){
            boldWords.append(matcher.group()).append(" ");
        }

        return boldWords.toString().trim();
    }

    public ResponseEntity<Map<String, Object>> getAllImagesByUser(String token){
        User user = authenticationService.getUser(token);
        int user_id = user.getId();
        System.out.println("user id inside image service is :: " + user_id);
        List<ImageDetails> images = getAllImages(user_id);
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("images", images);
        return ResponseEntity.ok(jsonResponse);
    }
}
