package com.wassim.databseTask.global.middleware;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.comment.CommentRepositry;
import com.wassim.databseTask.global.Exceptions.UnauthorizedException;
import com.wassim.databseTask.post.PostEntity;
import com.wassim.databseTask.post.repository.PostRepository;
import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.service.UserServiceImpl;

import jakarta.annotation.PostConstruct;

@Aspect
@Component
public class CheckOwnershipAspect {
    @PostConstruct
    public void init() {
        System.out.println("✅ Aspect Bean Initialized");
    }

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepositry commentRepositry;

    @Autowired
    private PostRepository postRepository;

    @Before("execution(* com.wassim.databseTask.comment.service.CommentServiceImpl.update(..)) || " +
            "execution(* com.wassim.databseTask.comment.service.CommentServiceImpl.delete(..)) || " +
            "execution(* com.wassim.databseTask.post.service.PostServiceImpl.removeComment(..))")
    public void checkCommentOwnership(JoinPoint joinPoint) {
        System.out
                .println("✅ Aspect: checkCommentOwnership triggered for method: " + joinPoint.getSignature().getName());

        Long commentId = (Long) joinPoint.getArgs()[0];
        CommentEntity comment = commentRepositry.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        UserEntity currentUser = userService.getCurrentUser();
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this comment.");
        }
    }

    @Before("execution(* com.wassim.databseTask.tag.service.TagServiceImpl.update(..)) || " +
            "execution(* com.wassim.databseTask.tag.service.TagServiceImpl.delete(..))")
    public void checkTagOwnership(JoinPoint joinPoint) {
        System.out.println("✅ Aspect: checkTagOwnership triggered for method: " + joinPoint.getSignature().getName());

        Long tagId = (Long) joinPoint.getArgs()[0];
        TagEntity tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        UserEntity currentUser = userService.getCurrentUser();
        if (!tag.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this tag.");
        }
    }

    @Before("execution(* com.wassim.databseTask.post.service.PostServiceImpl.update(..)) || " +
            "execution(* com.wassim.databseTask.post.service.PostServiceImpl.delete(..))")
    public void checkPostOwnership(JoinPoint joinPoint) {
        Long postId = (Long) joinPoint.getArgs()[0];
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserEntity user = userService.getCurrentUser();
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this post. ");
        }

    }

}
