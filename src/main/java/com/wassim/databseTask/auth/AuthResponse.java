package com.wassim.databseTask.auth;

public class AuthResponse {
    String token;


    public AuthResponse(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }


}
