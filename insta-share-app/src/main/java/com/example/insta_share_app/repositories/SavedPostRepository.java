package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Post;
import com.example.insta_share_app.entity.SavedPost;
import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedPostRepository extends JpaRepository<SavedPost, String> {
    boolean existsByPostAndUser(Post post, User user);
    List<SavedPost> findAllByUser_Id(String userId);
    List<SavedPost> findAllByUser_Username(String userId);
    Optional<SavedPost>findByPostAndUser(Post post,User user);
}
