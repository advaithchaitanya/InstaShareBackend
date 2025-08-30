package com.example.insta_share_app.controllers;

//import com.example.insta_share_app.dtos.SavePostDTO;
import com.example.insta_share_app.dtos.SavePostDTO;
import com.example.insta_share_app.repositories.SavedPostRepository;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/saved")
public class GetSavedController {
    @Autowired
    SavedPostRepository savedPostRepo;
    @Autowired
    MapperService mapperService;


    @GetMapping("/my-saved")
    private ResponseEntity<List<SavePostDTO>> getSavedPosts(Principal principal){
        List<SavePostDTO> savedPosts = savedPostRepo.findAllByUser_Username(principal.getName())
                .stream()
                .map(mapperService::toSavedPosts)
                .toList();

        return ResponseEntity.ok(savedPosts);

    }
}
