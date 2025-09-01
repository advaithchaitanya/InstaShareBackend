package com.example.insta_share_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// User.java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;   // real name
    private String username;   // handle (unique if needed)
    private String profileImageUrl;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile profile;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();



    // getters & setters
}