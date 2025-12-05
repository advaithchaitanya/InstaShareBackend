package com.example.insta_share_app.Exception;

import com.example.insta_share_app.dtos.ErrorDtos.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    public ResponseEntity<ErrorResponseDTO>responseBuilder(Exception ex, HttpStatus status,String path){
        ErrorResponseDTO errorBuilder=
//        new ErrorResponseDTO(
//                LocalDateTime.now(),
//                status.value(),
//                ex.getMessage(),
//                status.getReasonPhrase(),
//                path
//        );
        ErrorResponseDTO.builder()
                .timeStamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(path)
                .build();
    return new ResponseEntity<>(errorBuilder,status);
    }


    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(Exception ex, HttpServletRequest request){
        return responseBuilder(ex,HttpStatus.CONFLICT,request.getRequestURI());

    }


}
