package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, String> {}
