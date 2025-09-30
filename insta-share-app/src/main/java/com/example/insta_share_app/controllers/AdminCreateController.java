package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.AdminUserDTO;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.entity.UserProfile;
import com.example.insta_share_app.repositories.UserProfileRepository;
import com.example.insta_share_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminCreateController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserProfileRepository userProfileRepository;
    @PostMapping("/create-admin")
    ResponseEntity<AdminUserDTO> createAdmin(@RequestBody AdminUserDTO request ){

        Optional<User> isExist=userRepository.findByUsername(request.getUsername());
        if (isExist.isPresent()){
            throw new RuntimeException("User Already exists");
        }
        User newAdmin=new User();
        newAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<String> roles = request.getRoles();
        roles.add("ROLE_ADMIN");
        newAdmin.setRoles(roles);
        request.setRoles(newAdmin.getRoles());
        newAdmin.setUsername(request.getUsername());
        newAdmin.setFullName(request.getFullName());
        newAdmin.setEmail(request.getEmail());
        newAdmin.setProfileImageUrl(request.getProfileImageUrl()!=null? request.getProfileImageUrl() : "https://imgs.search.brave.com/gI1LMTLDwf4Ea16M4trLCUCHEj8sGFY2nbEIFGFDXEA/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9jZG4u/dmVjdG9yc3RvY2su/Y29tL2kvNTAwcC8x/My8xNy9tYWxlLWF2/YXRhci1wcm9maWxl/LXBpY3R1cmUtZ29s/ZC1tZW1iZXItc2ls/aG91ZXR0ZS12ZWN0/b3ItNTM1MTMxNy5q/cGc");
        request.setProfileImageUrl(newAdmin.getProfileImageUrl());
        UserProfile profile = new UserProfile();
        profile.setUser(newAdmin);
        profile.setUserBio("hey new Here!!");
        profile.setProfileImageUrl(newAdmin.getProfileImageUrl());
        userRepository.save(newAdmin);
        userProfileRepository.save(profile);
        return ResponseEntity.ok(request);

    }

}
