package com.wassim.databseTask.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;
import com.wassim.databseTask.post.service.PostService;
import com.wassim.databseTask.security.JwtUtility;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtility jwtUtility;

    @MockitoBean
    private UserServiceImpl userService;

    private PostDTO postDTO;

    @BeforeEach
    void setUp() {
        postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setTitle("Test Post");
        postDTO.setDescription("Post description");
        postDTO.setUserId(2L);
        postDTO.setLikedUsers(Set.of(3L));
    }

    @Test
    void testCreatePost() throws Exception {
        PostVMCreateDTO createDTO = new PostVMCreateDTO();
        createDTO.setTitle("Test Post");
        createDTO.setDescription("Post description");

        ApiResponse<PostDTO> response = new ApiResponse<>("Post created", postDTO, true);

        Mockito.when(postService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/post/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(postDTO.getId()))
                .andExpect(jsonPath("$.data.title").value(postDTO.getTitle()));
    }

    @Test
    void testGetPostById() throws Exception {
        ApiResponse<PostDTO> response = new ApiResponse<>("Post found", postDTO, true);

        Mockito.when(postService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(postDTO.getId()))
                .andExpect(jsonPath("$.data.title").value(postDTO.getTitle()));
    }

    @Test
    void testGetAllPosts() throws Exception {
        Page<PostDTO> postPage = new PageImpl<>(List.of(postDTO), PageRequest.of(0, 10), 1);
        ApiResponse<Page<PostDTO>> response = new ApiResponse<>("Fetched", postPage, true);

        Mockito.when(postService.getAll(0, 10)).thenReturn(response);

        mockMvc.perform(get("/api/post/all?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(postDTO.getId()));
    }

    @Test
    void testUpdatePost() throws Exception {
        PostVMUpdateDTO updateDTO = new PostVMUpdateDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Desc");

        PostDTO updatedPost = new PostDTO();
        updatedPost.setId(1L);
        updatedPost.setTitle("Updated Title");
        updatedPost.setDescription("Updated Desc");
        updatedPost.setUserId(2L);
        updatedPost.setLikedUsers(Set.of(3L));

        ApiResponse<PostDTO> response = new ApiResponse<>("Post updated", updatedPost, true);

        Mockito.when(postService.update(eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title"));
    }

    @Test
    void testDeletePost() throws Exception {
        ApiResponse<Void> response = new ApiResponse<>("Deleted", null, true);

        Mockito.when(postService.delete(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/post/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deleted"));
    }

    @Test
    void testLikePost() throws Exception {
        
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        
        Mockito.when(userService.getCurrentUser()).thenReturn(mockUser);

        ApiResponse<Object> expectedResponse = new ApiResponse<>("Post liked", null, true);
        Mockito.when(postService.likePost(1L)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/post/1/like"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Post liked"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void testSearchPosts() throws Exception {
        Page<PostDTO> postPage = new PageImpl<>(List.of(postDTO), PageRequest.of(0, 10), 1);
        ApiResponse<Page<PostDTO>> response = new ApiResponse<>("Search results", postPage, true);

        Mockito.when(postService.searchPosts(eq("Test"), eq("author"), eq(0), eq(10))).thenReturn(response);

        mockMvc.perform(get("/api/post/search")
                .param("keyword", "Test")
                .param("author", "author")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].title").value(postDTO.getTitle()))
                .andExpect(jsonPath("$.message").value("Search results"));
    }
}
