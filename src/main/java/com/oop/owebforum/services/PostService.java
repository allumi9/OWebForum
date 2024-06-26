package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.Optional;

@Service
public class PostService implements  IPostService{
    private PostRepository postRepository;
    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> loadPostById(Long id){
        return postRepository.findById(id);
    }

    public void createPost(Post post, AppUser originalPoster) {
        post.setOriginalPoster(originalPoster);
        postRepository.save(post);
    }

    public List<Post> getRecentPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc(); // Assuming you have a createdAt field in Post entity
    }

}
