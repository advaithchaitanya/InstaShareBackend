package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User>findByUsername(String username);
}
