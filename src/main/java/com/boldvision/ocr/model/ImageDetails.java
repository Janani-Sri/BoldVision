package com.boldvision.ocr.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ImageDetails")
public class ImageDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Lob
    @Column(name = "image_file",length = Integer.MAX_VALUE)
    private String imageFile;
    @Lob
    @Column(length = Integer.MAX_VALUE)
    private String content;
    @Lob
    @Column(name = "bold_content", length = Integer.MAX_VALUE)
    private String boldContent;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ImageDetails() {
    }

    public ImageDetails(String imageFile, String content, String boldContent, User user) {
        this.imageFile = imageFile;
        this.content = content;
        this.boldContent = boldContent;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoldContent() {
        return boldContent;
    }

    public void setBoldContent(String boldContent) {
        this.boldContent = boldContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
