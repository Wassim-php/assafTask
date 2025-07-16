package com.wassim.databseTask.post.service;

import java.util.List;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;

public interface PostService {
    ApiResponse<PostDTO> create(PostVMCreateDTO postVMCreateDTO);

    ApiResponse<PostDTO> getById(Long id);

    ApiResponse<List<PostDTO>> getAll();

    ApiResponse<PostDTO> update(Long id, PostVMUpdateDTO postVMUpdateDTO);

    ApiResponse<Void> delete(Long id);
    
    ApiResponse<?> likePost(Long postId);
}
