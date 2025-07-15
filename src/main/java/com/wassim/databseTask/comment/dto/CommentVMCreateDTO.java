package com.wassim.databseTask.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVMCreateDTO {
    private String content;
    private Long tagId;

}
