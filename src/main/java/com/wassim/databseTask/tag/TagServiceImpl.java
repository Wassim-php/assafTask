package com.wassim.databseTask.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.comment.CommentDTO;
import com.wassim.databseTask.Response.ApiResponse;
import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.UserServiceImpl;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserServiceImpl userService;

    private TagDTO mapTo(TagEntity tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        List<CommentDTO> commentDTOs = tag.getComments() != null
                ? tag.getComments().stream()
                        .map(comment -> new CommentDTO(comment.getId(), comment.getContent(), comment.getTag().getId(), comment.getTag().getName()))
                        .collect(Collectors.toList())
                : new ArrayList<>();
        dto.setComments(commentDTOs);
        return dto;
    }

    private TagEntity mapFrom(TagDTO dto) {
        TagEntity entity = new TagEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    @Override
    public ApiResponse<TagDTO> create(TagDTO tagDTO) {
        TagEntity entity = this.mapFrom(tagDTO);
        entity.setUser(userService.getCurrentUser());
        TagDTO saved = mapTo(tagRepository.save(entity));
        return new ApiResponse<>("Tag created successfully", saved, true);
    }

    @Override
    public ApiResponse<List<TagDTO>> getAll() {
        List<TagEntity> tags = tagRepository.findAll();
        List<TagDTO> dtos = tags.stream().map(this::mapTo).toList();
        return new ApiResponse<>("Tags retrieved successfully", dtos, true);
    }

    @Override
    public ApiResponse<TagDTO> getById(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return new ApiResponse<>("Tag found", mapTo(tag), true);
    }

    @Override
    public ApiResponse<TagDTO> update(Long id, TagDTO tagDTO) {
        TagEntity tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tag.setName(tagDTO.getName());
        TagDTO updated = mapTo(tagRepository.save(tag));
        return new ApiResponse<>("Tag updated successfully", updated, true);
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        TagEntity tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tagRepository.deleteById(id);
        return new ApiResponse<>("Tag deleted successfully", null, true);
    }
}
