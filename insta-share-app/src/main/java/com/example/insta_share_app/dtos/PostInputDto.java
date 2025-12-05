package com.example.insta_share_app.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class PostInputDto {
    private String imageUrl;

    private String caption;



}
