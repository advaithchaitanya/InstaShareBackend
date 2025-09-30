package com.example.insta_share_app.dtos;

import lombok.Data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private String id;
    private String commentText;
    private String userId;
    private String username;
    private LocalDateTime createdAt;
    private boolean isVarified;
}