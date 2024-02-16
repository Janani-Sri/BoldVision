package com.boldvision.ocr.dto;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadDto {
    private MultipartFile imageFile;
    private int userId;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
