package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.PostDTO;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.repositories.PostRepository;
import com.example.insta_share_app.repositories.UserRepository;
import com.example.insta_share_app.service.MapperService;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class GetPosts {
    private final PostRepository postRepo;
    private final MapperService mapper;
    private UserRepository userRepository;

    public GetPosts(PostRepository postRepo, MapperService mapper) {
        this.postRepo = postRepo;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable String id) {
        User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("user not exists"));


        if (user.isCurrentlyBanned()){
            throw new RuntimeException("user is currently banned");
        }

        return postRepo.findById(id)

                .map(post -> ResponseEntity.ok(mapper.toPostDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .filter(i->!i.getUserProfile().getUser().isCurrentlyBanned())
                .map(mapper::toPostDTO)
                .toList();
    }



}
