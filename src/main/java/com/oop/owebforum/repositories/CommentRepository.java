package com.oop.owebforum.repositories;

import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOriginalPost(Post post);
}
