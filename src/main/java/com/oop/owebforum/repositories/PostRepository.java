package com.oop.owebforum.repositories;


import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    List<Post> findAllByCategory(Category category);
    List<Post> findAllByOriginalPoster(AppUser appUser);
    List<Post> findAllByCategoryOrderByRatingDesc(Category category);
    List<Post> findAllByOriginalPosterOrderByRatingDesc(AppUser appUser);
    List<Post> findAllByCreatedAtAfterOrderByCreatedAt(LocalDateTime localDateTime);
}