package com.wassim.databseTask.post;



import com.wassim.databseTask.comment.CommentEntity;
import com.wassim.databseTask.comment.CommentRepositry;
import com.wassim.databseTask.comment.dto.CommentDTO;
import com.wassim.databseTask.comment.service.CommentServiceImpl;

import com.wassim.databseTask.global.Response.ApiResponse;

import com.wassim.databseTask.post.dto.PostDTO;
import com.wassim.databseTask.post.dto.PostVMCreateDTO;
import com.wassim.databseTask.post.dto.PostVMUpdateDTO;
import com.wassim.databseTask.post.repository.PostRepository;

import com.wassim.databseTask.post.service.PostServiceImpl;
import com.wassim.databseTask.user.UserEntity;
import com.wassim.databseTask.user.UserRepository;
import com.wassim.databseTask.user.service.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepositry commentRepositry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_shouldReturnCreatedPostDTO() {
        PostVMCreateDTO dto = new PostVMCreateDTO("title", "desc");
        UserEntity user = new UserEntity();
        user.setId(1L);

        PostEntity saved = new PostEntity();
        saved.setId(1L);
        saved.setTitle("title");
        saved.setDescription("desc");
        saved.setAuthor(user);
        saved.setLikedUsers(new HashSet<>());
        saved.setComments(new ArrayList<>());

        when(userService.getCurrentUser()).thenReturn(user);
        when(postRepository.save(any())).thenReturn(saved);

        ApiResponse<PostDTO> response = postService.create(dto);

        assertTrue(response.isState());
        assertEquals("title", response.getData().getTitle());
    }

    @Test
    void getPostById_shouldReturnPost() {
        PostEntity post = new PostEntity();
        post.setId(1L);
        post.setTitle("post");
        post.setDescription("desc");
        post.setAuthor(new UserEntity());
        post.setLikedUsers(new HashSet<>());
        post.setComments(new ArrayList<>());

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        ApiResponse<PostDTO> response = postService.getById(1L);

        assertTrue(response.isState());
        assertEquals(1L, response.getData().getId());
    }

    @Test
    void getAllPosts_shouldReturnPageOfPostDTOs() {
        PostEntity post = new PostEntity();
        post.setId(1L);
        post.setTitle("post");
        post.setDescription("desc");
        post.setAuthor(new UserEntity());
        post.setLikedUsers(new HashSet<>());
        post.setComments(new ArrayList<>());

        Page<PostEntity> page = new PageImpl<>(List.of(post));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

        ApiResponse<Page<PostDTO>> response = postService.getAll(0, 10);

        assertTrue(response.isState());
        assertEquals(1, response.getData().getTotalElements());
    }

    @Test
    void updatePost_shouldReturnUpdatedPostDTO() {
        PostEntity post = new PostEntity();
        post.setId(1L);
        post.setTitle("old");
        post.setDescription("old");
        post.setAuthor(new UserEntity());
        post.setLikedUsers(new HashSet<>());
        post.setComments(new ArrayList<>());

        PostVMUpdateDTO dto = new PostVMUpdateDTO("new", "new");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);

        ApiResponse<PostDTO> response = postService.update(1L, dto);

        assertTrue(response.isState());
        assertEquals("new", response.getData().getTitle());
    }

    @Test
    void deletePost_shouldReturnSuccess() {
        PostEntity post = new PostEntity();
        post.setId(1L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        ApiResponse<Void> response = postService.delete(1L);

        assertTrue(response.isState());
        verify(postRepository).delete(post);
    }

    @Test
    void likePost_shouldLikeAndUnlikePost() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        PostEntity post = new PostEntity();
        post.setId(1L);
        post.setLikedUsers(new HashSet<>());
        post.setAuthor(user);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userService.getCurrentUser()).thenReturn(user);

        // Like
        ApiResponse<?> likeResp = postService.likePost(1L);
        assertTrue(likeResp.isState());
        assertEquals("Post liked", likeResp.getMessage());

        // Unlike
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        post.getLikedUsers().add(user);
        ApiResponse<?> unlikeResp = postService.likePost(1L);
        assertEquals("Post unliked", unlikeResp.getMessage());
    }

    @Test
    void addComment_shouldSaveComment() {
        CommentDTO dto = new CommentDTO();
        dto.setContent("new comment");

        CommentEntity commentEntity = new CommentEntity();

        PostEntity post = new PostEntity();
        post.setId(1L);

        when(commentService.mapFrom(any())).thenReturn(commentEntity);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        ApiResponse<Void> response = postService.addComment(1L, dto);

        assertTrue(response.isState());
        verify(commentRepositry).save(commentEntity);
    }

    @Test
    void removeComment_shouldDelegateToCommentService() {
        when(commentService.delete(1L)).thenReturn(new ApiResponse<>("deleted", null, true));

        ApiResponse<Void> response = postService.removeComment(1L);

        assertTrue(response.isState());
        assertEquals("deleted", response.getMessage());
    }

    @Test
    void searchPosts_shouldReturnFilteredPosts() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("author");

        PostEntity post = new PostEntity();
        post.setId(1L);
        post.setAuthor(user);
        post.setTitle("test");
        post.setDescription("desc");
        post.setLikedUsers(new HashSet<>());
        post.setComments(new ArrayList<>());

        Page<PostEntity> page = new PageImpl<>(List.of(post));
        when(userRepository.findByUsername("author")).thenReturn(Optional.of(user));
        when(postRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        ApiResponse<Page<PostDTO>> response = postService.searchPosts("test", "author", 0, 10);

        assertTrue(response.isState());
        assertEquals(1, response.getData().getTotalElements());
    }
}

