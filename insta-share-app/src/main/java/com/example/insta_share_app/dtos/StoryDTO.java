package com.example.insta_share_app.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoryDTO {
    private String id;
    private String imageUrl;
    private String userId;
    private String userName;
    private LocalDateTime createdAt;
}
