package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.MyLikedPostsDTO;
import com.example.insta_share_app.repositories.LikeRepository;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/likes")
public class GetLikeController {
    @Autowired
    private LikeRepository likeRepo;
    @Autowired
    private MapperService mapper;

    @GetMapping("/my-likes")
    public List<MyLikedPostsDTO> getMyLikes( Principal principal) {
        return likeRepo.findAllByUser_Username(principal.getName())
                .stream()
                .map(mapper::toMyLikedPosts)
                .toList();
    }
}
