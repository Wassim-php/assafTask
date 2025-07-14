package com.wassim.databseTask.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.UserRepository;


@Service
public class UserServiceImpl implements  UserDetailsService{


        @Autowired
        private UserRepository userRepository;

         


        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
            System.out.println("Loading user by username: " +username);
            UserEntity user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>()
            );                    
        }
}