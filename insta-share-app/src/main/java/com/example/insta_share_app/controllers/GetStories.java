package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.StoryDTO;
import com.example.insta_share_app.dtos.UserStoriesDTO;
import com.example.insta_share_app.repositories.StoryRepository;
import com.example.insta_share_app.service.MapperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/stories")
public class GetStories {
    private final StoryRepository storyRepo;
    private final MapperService mapper;

    GetStories(StoryRepository storyRepo,MapperService mapper){
        this.storyRepo = storyRepo;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryDTO> getStory(@PathVariable String id) {
        return storyRepo.findById(id)
                .map(story -> ResponseEntity.ok(mapper.toStoryDTO(story)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<StoryDTO> getAllStories() {
        return storyRepo.findAll().stream()
                .filter(i -> i.getUserProfile() != null
                        && i.getUserProfile().getUser() != null
                        && !i.getUserProfile().getUser().isCurrentlyBanned())
                .map(mapper::toStoryDTO)
                .toList();
    }
    @GetMapping("/all")
    public List<UserStoriesDTO> getAllStoriesByGroup() {
        List<StoryDTO> allStories= storyRepo.findAll().stream()
                .filter(i -> i.getUserProfile() != null
                        && i.getUserProfile().getUser() != null
                        && !i.getUserProfile().getUser().isCurrentlyBanned())
                .map(mapper::toStoryDTO)
                .toList();

        return allStories.stream()
                .collect(Collectors.groupingBy(StoryDTO::getUserId))
                .entrySet().stream()
                .map(e->{
                    List<StoryDTO> userStories=e.getValue();
                    StoryDTO firstStory=userStories.get(0);

                    UserStoriesDTO dto=new UserStoriesDTO();
                    dto.setUserId(firstStory.getUserId());
                    dto.setUsername(firstStory.getUserName());
                    dto.setStories(userStories);
                    return dto;
                }).toList();
    }

}
