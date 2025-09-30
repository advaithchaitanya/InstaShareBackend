package com.example.insta_share_app.dtos;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class AdminUserDTO {
    private String email;
    private String username;
    private String password;
    private String fullName;
    private String profileImageUrl;
    private Set<String> roles=new HashSet<>(Set.of("ROLE_USER"));

}
