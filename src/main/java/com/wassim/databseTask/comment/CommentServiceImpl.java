package com.wassim.databseTask.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.Response.ApiResponse;
import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.tag.TagServiceImpl;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.UserServiceImpl;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepositry commentRepositry;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserServiceImpl userService;

    private CommentDTO mapTo(CommentEntity comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());

        if (comment.getTag() != null) {
            dto.setTagId(comment.getTag().getId());
            dto.setTagName(comment.getTag().getName());
        }
        return dto;
    }

    private CommentEntity mapFrom(CommentDTO comment) {
        CommentEntity entity = new CommentEntity();
        entity.setId(comment.getId());
        entity.setContent(comment.getContent());

        if (comment.getTagId() != null) {
            TagEntity tag = tagRepository.findById(comment.getTagId())
                .orElseThrow(() -> new RuntimeException("Tag not found"));
            entity.setTag(tag);
        }

        return entity;
    }

    @Override
    public ApiResponse<CommentDTO> create(CommentDTO dto) {
        CommentEntity entity = this.mapFrom(dto);
        entity.setUser(userService.getCurrentUser());
        CommentDTO saved = mapTo(commentRepositry.save(entity));
        return new ApiResponse<CommentDTO>("Comment created successfully", saved, true);
    }

    @Override
public ApiResponse<List<CommentDTO>> getAll() {
    List<CommentDTO> comments = commentRepositry.findAll()
        .stream().map(this::mapTo).collect(Collectors.toList());
    return new ApiResponse<>("Comments fetched successfully", comments, true);
}

   @Override
public ApiResponse<CommentDTO> getById(Long id) {
    CommentEntity comment = commentRepositry.findById(id)
        .orElseThrow(() -> new RuntimeException("Comment not found"));

    return new ApiResponse<>("Comment found", mapTo(comment), true);
}

    @Override
public ApiResponse<CommentDTO> update(Long id, CommentDTO dto) {
    CommentEntity entity = commentRepositry.findById(id)
        .orElseThrow(() -> new RuntimeException("Comment not found"));

        entity.setContent(dto.getContent());

    if (dto.getTagId() != null) {
        TagEntity tag = tagRepository.findById(dto.getTagId())
            .orElseThrow(() -> new RuntimeException("Tag not found"));
        entity.setTag(tag);
    }

    return new ApiResponse<>("Comment updated successfully", mapTo(commentRepositry.save(entity)), true);
}

    @Override
public ApiResponse<Void> delete(Long id) {
    CommentEntity comment = commentRepositry.findById(id)
        .orElseThrow(() -> new RuntimeException("Comment not found"));
    commentRepositry.deleteById(id);
    return new ApiResponse<>("Comment deleted successfully", null, true);
}
}
