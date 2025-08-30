package com.example.insta_share_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Post.java
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "image_url", length = 2048)  // or even 5000 if you want
    private String imageUrl;
    private String caption;
    private int likesCount;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    @JsonIgnore
    private UserProfile userProfile;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;



    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Like> likes=new ArrayList<>();
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<SavedPost> savedPosts=new ArrayList<>();
}
