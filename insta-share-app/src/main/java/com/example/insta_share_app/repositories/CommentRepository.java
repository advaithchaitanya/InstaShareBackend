package com.example.insta_share_app.repositories;

import com.example.insta_share_app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {}
