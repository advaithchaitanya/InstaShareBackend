package com.example.insta_share_app.dtos;


import lombok.Data;

import java.util.List;
@Data

public class UserDTO {
    private String id;
    private String email;
    private String username;
    private String fullName;
    private UserProfileDTO profile;
    private boolean isBanned;
    private boolean isVarified;
    private BasicUserDTO isVarifiedBy;
}