package com.wassim.databseTask.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVMUpdateDTO {
    @NotBlank(message = "Content cannot be blank")
    private String content;
    @NotBlank(message = "Tag ID is required")       
    private Long tagId;
}
