package com.wassim.databseTask.auth.service;

import com.wassim.databseTask.auth.AuthRequest;
import com.wassim.databseTask.auth.AuthResponse;
import com.wassim.databseTask.global.Response.ApiResponse;

public interface AuthService {
    ApiResponse<AuthResponse> login(AuthRequest authRequest);
}
