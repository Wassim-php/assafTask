package com.wassim.databseTask.auth.service;

import java.util.Map;

import com.wassim.databseTask.auth.payload.AuthDTO;
import com.wassim.databseTask.auth.payload.AuthResponse;
import com.wassim.databseTask.global.Response.ApiResponse;

public interface AuthService {
    ApiResponse<AuthResponse> login(AuthDTO authRequest);

    ApiResponse<AuthResponse> refresh(Map<String, String> request);
}
