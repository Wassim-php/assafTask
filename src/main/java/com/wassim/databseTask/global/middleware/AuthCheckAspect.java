package com.wassim.databseTask.global.middleware;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Before;

import com.wassim.databseTask.global.Exceptions.UnauthorizedException;
import com.wassim.databseTask.global.annotations.RequireLogin;

import com.wassim.databseTask.user.UserServiceImpl;

@Aspect
@Component
public class AuthCheckAspect {
    
    @Autowired
    private UserServiceImpl userService;


    @Before("@annotation(requireLogin)")
    public void checkIfLoggedIn(RequireLogin requireLogin){
        if(userService.getCurrentUser() == null) {
            throw new UnauthorizedException("You must be logged in to access this resource");
        }
    }
}
