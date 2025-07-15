package com.wassim.databseTask.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private Long tagId;
    private String tagName;

    public CommentDTO(){
        this.id = null;
        this.content = null;
        this.tagId = null;
        this.tagName = null;
    }

    public CommentDTO(Long id, String content, Long tagId, String tagName){
        this.id = id;
        this.content = content;
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public CommentDTO(Long id, String content, Long tagId){
        this.id = id;
        this.content = content;
        this.tagId = tagId;
    }

   

}
