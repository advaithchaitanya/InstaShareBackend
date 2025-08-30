package com.example.insta_share_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserStoriesDTO {
    private String userId;
    private String username;
    private List<StoryDTO> stories;
}
