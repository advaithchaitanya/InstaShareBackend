package com.example.insta_share_app.dtos.ErrorDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timeStamp;
    private int status;
    private String message;
    private String error;
    private String path;
}
