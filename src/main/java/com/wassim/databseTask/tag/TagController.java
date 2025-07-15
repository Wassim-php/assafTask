package com.wassim.databseTask.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.tag.dto.TagDTO;
import com.wassim.databseTask.tag.dto.TagVMCreateDTO;
import com.wassim.databseTask.tag.dto.TagVMUpdateDTO;
import com.wassim.databseTask.tag.service.TagService;

import jakarta.validation.Valid;

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
    public ResponseEntity<ApiResponse<TagDTO>> createTag(@Valid @RequestBody TagVMCreateDTO tagDTO) {
        return ResponseEntity.ok(tagService.create(tagDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TagDTO>>> getAllTags() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TagDTO>> updateTag(@Valid @PathVariable Long id,
            @RequestBody TagVMUpdateDTO tagDTO) {
        return ResponseEntity.ok(tagService.update(id, tagDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

}
