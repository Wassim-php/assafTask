package com.wassim.dto;

import java.util.List;

public class TagDTO {
    private Long id;
    private String name;
    private List<CommentDTO> comments;

    public Long getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    
    public void setId(Long id){
        this.id = id;

    }
    public void setName(String name){
        this.name = name;
    }

    public List<CommentDTO> getComments(){
        return comments;
    }
    public void setComments(java.util.List<CommentDTO> commentsDTO){
        this.comments = commentsDTO;
    }

     
}
