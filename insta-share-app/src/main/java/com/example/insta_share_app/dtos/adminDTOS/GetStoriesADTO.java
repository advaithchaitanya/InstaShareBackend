package com.example.insta_share_app.dtos.adminDTOS;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetStoriesADTO {
    private String id;
    private String imageUrl;
    private String userId;
    private String userName;
    private String profileImageUrl;
    private boolean userIsBanned;
    private boolean isVarified;
    private LocalDateTime createdAt;
}
