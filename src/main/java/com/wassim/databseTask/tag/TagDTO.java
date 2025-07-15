package com.wassim.databseTask.tag;

import java.util.List;

import com.wassim.databseTask.comment.CommentDTO;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TagDTO {
    private Long id;
    private String name;
    private List<CommentDTO> comments;

    

     
}
