package com.wassim.databseTask.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVMCreateDTO {

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Tag ID is required")
    private Long tagId;
}
