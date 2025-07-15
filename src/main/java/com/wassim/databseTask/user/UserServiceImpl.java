package com.wassim.databseTask.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.Response.ApiResponse;
import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;


@Service
public class UserServiceImpl implements  UserDetailsService, UserService{


        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

         
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


        private UserDTO mapTo(UserEntity user) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            return dto;
        }

        private UserEntity mapFrom(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword()); 
        return entity;
        }


        @Override
        public ApiResponse<UserDTO> create(UserDTO userDTO) {
        UserEntity user = mapFrom(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        return new ApiResponse<>("User created successfully", mapTo(savedUser), true);
        }    
        

         @Override
        public ApiResponse<List<UserDTO>> getAll() {
        List<UserEntity> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream().map(this::mapTo).toList();
        return new ApiResponse<>("Users retrieved successfully", userDTOS, true);
        }

        @Override
        public ApiResponse<UserDTO> getById(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new ApiResponse<>("User found", mapTo(user), true);
        }


         @Override
        public ApiResponse<UserDTO> update(Long id, UserDTO userDTO) {
        UserEntity existing = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existing.setUsername(userDTO.getUsername());
        existing.setPassword(userDTO.getPassword()); 

        UserEntity updated = userRepository.save(existing);
        return new ApiResponse<>("User updated successfully", mapTo(updated), true);
        }

        @Override
        public ApiResponse<Void> delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        return new ApiResponse<>("User deleted successfully", null, true);
    }








}