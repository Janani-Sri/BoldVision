package com.boldvision.ocr.service;

import com.boldvision.ocr.model.ImageDetails;
import com.boldvision.ocr.repository.ImageRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.loadLibrary;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void saveImage(ImageDetails imageDetails){
        imageRepository.save(imageDetails);
    }

    public List<ImageDetails> getAllImages(int user_id){
        return imageRepository.findAllImagesByUserId(user_id);
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
}
