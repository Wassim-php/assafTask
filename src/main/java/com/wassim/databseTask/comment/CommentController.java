package com.wassim.databseTask.comment;

import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.comment.dto.CommentVMCreateDTO;
import com.wassim.databseTask.comment.dto.CommentVMUpdateDTO;
import com.wassim.databseTask.comment.service.CommentService;
import com.wassim.databseTask.global.Response.ApiResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
    @Autowired
    private CommentService commentService;

     @PostMapping("/create")
    public ResponseEntity<ApiResponse<CommentDTO>> create(@RequestBody CommentVMCreateDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> update(@PathVariable Long id, @RequestBody CommentVMUpdateDTO dto) {
        return ResponseEntity.ok(commentService.update(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.delete(id));
    }
    
}
