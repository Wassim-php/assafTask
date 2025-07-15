package com.wassim.databseTask.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentVMUpdateDTO {
    private String content;
    private Long tagId;
}
