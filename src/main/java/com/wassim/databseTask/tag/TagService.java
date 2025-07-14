package com.wassim.databseTask.tag;
import com.wassim.databseTask.tag.*;
import java.util.List;

public interface TagService {
    TagDTO create(TagDTO tagDTO);
    List<TagDTO> getAll();
    TagDTO getById(Long id);
    TagDTO update(Long id, TagDTO tagDTO);
    void delete(Long id);
}
