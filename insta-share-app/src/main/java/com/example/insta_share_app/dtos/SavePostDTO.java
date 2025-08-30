package com.example.insta_share_app.dtos;

import lombok.Data;

@Data
public class SavePostDTO {
    private String postId;
    private String imageUrl;
    private String postedById;
    private String postByUsername;
    private String postedByUserProfileUrl;
}
