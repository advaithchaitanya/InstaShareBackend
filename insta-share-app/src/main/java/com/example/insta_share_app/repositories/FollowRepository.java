package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Follow;
import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, String> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    Optional<Follow>findByFollowerAndFollowing(User follower,User following);
}
