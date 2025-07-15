package com.wassim.databseTask.comment;

import java.util.List;

import com.wassim.databseTask.*;
import com.wassim.databseTask.Response.ApiResponse;

public interface CommentService {
    ApiResponse<CommentDTO> create(CommentDTO commentDTO);

    ApiResponse<List<CommentDTO>> getAll();

    ApiResponse<CommentDTO> getById(Long id);

    ApiResponse<CommentDTO> update(Long id, CommentDTO commentDTO);

    ApiResponse<Void> delete(Long id);
    
}
