package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.MyDetailsDTO;
import com.example.insta_share_app.dtos.UserDTO;
import com.example.insta_share_app.entity.Post;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.entity.UserProfile;
import com.example.insta_share_app.repositories.PostRepository;
import com.example.insta_share_app.repositories.UserProfileRepository;
import com.example.insta_share_app.repositories.UserRepository;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")

public class GetController {
    private final UserRepository userRepo;
    private final MapperService mapper;

    public GetController(UserRepository userRepo, MapperService mapper) {
        this.userRepo = userRepo;
        this.mapper = mapper;

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        return userRepo.findById(id)
                .map(user -> ResponseEntity.ok(mapper.toUserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/my-profile")
    public ResponseEntity<UserDTO> getUser(Principal principal){
        return userRepo.findByUsername(principal.getName())
                .map(user -> ResponseEntity.ok(mapper.toUserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/get-me")
    public ResponseEntity<MyDetailsDTO> getMe(Principal principal){
        return userRepo.findByUsername(principal.getName())
                .map(user->ResponseEntity.ok(MyDetailsDTO.builder().username(user.getUsername()).profileUrl(user.getProfileImageUrl()).id(user.getId()).build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(mapper::toUserDTO)
                .toList();
    }
}