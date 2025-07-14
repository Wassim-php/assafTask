package com.wassim.databseTask.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.tag.TagService;
import com.wassim.dto.ApiResponse;

import com.wassim.dto.TagDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TagDTO>> create(@RequestBody TagDTO tagDTO){
        try{
            TagDTO savedTag = tagService.create(tagDTO);
            return ResponseEntity.ok(
                new ApiResponse<TagDTO>("Tag created successfully", savedTag, true)
            );
        }catch( Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<TagDTO>("Failed to create tag", null, false)
            );
        }
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getAll() {
       try{ return ResponseEntity.ok(
            new ApiResponse<List<TagDTO>>("Tags retrieved successfully", tagService.getAll(), true)
        );
       }catch(Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<List<TagDTO>>("Failed to retrieve tags", null, false)
            );
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> getById(@PathVariable Long id) {
        try{
            TagDTO tag = tagService.getById(id);
            if(tag == null){
                return ResponseEntity.badRequest().body(
                    new ApiResponse<TagDTO>("Failed to find tag", null, false)
                );
            }
            return ResponseEntity.ok(
                new ApiResponse<TagDTO>("Tag retrieved successfully", tag, true)
            );
        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                    new ApiResponse<TagDTO>("Failed to find tag", null, false)
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> update(@PathVariable Long id, @RequestBody TagDTO tagDTO){
        try{
            TagDTO updatedTag = tagService.update(id, tagDTO);
            if (updatedTag == null) {
            return ResponseEntity.badRequest().body(
                new ApiResponse<>("Tag not found", null, false)
            );
        }
            return ResponseEntity.ok(
                new ApiResponse<TagDTO>("Tag updated successfully", updatedTag, true)
            );

        }catch(Exception e){
            return ResponseEntity.badRequest().body(
                new ApiResponse<TagDTO>("Failed to update tag", null, false)
            );
        }
    }

        @DeleteMapping("/{id}")
         public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){
        tagService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<Void>("Tag deleted successfully", null, true)
        );
    }
    
    
    
    
}
