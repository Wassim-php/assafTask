package com.wassim.databseTask.user;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetails;

import com.wassim.databseTask.Response.ApiResponse;

public interface UserService {
    public UserDetails loadUserByUsername(String username);
    ApiResponse<UserDTO> create(UserDTO userDTO);
    ApiResponse<List<UserDTO>> getAll();
    ApiResponse<UserDTO> getById(Long id);
    ApiResponse<UserDTO> update(Long id, UserDTO userDTO);
    ApiResponse<Void> delete(Long id);
}
