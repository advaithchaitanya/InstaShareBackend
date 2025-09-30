package com.example.insta_share_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        @Column(nullable = false)
        private boolean isVarified=false;
    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "verified_by")
    //    private User verifiedBy; // the admin/mod who verified

        @ElementCollection(fetch = FetchType.EAGER)
        private Set<String> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserProfile profile;

        @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
        private List<Follow> following = new ArrayList<>();

        @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
        private List<Follow> followers = new ArrayList<>();




    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
        private List<Ban> banHistory=new ArrayList<>();
        public boolean isCurrentlyBanned() {
            return this.banHistory != null && this.banHistory.stream()
                    .anyMatch(ban -> ban.getExpiresAt() == null || ban.getExpiresAt().isAfter(LocalDateTime.now()));
        }
        // getters & setters
    }