package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String>
{
    List<Post> findByCaptionOrUserProfileUserUsername(String caption, String username);
}
