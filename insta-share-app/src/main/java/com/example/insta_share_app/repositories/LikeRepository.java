package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Like;
import com.example.insta_share_app.entity.Post;
import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, String> {
       boolean existsByPostAndUser(Post post, User user);
       List<Like> findAllByUser_Username(String username);
       Optional<Like> findAllByUser_Id(String userId);
       Optional<Like> findByPostAndUser(Post post, User user);
}