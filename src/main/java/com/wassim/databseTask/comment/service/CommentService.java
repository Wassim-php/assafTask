package com.wassim.databseTask.comment.service;

import java.util.List;

import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.comment.dto.CommentVMCreateDTO;
import com.wassim.databseTask.comment.dto.CommentVMUpdateDTO;
import com.wassim.databseTask.global.Response.ApiResponse;

public interface CommentService {
    ApiResponse<CommentDTO> create(CommentVMCreateDTO commentDTO);

    ApiResponse<List<CommentDTO>> getAll();

    ApiResponse<CommentDTO> getById(Long id);

    ApiResponse<CommentDTO> update(Long id, CommentVMUpdateDTO commentVMUpdateDTO);

    ApiResponse<Void> delete(Long id);

}
