package com.wassim.databseTask.auth;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.auth.service.AuthService;
import com.wassim.databseTask.global.Response.ApiResponse;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    

   @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        ApiResponse<AuthResponse> response = authService.login(authRequest);
        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
   
}


}
