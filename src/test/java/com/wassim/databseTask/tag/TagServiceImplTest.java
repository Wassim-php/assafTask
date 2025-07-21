package com.wassim.databseTask.tag;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.global.Exceptions.ResourceNotFoundException;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.tag.dto.TagDTO;
import com.wassim.databseTask.tag.dto.TagVMCreateDTO;
import com.wassim.databseTask.tag.dto.TagVMUpdateDTO;
import com.wassim.databseTask.tag.service.TagServiceImpl;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private TagServiceImpl tagService;

    private UserEntity dummyUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyUser = new UserEntity();
        dummyUser.setId(100L);
        dummyUser.setUsername("dummyUser");

        when(userService.getCurrentUser()).thenReturn(dummyUser);
    }

    @Test
    void create_ShouldReturnCreatedTag() {
        TagVMCreateDTO createDTO = new TagVMCreateDTO();
        createDTO.setName("TestTag");

        TagEntity savedEntity = new TagEntity();
        savedEntity.setId(1L);
        savedEntity.setName("TestTag");
        savedEntity.setUser(dummyUser);

        when(tagRepository.save(any(TagEntity.class))).thenReturn(savedEntity);

        ApiResponse<TagDTO> response = tagService.create(createDTO);

        assertTrue(response.isState());
        assertEquals("Tag created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
        assertEquals("TestTag", response.getData().getName());

        verify(tagRepository, times(1)).save(any(TagEntity.class));
    }

    @Test
    void getAll_ShouldReturnListOfTags() {
        TagEntity tag1 = new TagEntity();
        tag1.setId(1L);
        tag1.setName("Tag1");

        TagEntity tag2 = new TagEntity();
        tag2.setId(2L);
        tag2.setName("Tag2");

        List<TagEntity> tagList = List.of(tag1, tag2);

        when(tagRepository.findAll()).thenReturn(tagList);

        ApiResponse<List<TagDTO>> response = tagService.getAll();

        assertTrue(response.isState());
        assertEquals("Tags retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals("Tag1", response.getData().get(0).getName());
        assertEquals("Tag2", response.getData().get(1).getName());

        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingId_ShouldReturnTag() {
        TagEntity tag = new TagEntity();
        tag.setId(1L);
        tag.setName("Tag1");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        ApiResponse<TagDTO> response = tagService.getById(1L);

        assertTrue(response.isState());
        assertEquals("Tag found", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
        assertEquals("Tag1", response.getData().getName());

        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void getById_NonExistingId_ShouldThrowException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            tagService.getById(1L);
        });

        assertEquals("Tag not found with id: 1", ex.getMessage());

        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void update_ExistingTag_ShouldReturnUpdatedTag() {
        TagEntity existingTag = new TagEntity();
        existingTag.setId(1L);
        existingTag.setName("OldName");

        TagVMUpdateDTO updateDTO = new TagVMUpdateDTO();
        updateDTO.setName("NewName");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(existingTag)).thenAnswer(i -> i.getArgument(0)); // return updated entity

        ApiResponse<TagDTO> response = tagService.update(1L, updateDTO);

        assertTrue(response.isState());
        assertEquals("Tag updated successfully", response.getMessage());
        assertEquals("NewName", response.getData().getName());

        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(existingTag);
    }

    @Test
    void update_NonExistingTag_ShouldThrowException() {
        TagVMUpdateDTO updateDTO = new TagVMUpdateDTO();
        updateDTO.setName("NewName");

        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            tagService.update(1L, updateDTO);
        });

        assertEquals("Tag not found", ex.getMessage());

        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void delete_ExistingTagWithoutComments_ShouldSucceed() {
        TagEntity tag = new TagEntity();
        tag.setId(1L);
        tag.setComments(new ArrayList<>());

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        doNothing().when(tagRepository).deleteById(1L);

        ApiResponse<Void> response = tagService.delete(1L);

        assertTrue(response.isState());
        assertEquals("Tag deleted successfully", response.getMessage());

        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ExistingTagWithComments_ShouldThrowException() {
        TagEntity tag = new TagEntity();
        tag.setId(1L);
        CommentEntity comment = new CommentEntity();
        List<CommentEntity> comments = List.of(comment); // simulate non-empty comments list
        tag.setComments(comments);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            tagService.delete(1L);
        });

        assertEquals("Cannot delete tag with existing comments", ex.getMessage());

        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, never()).deleteById(any());
    }

    @Test
    void delete_NonExistingTag_ShouldThrowException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            tagService.delete(1L);
        });

        assertEquals("Tag not found with id: 1", ex.getMessage());

        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, never()).deleteById(any());
    }
}
