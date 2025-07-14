package com.wassim.databseTask.comment;

import java.util.List;

import com.wassim.dto.CommentDTO;

public interface CommentService {
    CommentDTO create(CommentDTO commentDTO);

    List<CommentDTO> getAll();

    CommentDTO getById(Long id);

    CommentDTO update(Long id, CommentDTO commentDTO);

    void delete(Long id);
    
}
