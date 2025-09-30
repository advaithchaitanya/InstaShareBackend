package com.example.insta_share_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ban
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_user_id", nullable = false) // the banned user
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id",nullable = false)
    private User bannedBy; // maybe store adminId
    private LocalDateTime bannedAt;
    private LocalDateTime expiresAt;
}
