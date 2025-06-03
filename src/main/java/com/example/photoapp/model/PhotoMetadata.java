package com.example.photoapp.model;

public class PhotoMetadata {
    private String photoId;
    private String userId;
    private String uploadTime;
    private String fileName;

    // Getters and setters
    public String getPhotoId() { return photoId; }
    public void setPhotoId(String photoId) { this.photoId = photoId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUploadTime() { return uploadTime; }
    public void setUploadTime(String uploadTime) { this.uploadTime = uploadTime; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
}