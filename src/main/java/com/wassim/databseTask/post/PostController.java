package com.wassim.databseTask.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAll() {
        return ResponseEntity.ok(postService.getAll());
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
    public ResponseEntity<ApiResponse<?>> likePost(@PathVariable Long id) {
       
        return ResponseEntity.ok(postService.likePost(id));
    }
    

}
