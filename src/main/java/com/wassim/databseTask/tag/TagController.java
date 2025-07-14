package com.wassim.databseTask.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.tag.TagService;
import com.wassim.Response.ApiResponse;
import com.wassim.databseTask.tag.*;

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
    public ResponseEntity<ApiResponse<TagDTO>> createTag(@RequestBody TagDTO tagDTO) {
    TagDTO created = tagService.create(tagDTO);
    return new ResponseEntity<ApiResponse<TagDTO>>(new ApiResponse<TagDTO>("Tag created", created, true), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getAllTags() {
        List<TagDTO> tags = tagService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("All tags retrieved",  tags, true));
    }


   @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> getTagById(@PathVariable Long id) {
    TagDTO tag = tagService.getById(id);
    return ResponseEntity.ok(new ApiResponse<>("Tag retrieved successfully", tag, true));
    }


   @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        TagDTO updated = tagService.update(id, tagDTO);
        return ResponseEntity.ok(new ApiResponse<>("Tag updated successfully", updated, true));
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Tag deleted", null, true));
    }
    
    
    
    
}
