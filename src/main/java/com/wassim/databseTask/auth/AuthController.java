package com.wassim.databseTask.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.auth.payload.AuthDTO;
import com.wassim.databseTask.auth.payload.AuthResponse;
import com.wassim.databseTask.auth.service.AuthService;
import com.wassim.databseTask.global.Response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO authRequestDTO) {

        ApiResponse<AuthResponse> response = authService.login(authRequestDTO);
        return ResponseEntity.status(response.isState() ? 200 : 401).body(response);

    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.refresh(request));

    }

}
