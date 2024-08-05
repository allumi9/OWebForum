package com.oop.owebforum.services;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Category;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.repositories.CommentRepository;
import com.oop.owebforum.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

import java.util.List;

import java.util.Optional;

@Service
public class PostService{
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    @Autowired
    public PostService(PostRepository postRepository,
                       CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post findByID(Long ID) throws Exception{
        if(postRepository.findById(ID).isEmpty()){
            throw new Exception("Post not found.");
        }
        return postRepository.findById(ID).get();
    }

    public void createPost(Post post, AppUser originalPoster) {
        post.setOriginalPoster(originalPoster);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public List<Post> getRecentPosts(){
        return postRepository.findAllByCreatedAtAfterOrderByCreatedAt(LocalDateTime.now().minusDays(1));
    }

    public List<Post> getAllPostsByCategoryOrderByRatingDesc(Category category){
        return postRepository.findAllByCategoryOrderByRatingDesc(category);
    }

    public void makePostModelToShow(Model model, Long id) throws Exception {
        Post post = findByID(id);
        List<Comment> comments = commentRepository.findAllByOriginalPostWithReplies(post);

        model.addAttribute("comments", comments);
        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("op", post.getOriginalPoster().getUsername());
        model.addAttribute("content", post.getContent());
        model.addAttribute("createdAt", post.getCreatedAt().withNano(0).toString());
        model.addAttribute("category", post.getCategory().getName());
        model.addAttribute("rating", post.getRating());
        model.addAttribute("postid", id);
    }

    public void makePostModelsToShowHome(Model model){
        List<Post> recentPosts = getRecentPosts();
        model.addAttribute("posts", recentPosts);
    }

}
