package com.wassim.databseTask.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.comment.dto.CommentVMCreateDTO;
import com.wassim.databseTask.comment.dto.CommentVMUpdateDTO;
import com.wassim.databseTask.comment.service.CommentServiceImpl;
import com.wassim.databseTask.global.Response.ApiResponse;
import com.wassim.databseTask.tag.TagEntity;
import com.wassim.databseTask.tag.TagRepository;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Mock
    private CommentRepositry commentRepositry;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    private CommentEntity commentEntity;
    private CommentDTO commentDTO;
    private TagEntity tagEntity;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        tagEntity = new TagEntity();
        tagEntity.setId(1L);
        tagEntity.setName("test tag");

        user = new UserEntity();
        user.setId(1L);
        user.setUsername("test user");

        commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setContent("test comment");
        commentEntity.setTag(tagEntity);
        commentEntity.setUser(user);

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setContent("Test Comment DTO");
        commentDTO.setTagId(1L);
    }

    @Test
    void testCreateComment_Success() {
        CommentVMCreateDTO dto = new CommentVMCreateDTO();
        dto.setContent("test comment");
        dto.setTagId(1L);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tagEntity));
        when(userServiceImpl.getCurrentUser()).thenReturn(user);
        when(commentRepositry.save(any(CommentEntity.class))).thenReturn(commentEntity);

        ApiResponse<CommentDTO> respone = commentServiceImpl.create(dto);

        assertTrue(respone.isState());
        assertEquals("Comment created successfully", respone.getMessage());
        assertEquals("test comment", respone.getData().getContent());

    }

    @Test
    void testGetAllComments_Success() {
        when(commentRepositry.findAll()).thenReturn(List.of(commentEntity));

        ApiResponse<List<CommentDTO>> response = commentServiceImpl.getAll();

        assertTrue(response.isState());
        assertEquals("test comment", response.getData().get(0).getContent());
        assertEquals(1, response.getData().size());
    }

    @Test
    void testGetById_Success() {
        when(commentRepositry.findById(1L)).thenReturn(Optional.of(commentEntity));

        ApiResponse<CommentDTO> response = commentServiceImpl.getById(1L);

        assertTrue(response.isState());
        assertEquals("Comment found", response.getMessage());

    }

    @Test
    void testGetById_NotFound() {
        when(commentRepositry.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> commentServiceImpl.getById(1L));
    }

    @Test
    void testUpdateComment_Success() {
        CommentVMUpdateDTO updateDTO = new CommentVMUpdateDTO();
        updateDTO.setContent("Updated content");
        updateDTO.setTagId(1L);

        when(commentRepositry.findById(1L)).thenReturn(Optional.of(commentEntity));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tagEntity));
        when(commentRepositry.save(any(CommentEntity.class))).thenReturn(commentEntity);

        ApiResponse<CommentDTO> response = commentServiceImpl.update(1L, updateDTO);

        assertTrue(response.isState());
        assertEquals("Comment updated successfully", response.getMessage());
    }

    @Test
    void testDeleteComment_Success() {
        when(commentRepositry.findById(1L)).thenReturn(Optional.of(commentEntity));

        ApiResponse<Void> response = commentServiceImpl.delete(1L);

        assertTrue(response.isState());
        verify(commentRepositry).deleteById(1L);
    }

}
