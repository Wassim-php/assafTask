package com.wassim.databseTask.auth.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.auth.payload.AuthDTO;
import com.wassim.databseTask.auth.payload.AuthResponse;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.security.JwtUtility;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Override
    public ApiResponse<AuthResponse> login(AuthDTO authRequest) {
        try {
            org.springframework.security.core.Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            String token = jwtUtility.generateToken(authRequest.getUsername());
            return new ApiResponse<AuthResponse>("Login successful", new AuthResponse(token), true);

        } catch (AuthenticationException e) {
            return new ApiResponse<>("Invalid credentials", null, false);
        } catch (Exception e) {
            return new ApiResponse<>("Something went wrong: " + e.getMessage(), null, false);
        }
    }
}
