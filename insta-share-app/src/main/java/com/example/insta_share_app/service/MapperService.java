package com.example.insta_share_app.service;
import com.example.insta_share_app.dtos.*;
import com.example.insta_share_app.entity.*;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
@Service
public class MapperService {
    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setProfile(toUserProfileDTO(user.getProfile()));
        return dto;
    }

    public UserProfileDTO toUserProfileDTO(UserProfile profile) {
        if (profile == null) return null;
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(profile.getId());
        dto.setUserBio(profile.getUserBio());
        dto.setProfileImageUrl(profile.getProfileImageUrl());
        dto.setFollowersCount(profile.getFollowersCount());
        dto.setFollowingCount(profile.getFollowingCount());


        if (profile.getPosts() != null)
            dto.setPosts(profile.getPosts().stream().map(this::toPostDTO).collect(Collectors.toList()));

        if (profile.getStories() != null)
            dto.setStories(profile.getStories().stream().map(this::toStoryDTO).collect(Collectors.toList()));
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setUsername(profile.getUser().getUsername());
            if (profile.getUser().getFollowers()!=null){
                dto.setFollowers(profile.getUser().getFollowers().stream()
                        .map(m->toBasicUserDTO(m.getFollower()))
                        .collect(Collectors.toList()));
            }
            if (profile.getUser().getFollowing()!=null){
                dto.setFollowing(profile.getUser().getFollowing().stream()
                        .map(m->toBasicUserDTO(m.getFollowing()))
                        .collect(Collectors.toList()));
            }
        }

        return dto;
    }
    public BasicUserDTO toBasicUserDTO(User user){
        if (user==null) return null;

        BasicUserDTO dto=new BasicUserDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
    public PostDTO toPostDTO(Post post) {
        if (post == null) return null;
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setImageUrl(post.getImageUrl());
        dto.setCaption(post.getCaption());
        dto.setLikesCount(post.getLikesCount());
        dto.setCreatedAt(post.getCreatedAt());
        if (post.getComments() != null)
            dto.setComments(post.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList()));
        // user details
        if (post.getUserProfile() != null) {
            dto.setUserId(post.getUserProfile().getUser().getId());
            dto.setUsername(post.getUserProfile().getUser().getUsername());

            if (post.getUserProfile().getProfileImageUrl() != null) {
                dto.setProfileImageUrl(post.getUserProfile().getProfileImageUrl());
            }
        }
        if (post.getLikes()!=null){
            dto.setLikes(post.getLikes().stream().map(this::toLikedPosts).collect(Collectors.toList()));
        }
        if(post.getSavedPosts()!=null){
            dto.setSaves(post.getSavedPosts().stream().map(this::toSavedList).collect(Collectors.toList()));
        }
        return dto;
    }

    public CommentDTO toCommentDTO(Comment comment) {
        if (comment == null) return null;
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setCommentText(comment.getCommentText());
        dto.setCreatedAt(comment.getCreatedAt());
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getId());
            dto.setUsername(comment.getUser().getUsername());
        }
        return dto;
    }

    public StoryDTO toStoryDTO(Story story) {
        if (story == null) return null;

        StoryDTO dto = new StoryDTO();
        dto.setId(story.getId());
        dto.setImageUrl(story.getImageUrl());
        dto.setCreatedAt(story.getCreatedAt());
        if (story.getUserProfile() != null && story.getUserProfile().getUser() != null) {
            dto.setUserId(story.getUserProfile().getUser().getId());
            dto.setUserName(story.getUserProfile().getUser().getUsername());
        }

        return dto;
    }
    public PostLikesDTO toLikedPosts(Like like){
        if (like==null) return null;
        PostLikesDTO dto=new PostLikesDTO();
        dto.setUserId(like.getUser().getId());
        dto.setUsername(like.getUser().getUsername());
        dto.setProfileImage(like.getUser().getProfileImageUrl());
        return dto;
    }
    public MySavedPostsDTO toSavedList(SavedPost savedPost){
        if (savedPost==null) return null;
        MySavedPostsDTO dto=new MySavedPostsDTO();
        dto.setUserId(savedPost.getUser().getId());
        dto.setUsername(savedPost.getUser().getUsername());
        dto.setProfileImage(savedPost.getUser().getProfileImageUrl());
        return dto;
    }
    public MyLikedPostsDTO toMyLikedPosts(Like like){
        if (like==null) return null;
        MyLikedPostsDTO dto=new MyLikedPostsDTO();
        dto.setPostId(like.getPost().getId());
        dto.setPostedById(like.getUser().getId());
        dto.setPostedByUserProfileUrl(like.getUser().getProfileImageUrl());
        dto.setImageUrl(like.getPost().getImageUrl());
        dto.setPostByUsername(like.getUser().getUsername());
        return dto;
    }
    public SavePostDTO toSavedPosts(SavedPost savedPost){
        if (savedPost==null) return null;
        SavePostDTO dto=new SavePostDTO();
        if (savedPost.getPost()!=null){
            dto.setPostId(savedPost.getPost().getId());
            dto.setImageUrl(savedPost.getPost().getImageUrl());
            dto.setPostedById(savedPost.getPost().getUserProfile().getUser().getId());
            dto.setPostByUsername(savedPost.getPost().getUserProfile().getUser().getUsername());
            dto.setPostedByUserProfileUrl(savedPost.getPost().getUserProfile().getProfileImageUrl());
        }

        return dto;
    }
}
