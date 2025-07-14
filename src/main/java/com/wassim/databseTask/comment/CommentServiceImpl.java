package com.wassim.databseTask.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CommentDTO create(CommentDTO dto) {
        CommentEntity entity = this.mapFrom(dto);
        entity.setUser(userService.getCurrentUser());
        return mapTo(commentRepositry.save(entity));
    }

    @Override
    public List<CommentDTO> getAll() {
        return commentRepositry.findAll().stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getById(Long id) {
        return commentRepositry.findById(id)
                .map(this::mapTo)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public CommentDTO update(Long id, CommentDTO dto) {
        CommentEntity entity = commentRepositry.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        entity.setContent(dto.getContent());

        if (dto.getTagId() != null) {
            TagEntity tag = tagRepository.findById(dto.getTagId())
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            entity.setTag(tag);
        }

        UserEntity currentUser = userService.getCurrentUser();
    if (!entity.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("You are not authorized to update this comment");
    }

        return mapTo(commentRepositry.save(entity));
    }

    @Override
public void delete(Long id) {
    CommentEntity comment = commentRepositry.findById(id)
        .orElseThrow(() -> new RuntimeException("Comment not found"));

    if (!comment.getUser().getId().equals(userService.getCurrentUser().getId())) {
        throw new RuntimeException("You are not authorized to delete this comment");
    }

    commentRepositry.deleteById(id);
}
}
