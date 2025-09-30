package com.example.insta_share_app.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BanRequest {
    private String reason;
    private long DaysBanned=0;
}