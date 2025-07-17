package com.wassim.databseTask.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wassim.databseTask.post.PostEntity;

import org.springframework.data.domain.Page;

public interface PostRepository extends JpaRepository<PostEntity, Long>, JpaSpecificationExecutor<PostEntity> {
    Page<PostEntity> findAll(Specification<PostEntity> spec,Pageable pageable);

}
