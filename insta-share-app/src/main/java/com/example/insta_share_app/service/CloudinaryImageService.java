package com.example.insta_share_app.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageService {
    @Autowired
    Cloudinary cloudinary;
    public Map upload(MultipartFile multipartFile){
        Map data= null;
        try {
            data = this.cloudinary.uploader().upload(multipartFile.getBytes(), Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload");
        }
        return data;
    }
}
