package com.wassim.databseTask.user;

import java.util.List;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.tag.TagEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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


   
}
