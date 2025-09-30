package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User>findByUsername(String username);
//    User findByUsername2(String username);
//    List<User> findByIsBannedAndBanExpiresAtBefore(boolean isBanned,LocalDateTime now);
}
