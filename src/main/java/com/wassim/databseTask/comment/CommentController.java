package com.wassim.databseTask.comment;

import org.springframework.web.bind.annotation.RestController;

import com.wassim.Response.ApiResponse;
import com.wassim.databseTask.*;

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
    public ResponseEntity<ApiResponse<CommentDTO>> create(@RequestBody CommentDTO dto) {
        CommentDTO created = commentService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>("Comment created successfully",  created, true));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getAll() {
        List<CommentDTO> all = commentService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("All comments retrieved", all, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getById(@PathVariable Long id) {
        CommentDTO comment = commentService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("Comment retrieved",comment, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> update(@PathVariable Long id, @RequestBody CommentDTO dto) {
        CommentDTO updated = commentService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("Comment updated", updated, true));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Comment deleted", null, true));
    }
    
}
