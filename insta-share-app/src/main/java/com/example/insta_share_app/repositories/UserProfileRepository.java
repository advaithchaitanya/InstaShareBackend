package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
//    Optional<UserProfile>findById()
}
