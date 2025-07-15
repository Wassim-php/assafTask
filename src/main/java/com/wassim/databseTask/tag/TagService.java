package com.wassim.databseTask.tag;
import com.wassim.databseTask.Response.ApiResponse;
import com.wassim.databseTask.tag.*;
import java.util.List;

public interface TagService {
    ApiResponse<TagDTO> create(TagDTO tagDTO);
    ApiResponse<List<TagDTO>> getAll();
    ApiResponse<TagDTO> getById(Long id);
    ApiResponse<TagDTO> update(Long id, TagDTO tagDTO);
    ApiResponse<Void> delete(Long id);
}
