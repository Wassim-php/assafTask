package com.wassim.databseTask.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.comment.CommentService;
import com.wassim.dto.ApiResponse;
import com.wassim.dto.CommentDTO;

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
    public ResponseEntity<ApiResponse<CommentDTO>>  create( @RequestBody CommentDTO commentDTO){
        
        try{
            System.out.println("Creating comment with tagId: " + commentDTO.getTagId());
            CommentDTO savedComment = commentService.create(commentDTO);
        return ResponseEntity.ok(
            new ApiResponse<>("Comment created successfully", savedComment, true)
        );
        }catch( Exception e){
            e.printStackTrace(); // log to terminal
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Failed to create comment", null, false)
            );
        }
        

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CommentDTO>>>getAll() {
        try{
            List<CommentDTO> comments = commentService.getAll();
            return ResponseEntity.ok(
                new ApiResponse<List<CommentDTO>>("Comments retrieved successfully", comments, true)
            );
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<List<CommentDTO>>("Failed to retrieve comments", null, false)
            );
        }
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> getById(@PathVariable Long id){
        try{
            CommentDTO comment = commentService.getById(id);
            if (comment == null) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Comment not found", null, false)
            );
        }
            return ResponseEntity.ok(
                new ApiResponse<CommentDTO>("Comment retrived successfully", comment, true)
            );
        }catch( Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<CommentDTO>("Failed to retrieve comment", null, false)
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDTO>> update(@PathVariable Long id, @RequestBody CommentDTO commentDTO){
        try{
            CommentDTO updatedComment = commentService.update(id, commentDTO);
            if (updatedComment == null) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Comment not found", null, false)
            );
        }
            return ResponseEntity.ok(
                new ApiResponse<CommentDTO>("Comment updated successfully", updatedComment, true)
            );

        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<CommentDTO>("Failed to update comment", null, false)
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        commentService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<Void>("Comment deleted successfully", null, true)
        );
    }
    
}
