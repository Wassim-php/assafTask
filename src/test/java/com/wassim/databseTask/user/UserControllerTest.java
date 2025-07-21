package com.wassim.databseTask.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.security.JwtUtility;
import com.wassim.databseTask.user.dto.UserDTO;
import com.wassim.databseTask.user.dto.UserVMCreateDTO;
import com.wassim.databseTask.user.dto.UserVMUpdateDTO;
import com.wassim.databseTask.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtility jwtUtility;

    private UserDTO sampleUserDTO;

    @BeforeEach
    void setup() {
        sampleUserDTO = new UserDTO();
        sampleUserDTO.setId(1L);
        sampleUserDTO.setUsername("testUser");
    }

    @Test
    void testCreateUser() throws Exception {
        UserVMCreateDTO createDTO = new UserVMCreateDTO();
        createDTO.setUsername("testUser");
        createDTO.setPassword("password");

        ApiResponse<UserDTO> response = new ApiResponse<>("User created successfully", sampleUserDTO, true);
        when(userService.create(any(UserVMCreateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.state").value(true))
                .andExpect(jsonPath("$.data.username").value("testUser"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        ApiResponse<List<UserDTO>> response = new ApiResponse<>("Users retrieved successfully", List.of(sampleUserDTO),
                true);
        when(userService.getAll()).thenReturn(response);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                .andExpect(jsonPath("$.state").value(true))
                .andExpect(jsonPath("$.data[0].username").value("testUser"));
    }

    @Test
    void testGetById() throws Exception {
        ApiResponse<UserDTO> response = new ApiResponse<>("User found", sampleUserDTO, true);
        when(userService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User found"))
                .andExpect(jsonPath("$.state").value(true))
                .andExpect(jsonPath("$.data.username").value("testUser"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserVMUpdateDTO updateDTO = new UserVMUpdateDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setPassword("newPassword");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setUsername("updatedUser");

        ApiResponse<UserDTO> response = new ApiResponse<>("User updated successfully", updatedUserDTO, true);
        when(userService.update(eq(1L), any(UserVMUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.state").value(true))
                .andExpect(jsonPath("$.data.username").value("updatedUser"));
    }

    @Test
    void testDeleteUser() throws Exception {
        ApiResponse<Void> response = new ApiResponse<>("User deleted successfully", null, true);
        when(userService.delete(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/users/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.state").value(true));
    }
}
