    package com.example.insta_share_app.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;

    import java.util.ArrayList;
    import java.util.List;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    // UserProfile.java
    @Entity
    @Table(name = "user_profiles")
    public class UserProfile {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        private String userBio;
        private String profileImageUrl;

        private int followersCount;
        private int followingCount;

        @OneToOne
        @JoinColumn(name = "user_id",unique = true)
        @JsonIgnore
        private User user;
        @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
        private List<Post> posts = new ArrayList<>();

        @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
        private List<Story> stories = new ArrayList<>();


    }
