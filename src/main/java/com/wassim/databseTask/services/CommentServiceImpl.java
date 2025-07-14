package com.wassim.databseTask.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.databseTask.tag.TagRepository;
import com.wassim.dto.CommentDTO;
import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.comment.CommentRepositry;
import com.wassim.databseTask.comment.CommentService;
import com.wassim.databseTask.tag.TagEntity;

import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepositry commentRepositry;
    @Autowired
    private TagRepository tagRepository;

    private CommentDTO mapTo(CommentEntity comment){
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());

       if (comment.getTag() != null) {
        dto.setTagId(comment.getTag().getId());
        dto.setTagName(comment.getTag().getName());
    }
        return dto;
    }

    private CommentEntity mapFrom(CommentDTO comment){
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
    public CommentDTO create(CommentDTO dto){
        return mapTo(commentRepositry.save(mapFrom(dto)));
    }

    @Override
    public List<CommentDTO> getAll() {
        return commentRepositry.findAll().stream().map(this::mapTo).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getById(Long id){
        return commentRepositry.findById(id)
                .map(this::mapTo)
                .orElse(null);
    }

    public CommentDTO update(Long id, CommentDTO dto){
        boolean exists = commentRepositry.existsById(id);
        if(!exists){
            return null;
        }
        CommentEntity entity = commentRepositry.findById(id).get();
        entity.setContent(dto.getContent());
        TagEntity tag = null;
        if(dto.getTagId() != null){
             tag = tagRepository.findById(id).orElseThrow(
                () -> new RuntimeException("tag not found")
             );
        }
        entity.setTag(tag);
        entity.setId(id);
        return mapTo(commentRepositry.save(entity));

    }

    @Override
    public void delete(Long id){
        commentRepositry.deleteById(id);
    }


}