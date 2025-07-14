package com.wassim.databseTask.global;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wassim.Response.ApiResponse;
import com.wassim.databseTask.global.Exceptions.BadRequestException;
import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalException {
    

    @ExceptionHandler(Exception.class)
     public ResponseEntity<Object> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());

        ex.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Object> handleAccessDenied(Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
            "error", "Access Denied",
            "message", ex.getMessage()
    ));
}

    @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
            new ApiResponse<>(ex.getMessage(), null, false),
            HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
        new ApiResponse<>(ex.getMessage(), null, false),
        HttpStatus.BAD_REQUEST
    );
}

    
}


