package com.example.insta_share_app.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class AllUsersDTO {
    private String userId;
    private String username;
    private String profileImageUrl;
    private Set<String> roles;
    private boolean isBanned;
    private boolean isVarified;

}
