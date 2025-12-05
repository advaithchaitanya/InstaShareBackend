package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.UpdateUserProfileDTO;
import com.example.insta_share_app.dtos.UserDTO;
import com.example.insta_share_app.dtos.UserProfileDTO;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.entity.UserProfile;
import com.example.insta_share_app.repositories.UserProfileRepository;
import com.example.insta_share_app.repositories.UserRepository;
import com.example.insta_share_app.service.CloudinaryImageService;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UpdateUserProfile {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    CloudinaryImageService cloudinaryImageService;
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<UserProfileDTO>updateProfile(@PathVariable String userId,
                                                        @RequestBody UpdateUserProfileDTO dto)
    {
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("not found user"));
        UserProfile userProfile=user.getProfile();
        if (dto.getFullname() !=null && !user.getUsername().equals(dto.getFullname())){
            user.setUsername(dto.getFullname());
        }
        if (dto.getUserBio() !=null && !userProfile.getUserBio().equals(dto.getUserBio())){
            userProfile.setUserBio(dto.getUserBio());
        }
        if (dto.getProfileImageUrl() !=null && !userProfile.getProfileImageUrl().equals(dto.getProfileImageUrl())){
            user.setProfileImageUrl(dto.getProfileImageUrl());
            userProfile.setProfileImageUrl(dto.getProfileImageUrl());
        }
        userRepo.save(user);
        userProfileRepository.save(userProfile);
        UserProfileDTO userDto=mapperService.toUserProfileDTO(userProfile);
        return ResponseEntity.ok(userDto);
    }
    @PutMapping("/edit-self")
    private ResponseEntity<UserDTO>updateMyProfile(Principal principal,
                                                   @RequestPart("data") UpdateUserProfileDTO dto,
                                                       @RequestPart(value = "imageFile",required = false)MultipartFile file)
    {
        User user=userRepo.findByUsername(principal.getName()).orElseThrow(()->new RuntimeException("not found user"));
        UserProfile userProfile=user.getProfile();
        if (dto.getFullname() !=null && !user.getFullName().equals(dto.getFullname())){
            user.setFullName(dto.getFullname());
        }
        if (dto.getUserBio() !=null && !userProfile.getUserBio().equals(dto.getUserBio())){
            userProfile.setUserBio(dto.getUserBio());
        }
        if (file==null && dto.getProfileImageUrl() !=null && !userProfile.getProfileImageUrl().equals(dto.getProfileImageUrl())){
            user.setProfileImageUrl(dto.getProfileImageUrl());
            userProfile.setProfileImageUrl(dto.getProfileImageUrl());
        }
        if (file!=null){
            Map data=cloudinaryImageService.upload(file);
            String uploadedUrl=(String) data.get("url");
            user.setProfileImageUrl(uploadedUrl);
            userProfile.setProfileImageUrl(uploadedUrl);
        }
        userRepo.save(user);
        userProfileRepository.save(userProfile);
        UserDTO userDto=mapperService.toUserDTO(user);
        return ResponseEntity.ok(userDto);
    }

}
