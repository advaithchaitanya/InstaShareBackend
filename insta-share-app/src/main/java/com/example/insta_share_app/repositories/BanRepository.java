package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Ban;
import com.example.insta_share_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban,String > {
    Optional<Ban> findByUserId(String userid);

//    void delete(Optional<Ban> ban);
}
