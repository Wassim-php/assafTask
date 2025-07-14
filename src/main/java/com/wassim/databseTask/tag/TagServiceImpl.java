package com.wassim.databseTask.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.comment.CommentDTO;
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
    public TagDTO create(TagDTO tagDTO) {
        TagEntity entity = this.mapFrom(tagDTO);
        entity.setUser(userService.getCurrentUser());
        return mapTo(tagRepository.save(entity));
    }

    @Override
    public List<TagDTO> getAll() {
        List<TagEntity> tags = tagRepository.findAll();
        return tags.stream().map(this::mapTo).toList();
    }

    @Override
    public TagDTO getById(Long id) {
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return mapTo(tag);
    }

   @Override
    public TagDTO update(Long id, TagDTO tagDTO) {
    TagEntity tag = tagRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tag not found"));
        System.out.println("Current user ID: " + userService.getCurrentUser().getId());
        System.out.println("Tag owner ID: " + tag.getUser().getId());

    UserEntity currentUser = userService.getCurrentUser();
    if (!tag.getUser().getId().equals(currentUser.getId())) {
        throw new RuntimeException("You are not authorized to update this tag");
    }

    tag.setName(tagDTO.getName());
    return mapTo(tagRepository.save(tag));
    }


    @Override
    public void delete(Long id) {
    TagEntity tag = tagRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Tag not found"));

    if (!tag.getUser().getId().equals(userService.getCurrentUser().getId())) {
        throw new RuntimeException("You are not authorized to delete this tag");
    }

    tagRepository.deleteById(id);
    }
}
