package com.wassim.databseTask.user.service;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetails;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.user.dto.UserDTO;
import com.wassim.databseTask.user.dto.UserVMCreateDTO;
import com.wassim.databseTask.user.dto.UserVMUpdateDTO;

public interface UserService {
    public UserDetails loadUserByUsername(String username);
    ApiResponse<UserDTO> create(UserVMCreateDTO userDTO);
    ApiResponse<List<UserDTO>> getAll();
    ApiResponse<UserDTO> getById(Long id);
    ApiResponse<UserDTO> update(Long id, UserVMUpdateDTO userDTO);
    ApiResponse<Void> delete(Long id);
}
