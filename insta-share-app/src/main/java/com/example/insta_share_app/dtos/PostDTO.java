package com.example.insta_share_app.dtos;

//package com.example.insta_share_app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private String id;
    private String imageUrl;
    private String caption;
    private int likesCount;
    private List<CommentDTO> comments;
    private String userId;
    private String username;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private List<PostLikesDTO> likes;
    private List<MySavedPostsDTO> saves;
    private boolean isVarified;
}

