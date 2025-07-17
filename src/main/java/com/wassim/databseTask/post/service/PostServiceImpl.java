package com.wassim.databseTask.post.service;

import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.post.PostEntity;
import com.wassim.databseTask.post.PostRepository;
import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;
import com.wassim.databseTask.user.UserEntity;

import com.wassim.databseTask.user.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServiceImpl userService;

    private PostDTO mapTo(PostEntity post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setUserId(post.getAuthor().getId());
        dto.setLikedUsers(
                post.getLikedUsers().stream()
                        .map(UserEntity::getId)
                        .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public ApiResponse<PostDTO> create(PostVMCreateDTO dto) {
        UserEntity user = userService.getCurrentUser();
        PostEntity post = new PostEntity();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setAuthor(user);

        PostEntity saved = postRepository.save(post);
        return new ApiResponse<>("Post created successfully", mapTo(saved), true);
    }

    @Override
    public ApiResponse<PostDTO> getById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return new ApiResponse<>("Post found", mapTo(post), true);
    }

    @Override
    public ApiResponse<Page<PostDTO>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> posts = postRepository.findAll(pageable);
        Page<PostDTO> postDTOs = posts.map(this::mapTo);
        return new ApiResponse<>("Posts fetched successfully", postDTOs, true);
    }

    @Override
    public ApiResponse<PostDTO> update(Long id, PostVMUpdateDTO dto) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());

        return new ApiResponse<>("Post updated successfully", mapTo(postRepository.save(post)), true);
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
        return new ApiResponse<>("Post deleted successfully", null, true);
    }

    @Override
    public ApiResponse<?> likePost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        UserEntity user = userService.getCurrentUser();

        if (post.getLikedUsers().contains(user)) {
            post.getLikedUsers().remove(user);
            postRepository.save(post);
            return new ApiResponse<>("Post unliked", null, true);
        } else {
            post.getLikedUsers().add(user);
            postRepository.save(post);
            return new ApiResponse<>("Post liked", null, true);
        }
    }
}
