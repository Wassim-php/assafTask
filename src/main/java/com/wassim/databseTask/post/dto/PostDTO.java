package com.wassim.databseTask.post.dto;

import java.util.List;
import java.util.Set;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.comment.dto.CommentDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Set<Long> likedUsers;
    private List<CommentDTO> comments;

}
