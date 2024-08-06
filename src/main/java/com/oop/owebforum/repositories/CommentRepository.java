package com.oop.owebforum.repositories;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByOriginalPost(Post post);
    List<Comment> findAllByAppUser(AppUser appUser);
    List<Comment> findAllByOriginalComment(Comment comment);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.originalPost = :post AND c.originalComment IS NULL")
    List<Comment> findAllByOriginalPostWithReplies(@Param("post") Post post);

}
