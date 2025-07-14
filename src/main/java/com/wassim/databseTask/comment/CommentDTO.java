package com.wassim.databseTask.comment;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Long getTagId(){
        return tagId;
    }
    public void setTagId(Long tagId){
         this.tagId = tagId;
    }
    public String getTagName(){
        return tagName;
    }
    public void setTagName(String tagName){
        this.tagName = tagName;
    }

}
