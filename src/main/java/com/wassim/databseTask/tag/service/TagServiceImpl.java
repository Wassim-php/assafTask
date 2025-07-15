package com.wassim.databseTask.tag.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.tag.dto.TagDTO;
import com.wassim.databseTask.tag.dto.TagVMCreateDTO;
import com.wassim.databseTask.tag.dto.TagVMUpdateDTO;
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
    public ApiResponse<TagDTO> create(TagVMCreateDTO dto) {
        TagEntity entity = new TagEntity();
    entity.setName(dto.getName());
    entity.setUser(userService.getCurrentUser());

    TagDTO result = mapTo(tagRepository.save(entity));
    return new ApiResponse<>("Tag created successfully", result, true);
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
    public ApiResponse<TagDTO> update(Long id, TagVMUpdateDTO tagDTO) {
         TagEntity tag = tagRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setName(tagDTO.getName());
        TagDTO result = mapTo(tagRepository.save(tag));
        return new ApiResponse<>("Tag updated successfully", result, true);

    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        TagEntity tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tagRepository.deleteById(id);
        return new ApiResponse<>("Tag deleted successfully", null, true);
    }
}
