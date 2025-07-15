package com.wassim.databseTask.auth.service;

import com.wassim.databseTask.auth.payload.AuthDTO;
import com.wassim.databseTask.auth.payload.AuthResponse;
import com.wassim.databseTask.global.Response.ApiResponse;

public interface AuthService {
    ApiResponse<AuthResponse> login(AuthDTO authRequest);
}
