package com.wassim.databseTask.user;

import java.util.List;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.tag.TagEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String password;

    @OneToMany(mappedBy ="user")
    private List<TagEntity> tags;
    
    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;


    public Long getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public List<TagEntity> getTags(){
        return this.tags;
    }

    public List<CommentEntity> getComments(){
        return this.comments;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void setTags(List<TagEntity> tags){
        this.tags = tags;
    }

    public void setComments(List<CommentEntity> comments){
        this.comments = comments;
    }
}
