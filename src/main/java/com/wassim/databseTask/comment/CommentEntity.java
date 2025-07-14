package com.wassim.databseTask.comment;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class CommentEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
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
    public TagEntity getTag(){
        return tag;
    }
    public void setTag(TagEntity tag){
        this.tag = tag;
    }

    public UserEntity getUser(){
        return this.user;
    }

    public void setUser(UserEntity user){
        this.user = user;
    }

}