package com.wassim.databseTask.global.middleware;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.comment.CommentRepositry;
import com.wassim.databseTask.global.Exceptions.UnauthorizedException;
import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.service.UserServiceImpl;

@Aspect
@Component
public class CheckOwnershipAspect {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepositry commentRepositry;

    @Before("execution(* com.wassim.databseTask.comment.CommentServiceImpl.update(..)) || " +
            "execution(* com.wassim.databseTask.comment.CommentServiceImpl.delete(..))")
    public void checkCommentOwnership(JoinPoint joinPoint) {

        Long commentId = (Long) joinPoint.getArgs()[0];
        CommentEntity comment = commentRepositry.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        UserEntity currentUser = userService.getCurrentUser();
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this comment.");
        }
    }

    @Before("execution(* com.wassim.databseTask.tag.TagServiceImpl.update(..)) || " +
            "execution(* com.wassim.databseTask.tag.TagServiceImpl.delete(..))")
    public void checkTagOwnership(JoinPoint joinPoint) {

        Long tagId = (Long) joinPoint.getArgs()[0];
        TagEntity tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        UserEntity currentUser = userService.getCurrentUser();
        if (!tag.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this tag.");
        }
    }
}
