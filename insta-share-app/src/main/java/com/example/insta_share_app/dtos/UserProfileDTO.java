package com.example.insta_share_app.dtos;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileDTO {
    private String id;
    private String userBio;
    private String profileImageUrl;
    private int followersCount;
    private int followingCount;

    private List<PostDTO> posts;
    private List<StoryDTO> stories;
    private List<BasicUserDTO> followers;
    private List<BasicUserDTO> following;

    private String username;
    private String userId;
}