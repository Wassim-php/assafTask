package com.wassim.databseTask.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.tag.TagService;
import com.wassim.dto.CommentDTO;
import com.wassim.dto.TagDTO;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepository tagRepository;


    private TagDTO mapTo(TagEntity tag){
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
         List<CommentDTO> commentDTOs = tag.getComments() != null ?
        tag.getComments().stream()
            .map(comment -> new CommentDTO(comment.getId(), comment.getContent(), comment.getTag().getId(), comment.getTag().getName()))
            .collect(Collectors.toList()) : new ArrayList<>();

        dto.setComments(commentDTOs);
        return dto;
    }

    private TagEntity mapFrom(TagDTO dto){
        TagEntity entity = new TagEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }
    @Override
    public TagDTO create(TagDTO tagDTO){
        return mapTo(tagRepository.save(mapFrom(tagDTO)));
    }

    @Override
    public List<TagDTO> getAll(){
        List<TagEntity> tags = tagRepository.findAll();
        return tags.stream().map(this::mapTo).toList();
    }

    @Override
    public TagDTO getById(Long id){
        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return mapTo(tag);        
    }

    @Override
    public TagDTO update(Long id, TagDTO tagDTO){
        TagEntity entity = mapFrom(tagDTO);
        entity.setId(id);
        return mapTo(tagRepository.save(entity));

    }

    @Override
    public void delete(Long id){
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }
    
}
