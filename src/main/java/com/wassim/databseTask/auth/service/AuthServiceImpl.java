package com.wassim.databseTask.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.auth.payload.AuthDTO;
import com.wassim.databseTask.auth.payload.AuthResponse;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.security.JwtUtility;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public ApiResponse<AuthResponse> login(AuthDTO authRequest) {

        org.springframework.security.core.Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        String token = jwtUtility.generateToken(authRequest.getUsername());
        String refreshToken = jwtUtility.generateRefreshToken(authRequest.getUsername());
        return new ApiResponse<AuthResponse>("Login successful",
                new AuthResponse(token, refreshToken, "Log in successfull"), true);

    }

    public ApiResponse<AuthResponse> refresh(Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return new ApiResponse<>("Refresh token is required", null, false);
        }

        String username = jwtUtility.extractUsername(refreshToken);

        if (jwtUtility.isTokenValid(refreshToken, username)) {
            String newAccessToken = jwtUtility.generateToken(username);

            AuthResponse response = new AuthResponse(newAccessToken, refreshToken, "Refresh successful");

            return new ApiResponse<>("Refreshed successfully", response, true);
        } else {
            return new ApiResponse<>("Invalid refresh token", null, false);
        }

    }

}
