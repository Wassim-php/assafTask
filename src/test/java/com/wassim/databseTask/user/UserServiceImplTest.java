package com.wassim.databseTask.user;


import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.UserRepository;
import com.wassim.databseTask.user.dto.UserDTO;
import com.wassim.databseTask.user.dto.UserVMCreateDTO;
import com.wassim.databseTask.user.dto.UserVMUpdateDTO;
import com.wassim.databseTask.user.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContextHolder context for getCurrentUser()
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    private UserEntity createUser(Long id, String username, String password) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Test
    void testGetCurrentUser_UserDetailsPrincipal() {
        String username = "testUser";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        UserEntity userEntity = createUser(1L, username, "encodedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetCurrentUser_StringPrincipal() {
        String username = "stringPrincipal";

        when(authentication.getPrincipal()).thenReturn(username);

        UserEntity userEntity = createUser(2L, username, "password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetCurrentUser_UserNotFound() {
        String username = "missingUser";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCurrentUser());
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testCreateUser() {
        UserVMCreateDTO dto = new UserVMCreateDTO();
        dto.setUsername("newUser");
        dto.setPassword("rawPassword");

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        UserEntity savedUser = createUser(1L, dto.getUsername(), "encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        ApiResponse<UserDTO> response = userService.create(dto);

        assertTrue(response.isState());
        assertEquals("User created successfully", response.getMessage());
        assertEquals(dto.getUsername(), response.getData().getUsername());
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> users = List.of(
                createUser(1L, "user1", "pass1"),
                createUser(2L, "user2", "pass2")
        );

        when(userRepository.findAll()).thenReturn(users);

        ApiResponse<List<UserDTO>> response = userService.getAll();

        assertTrue(response.isState());
        assertEquals(2, response.getData().size());
        assertEquals("Users retrieved successfully", response.getMessage());
    }

    @Test
    void testGetById_Found() {
        UserEntity user = createUser(1L, "user1", "pass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ApiResponse<UserDTO> response = userService.getById(1L);

        assertTrue(response.isState());
        assertEquals("User found", response.getMessage());
        assertEquals("user1", response.getData().getUsername());
    }

    @Test
    void testGetById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(1L));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void testUpdateUser() {
        UserEntity user = createUser(1L, "oldUser", "oldPass");
        UserVMUpdateDTO dto = new UserVMUpdateDTO();
        dto.setUsername("newUser");
        dto.setPassword("newPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedNewPass");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        ApiResponse<UserDTO> response = userService.update(1L, dto);

        assertTrue(response.isState());
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("newUser", response.getData().getUsername());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ApiResponse<Void> response = userService.delete(1L);

        assertTrue(response.isState());
        assertEquals("User deleted successfully", response.getMessage());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.delete(1L));

        assertTrue(ex.getMessage().contains("User not found"));
    }
}

