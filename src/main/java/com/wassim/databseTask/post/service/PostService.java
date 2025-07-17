package com.wassim.databseTask.post.service;

import org.springframework.data.domain.Page;

import com.wassim.databseTask.comment.dto.CommentDTO;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;

public interface PostService {
    ApiResponse<PostDTO> create(PostVMCreateDTO postVMCreateDTO);

    ApiResponse<PostDTO> getById(Long id);

    ApiResponse<Page<PostDTO>> getAll(int page, int size);

    ApiResponse<PostDTO> update(Long id, PostVMUpdateDTO postVMUpdateDTO);

    ApiResponse<Void> delete(Long id);

    ApiResponse<?> likePost(Long postId);

    ApiResponse<Void> addComment(Long postId, CommentDTO comment);

    ApiResponse<Void> removeComment(Long commentId);

    ApiResponse<Page<PostDTO>> searchPosts(String keyword, String author, int page, int size);
}
