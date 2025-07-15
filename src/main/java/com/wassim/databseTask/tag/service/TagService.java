package com.wassim.databseTask.tag.service;
import java.util.List;

import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.tag.dto.TagDTO;
import com.wassim.databseTask.tag.dto.TagVMCreateDTO;
import com.wassim.databseTask.tag.dto.TagVMUpdateDTO;

public interface TagService {
    ApiResponse<TagDTO> create(TagVMCreateDTO tagDTO);
    ApiResponse<List<TagDTO>> getAll();
    ApiResponse<TagDTO> getById(Long id);
    ApiResponse<TagDTO> update(Long id, TagVMUpdateDTO tagDTO);
    ApiResponse<Void> delete(Long id);
}
