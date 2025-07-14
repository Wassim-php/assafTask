package com.wassim.databseTask.security;

public class AuthResponse {
    String token;


    public AuthResponse(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }


}
