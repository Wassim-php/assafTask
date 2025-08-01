package com.wassim.databseTask.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.comment.dto.CommentDTO;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;
import com.wassim.databseTask.post.service.PostService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PostDTO>> create(@Valid @RequestBody PostVMCreateDTO postVMCreateDTO) {
        return ResponseEntity.ok(postService.create(postVMCreateDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<PostDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> update(@Valid @PathVariable Long id,
            @RequestBody PostVMUpdateDTO postVMUpdateDTO) {
        return ResponseEntity.ok(postService.update(id, postVMUpdateDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(postService.delete(id));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Object>> likePost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.likePost(id));
    }

    @PostMapping("/{id}/add-comment")
    public ResponseEntity<ApiResponse<Void>> addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(postService.addComment(id, commentDTO));
    }

    @DeleteMapping("/{id}/remove-comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> removeComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(postService.removeComment(commentId));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostDTO>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.searchPosts(keyword, author, page, size));
    }

}
