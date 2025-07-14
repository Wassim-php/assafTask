package com.wassim.databseTask.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements  UserDetailsService{


        @Autowired
        private UserRepository userRepository;

         
        public UserEntity getCurrentUser(){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if( principal instanceof UserDetails){
                username = ((UserDetails) principal).getUsername();
            }else{
                username = principal.toString();
            }

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }



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