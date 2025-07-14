package com.wassim.databseTask.comment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepositry extends JpaRepository<CommentEntity, Long> {
    
    
}
