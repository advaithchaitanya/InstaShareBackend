package com.example.insta_share_app.dtos;



import lombok.Data;

@Data
public class UpdateUserProfileDTO {
        private String userBio;
    private String profileImageUrl;
    private String fullname;
    // add more fields if you want users to update them
}