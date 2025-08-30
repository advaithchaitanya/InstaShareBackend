package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.CommentDTO;
import com.example.insta_share_app.dtos.PostDTO;
import com.example.insta_share_app.entity.*;
import com.example.insta_share_app.repositories.*;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final LikeRepository likeRepo;
    private final SavedPostRepository savedPostRepo;
    private final FollowRepository followRepo;
    private final StoryRepository storyRepo;
    private final MapperService mapper;

    @Autowired
    public PostController(PostRepository postRepo,
                          UserRepository userRepo,
                          LikeRepository likeRepo,
                          SavedPostRepository savedPostRepo,
                          FollowRepository followRepo,
                          StoryRepository storyRepo,
                          MapperService mapper) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.likeRepo = likeRepo;
        this.savedPostRepo = savedPostRepo;
        this.followRepo = followRepo;
        this.storyRepo = storyRepo;
        this.mapper = mapper;
    }

    // ------------------------ Post ------------------------
    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@RequestBody Post post, Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUserProfile(user.getProfile());
        post.setLikesCount(0); // initialize
        Post saved = postRepo.save(post);

        return ResponseEntity.ok(mapper.toPostDTO(saved));
    }

    // ------------------------ Like ------------------------
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId, Principal principal) {
        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (likeRepo.existsByPostAndUser(post, user)) {
            return ResponseEntity.badRequest().body("Already liked!");
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        likeRepo.save(like);

        post.setLikesCount(post.getLikesCount() + 1);
        postRepo.save(post);

        return ResponseEntity.ok(mapper.toLikedPosts(like));
    }

    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable String postId, Principal principal) {
        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = likeRepo.findByPostAndUser(post, user)
                .orElseThrow(() -> new RuntimeException("You haven’t liked this post"));

        likeRepo.delete(like);

        post.setLikesCount(post.getLikesCount() - 1);
        postRepo.save(post);

        return ResponseEntity.ok(mapper.toPostDTO(post));
    }

    // ------------------------ Save ------------------------
    @PostMapping("/{postId}/save")
    public ResponseEntity<?> savePost(@PathVariable String postId, Principal principal) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (savedPostRepo.existsByPostAndUser(post, user)) {
            return ResponseEntity.badRequest().body("Post already saved!");
        }

        SavedPost savedPost = SavedPost.builder()
                .post(post)
                .user(user)
                .build();
        savedPostRepo.save(savedPost);

        return ResponseEntity.ok(mapper.toSavedPosts(savedPost));
    }
    @DeleteMapping("/{postId}/unsave")
    public ResponseEntity<?> unsavePost(@PathVariable String postId, Principal principal) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedPost savedPost = savedPostRepo.findByPostAndUser(post, user)
                .orElseThrow(() -> new RuntimeException("This post is not saved by you"));

        savedPostRepo.delete(savedPost);

        return ResponseEntity.ok(mapper.toSavedPosts(savedPost));
    }
    // ------------------------ Comment ------------------------
    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentDTO> comment(@PathVariable String postId,
                                              @RequestBody Comment text,
                                              Principal principal) {
        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentText(text.getCommentText());

        post.getComments().add(comment);
        Post saved = postRepo.save(post);

        return ResponseEntity.ok(mapper.toCommentDTO(comment));
    }

    // ------------------------ Follow ------------------------
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<?> follow(@PathVariable String followingId, Principal principal) {
        User follower = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepo.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (follower.getId().equals(following.getId())) {
            return ResponseEntity.badRequest().body("You cannot follow yourself!");
        }

        if (followRepo.existsByFollowerAndFollowing(follower, following)) {
            return ResponseEntity.badRequest().body("Already following!");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepo.save(follow);

        UserProfile followerProfile = follower.getProfile();
        UserProfile followingProfile = following.getProfile();

        followerProfile.setFollowingCount(followerProfile.getFollowingCount() + 1);
        followingProfile.setFollowersCount(followingProfile.getFollowersCount() + 1);

        userRepo.save(follower);
        userRepo.save(following);

        return ResponseEntity.ok(mapper.toBasicUserDTO(follower));
    }

    @DeleteMapping("/unfollow/{followingId}")
    public ResponseEntity<?> unfollow(@PathVariable String followingId, Principal principal) {
        User follower = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepo.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        Follow follow = followRepo.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Not following this user"));

        followRepo.delete(follow);

        UserProfile followerProfile = follower.getProfile();
        UserProfile followingProfile = following.getProfile();

        followerProfile.setFollowingCount(followerProfile.getFollowingCount() - 1);
        followingProfile.setFollowersCount(followingProfile.getFollowersCount() - 1);

        userRepo.save(follower);
        userRepo.save(following);

        return ResponseEntity.ok(mapper.toBasicUserDTO(follower));
    }

    // ------------------------ Story ------------------------
    @PostMapping("/story")
    public ResponseEntity<?> postStory(@RequestBody Story story, Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        story.setUserProfile(user.getProfile());
        storyRepo.save(story);

        return ResponseEntity.ok(mapper.toStoryDTO(story));
    }
}