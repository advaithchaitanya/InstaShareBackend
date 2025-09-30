package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.AllUsersDTO;
import com.example.insta_share_app.dtos.BasicUserDTO;
import com.example.insta_share_app.dtos.MyDetailsDTO;
import com.example.insta_share_app.dtos.UserDTO;
import com.example.insta_share_app.dtos.adminDTOS.GetCommentsADTO;
import com.example.insta_share_app.dtos.adminDTOS.GetPostsADTO;
import com.example.insta_share_app.dtos.adminDTOS.GetStoriesADTO;
import com.example.insta_share_app.entity.*;
import com.example.insta_share_app.repositories.*;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")

public class GetController {
    private final UserRepository userRepo;
    private final MapperService mapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    StoryRepository storyRepository;
    @Autowired
    CommentRepository commentRepository;

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
                .map(user->ResponseEntity.ok(MyDetailsDTO.builder().username(user.getUsername()).profileImageUrl(user.getProfileImageUrl()).id(user.getId()).isBanned(user.isCurrentlyBanned()).roles(user.getRoles()).build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(m->m.getBanHistory().stream().noneMatch(i->i.getBannedBy()==null|| i.getExpiresAt().isAfter(LocalDateTime.now())))
                .map(mapper::toUserDTO)
                .toList();
    }
    @GetMapping("/admin/get-all-users")
    public ResponseEntity<List<AllUsersDTO>> getAllUsersForAdmin() {
        return ResponseEntity.ok(
                userRepo.findAll().stream()
                        .filter(user -> user.getRoles().stream()
                                .noneMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_OWNER")))
                        .map(mapper::toAllUserDTO)
                        .toList()
        );
    }
    @GetMapping("/admin/get-admin-users")
    public ResponseEntity<List<AllUsersDTO>> getAllAdmins() {
        return ResponseEntity.ok(
                userRepo.findAll().stream()
                        .filter(user -> user.getRoles().stream()
                                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_OWNER")))
                        .map(mapper::toAllUserDTO)
                        .toList()
        );
    }
    @GetMapping("/admin/get-banned-users")
    public ResponseEntity<List<AllUsersDTO>> getAllBannedUsers() {
        return ResponseEntity.ok(
                userRepo.findAll().stream()
                        .filter(User::isCurrentlyBanned)
                        .map(mapper::toAllUserDTO)
                        .toList()
        );
    }
    @GetMapping("/admin/get-posts")
    public ResponseEntity<List<GetPostsADTO>> getAllPosts() {
        return ResponseEntity.ok(
                postRepository.findAll().stream()
                        .map(mapper::getPostsADTO)
                        .toList()
        );
    }
    @GetMapping("/admin/get-stories")
    public ResponseEntity<List<GetStoriesADTO>> getAllStories() {
        return ResponseEntity.ok(
                storyRepository.findAll().stream()
                        .map(mapper::getStoriesForAdmin)
                        .toList()
        );
    }
    @GetMapping("/admin/get-comments")
    public ResponseEntity<List<GetCommentsADTO>> getCommentsForAdmin() {
        return ResponseEntity.ok(
                commentRepository.findAll().stream()
                        .map(mapper::getCommentsADTO)
                        .toList()
        );
    }
    @GetMapping("/admin/stats/users")
    public ResponseEntity<Map<String, Long>> getStats() {
        List<User> users = userRepo.findAll();
        long totalUsers = users.stream()
                .filter(u -> u.getRoles().stream().noneMatch(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_OWNER")))
                .count();
        long totalAdmins = users.stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.equals("ROLE_ADMIN") || r.equals("ROLE_OWNER")))
                .count();
        long totalBanned = users.stream()
                .filter(User::isCurrentlyBanned)
                .count();
        List<Post> posts=postRepository.findAll();
        List<Story> stories=storyRepository.findAll();
        List<Comment> comments=commentRepository.findAll();
        long totalPosts=posts.stream()
                .filter(i->!i.getUserProfile().getUser().isCurrentlyBanned())
                .count();
        long totalStories=stories.stream()
                .filter(i->!i.getUserProfile().getUser().isCurrentlyBanned())
                .count();
        long totalComments=comments.stream()
                .filter(i->!i.getUser().isCurrentlyBanned())
                .count();
        Map<String, Long> stats = Map.of(
                "totalUsers", totalUsers,
                "totalAdmins", totalAdmins,
                "totalBanned", totalBanned,
                "totalPosts", totalPosts,
                "totalStories", totalStories,
                "totalComments", totalComments
        );

        return ResponseEntity.ok(stats);
    }

}