package com.example.insta_share_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyDetailsDTO {
    private String id;
    private String username;
    private String profileUrl;
}
