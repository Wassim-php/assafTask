package com.wassim.databseTask.tag;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.security.JwtUtility;
import com.wassim.databseTask.tag.dto.TagDTO;
import com.wassim.databseTask.tag.dto.TagVMCreateDTO;
import com.wassim.databseTask.tag.dto.TagVMUpdateDTO;
import com.wassim.databseTask.tag.service.TagService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper; // for JSON conversion

    @MockitoBean
    private JwtUtility jwtUtility;

    @Test
    void testCreateTag() throws Exception {
        TagVMCreateDTO createDTO = new TagVMCreateDTO();
        createDTO.setName("TestTag");

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1L);
        tagDTO.setName("TestTag");

        ApiResponse<TagDTO> apiResponse = new ApiResponse<>("Tag created successfully", tagDTO, true);

        when(tagService.create(any(TagVMCreateDTO.class))).thenReturn(apiResponse);

        mockMvc.perform(post("/api/tags/create") // adjust URL to your controller mapping
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tag created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TestTag"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void testGetAllTags() throws Exception {
        TagDTO tag1 = new TagDTO();
        tag1.setId(1L);
        tag1.setName("Tag1");

        TagDTO tag2 = new TagDTO();
        tag2.setId(2L);
        tag2.setName("Tag2");

        ApiResponse<List<TagDTO>> apiResponse = new ApiResponse<>("Tags retrieved successfully", List.of(tag1, tag2),
                true);

        when(tagService.getAll()).thenReturn(apiResponse);

        mockMvc.perform(get("/api/tags/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tags retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Tag1"))
                .andExpect(jsonPath("$.data[1].name").value("Tag2"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void testGetTagById() throws Exception {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1L);
        tagDTO.setName("Tag1");

        ApiResponse<TagDTO> apiResponse = new ApiResponse<>("Tag found", tagDTO, true);

        when(tagService.getById(1L)).thenReturn(apiResponse);

        mockMvc.perform(get("/api/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tag found"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Tag1"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void testUpdateTag() throws Exception {
        TagVMUpdateDTO updateDTO = new TagVMUpdateDTO();
        updateDTO.setName("UpdatedTag");

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1L);
        tagDTO.setName("UpdatedTag");

        ApiResponse<TagDTO> apiResponse = new ApiResponse<>("Tag updated successfully", tagDTO, true);

        when(tagService.update(eq(1L), any(TagVMUpdateDTO.class))).thenReturn(apiResponse);

        mockMvc.perform(put("/api/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tag updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("UpdatedTag"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void testDeleteTag() throws Exception {
        ApiResponse<Void> apiResponse = new ApiResponse<>("Tag deleted successfully", null, true);

        when(tagService.delete(1L)).thenReturn(apiResponse);

        mockMvc.perform(delete("/api/tags/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tag deleted successfully"))
                .andExpect(jsonPath("$.state").value(true));
    }
}
