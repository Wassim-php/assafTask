package com.wassim.databseTask.comment;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.comment.dto.CommentVMCreateDTO;
import com.wassim.databseTask.comment.dto.CommentVMUpdateDTO;
import com.wassim.databseTask.comment.service.CommentServiceImpl;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.security.JwtAuthenticationFilter;
import com.wassim.databseTask.security.JwtUtility;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentServiceImpl commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtility jwtUtility;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void testCreateComment() throws Exception {
        CommentVMCreateDTO createDTO = new CommentVMCreateDTO();
        createDTO.setContent("test comment");
        createDTO.setTagId(1L);

        CommentDTO returnedDto = new CommentDTO();
        returnedDto.setContent("test comment");
        returnedDto.setId(1L);
        returnedDto.setTagId(1L);
        returnedDto.setTagName("General");

        ApiResponse<CommentDTO> response = new ApiResponse<>("Comment created", returnedDto, true);

        Mockito.when(commentService.create(Mockito.any()))
                .thenAnswer(invocation -> {
                    System.out.println("====> MOCK SERVICE CALLED");
                    return response;
                });

        mockMvc.perform(post("/api/comments/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andDo(result -> {
                    System.out.println("=====> RESPONSE BODY:");
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Comment created"))
                .andExpect(jsonPath("$.data.content").value("test comment"))
                .andExpect(jsonPath("$.data.tagId").value(1));
    }

    @Test
    void testGetAllCOmments() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("test comment");

        ApiResponse<List<CommentDTO>> apiResponse = new ApiResponse<>("All comments", List.of(commentDTO), true);
        Mockito.when(commentService.getAll()).thenReturn(apiResponse);

        mockMvc.perform(get("/api/comments/all")).andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("All comments")))
                .andExpect(jsonPath("$.data[0].content", is("test comment")));
    }

    @Test
    public void testGetCommentById() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("test comment");

        ApiResponse<CommentDTO> apiResponse = new ApiResponse<>("Comment found", commentDTO, true);

        Mockito.when(commentService.getById(1L)).thenReturn(apiResponse);

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment found")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.content", is("test comment")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void testUpdateComment() throws Exception {
        CommentVMUpdateDTO updateDto = new CommentVMUpdateDTO();
        updateDto.setContent("updated comment");

        CommentDTO responseDto = new CommentDTO();
        responseDto.setId(1L);
        responseDto.setContent("updated comment");

        ApiResponse<CommentDTO> apiResponse = new ApiResponse<>("Comment updated", responseDto, true);

        Mockito.when(commentService.update(Mockito.eq(1L), Mockito.any(CommentVMUpdateDTO.class)))
                .thenReturn(apiResponse);

        String jsonRequest = "{\"content\":\"updated comment\"}";

        mockMvc.perform(put("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment updated")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.content", is("updated comment")))
                .andExpect(jsonPath("$.state", is(true)));
    }

    @Test
    public void testDeleteComment() throws Exception {
        ApiResponse<Void> apiResponse = new ApiResponse<>("Comment deleted", null, true);

        Mockito.when(commentService.delete(1L)).thenReturn(apiResponse);

        mockMvc.perform(delete("/api/comments/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Comment deleted")))
                .andExpect(jsonPath("$.state", is(true)));
    }

}
