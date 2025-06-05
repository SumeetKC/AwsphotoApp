package com.example.photoapp.controller;

import com.example.photoapp.service.PhotoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/")
    public String home() {
        return "Application is running!";
    }

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("userId") String userId,
                              @RequestParam("file") MultipartFile file) throws IOException {
        return photoService.uploadPhoto(userId, file.getOriginalFilename(), file.getBytes());
    }
}