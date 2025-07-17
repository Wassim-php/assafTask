package com.wassim.databseTask.post.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.wassim.databseTask.post.PostEntity;
import com.wassim.databseTask.user.UserEntity;

public class PostSpecifications {
    public static Specification<PostEntity> hasTitle(String title) {
        return (root, query, builder) -> title == null ? null : builder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<PostEntity> hasAuthor(UserEntity author) {
        return (root, query, builder) -> author == null ? null : builder.equal(root.get("author"), author);
    }

    public static Specification<PostEntity> hasKeyword(String keyword) {
        return (root, query, builder) -> keyword == null ? null
                : builder.or(
                        builder.like(builder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%"),
                        builder.like(builder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"));
    }
}
