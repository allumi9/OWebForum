package com.oop.owebforum.services.voteService;

import com.oop.owebforum.entities.AppUser;
import com.oop.owebforum.entities.Comment;
import com.oop.owebforum.entities.Post;
import com.oop.owebforum.entities.Vote;
import com.oop.owebforum.enums.VoteState;
import com.oop.owebforum.repositories.AppUserRepository;
import com.oop.owebforum.repositories.PostRepository;
import com.oop.owebforum.repositories.VoteRepository;
import com.oop.owebforum.services.AppUserService;
import com.oop.owebforum.services.CommentService;
import com.oop.owebforum.services.PostService;
import com.oop.owebforum.services.VoteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTests {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private CommentService commentService;
    @Mock
    private PostService postService;
    @Mock
    private PostRepository postRepository; // DON'T DELETE - it is used in the VoteService void methods
    @Mock
    private AppUserRepository appUserRepository; // DON'T DELETE - it is used in the VoteService void methods
    @InjectMocks
    private VoteService voteService;

    private AppUser appUser;
    private Post post;
    private Comment comment;

    @BeforeEach
    public void setUp() throws Exception {
        appUser = new AppUser();
        post = new Post();
        comment = new Comment();

        when(appUserService.loadUserByUsername(anyString())).thenReturn(appUser);
        when(postService.findByID(anyLong())).thenReturn(post);
    }

    // Downvote scenarios
    @Test
    public void testChangePostRating_DownvoteOnNullVote_ShouldSubtractOne() throws Exception {
        // Arrange
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.empty());
        post.setRating(0);

        // Act
        voteService.changePostRating(1L, "username", "down");

        // Assert
        Assertions.assertEquals(-1, post.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangePostRating_DownvoteOnDownVote_ShouldAddOne() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.DOWN).build();
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.of(vote));
        post.setRating(-1);

        // Act
        voteService.changePostRating(1L, "username", "down");

        // Assert
        Assertions.assertEquals(0, post.getRating());
        verify(voteRepository).delete(any(Vote.class));
    }

    @Test
    public void testChangePostRating_DownvoteOnUpVote_ShouldSubtractTwo() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.UP).build();
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.of(vote));
        post.setRating(1);

        // Act
        voteService.changePostRating(1L, "username", "down");

        // Assert
        Assertions.assertEquals(-1, post.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    // Upvote scenarios
    @Test
    public void testChangePostRating_UpvoteOnNullVote_ShouldAddOne() throws Exception {
        // Arrange
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.empty());
        post.setRating(0);

        // Act
        voteService.changePostRating(1L, "username", "up");

        // Assert
        Assertions.assertEquals(1, post.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangePostRating_UpvoteOnDownVote_ShouldAddTwo() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.DOWN).build();
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.of(vote));
        post.setRating(-1);

        // Act
        voteService.changePostRating(1L, "username", "up");

        // Assert
        Assertions.assertEquals(1, post.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangePostRating_UpvoteOnUpVote_ShouldSubtractOne() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.UP).build();
        when(voteRepository.findByVoterAndPost(appUser, post)).thenReturn(Optional.of(vote));
        post.setRating(1);

        // Act
        voteService.changePostRating(1L, "username", "up");

        // Assert
        Assertions.assertEquals(0, post.getRating());
        verify(voteRepository).delete(any(Vote.class));
    }

    // Comment rating:

    // Downvote scenarios
    @Test
    public void testChangeCommentRating_DownvoteOnNullVote_ShouldSubtractOne() throws Exception {
        // Arrange
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.empty());
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(0);

        // Act
        voteService.changeCommentRating(1L, "username", "down", 1L);

        // Assert
        Assertions.assertEquals(-1, comment.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangeCommentRating_DownvoteOnDownVote_ShouldAddOne() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.DOWN).build();
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.of(vote));
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(-1);

        // Act
        voteService.changeCommentRating(1L, "username", "down", 1L);

        // Assert
        Assertions.assertEquals(0, comment.getRating());
        verify(voteRepository).delete(any(Vote.class));
    }

    @Test
    public void testChangeCommentRating_DownvoteOnUpVote_ShouldSubtractTwo() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.UP).build();
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.of(vote));
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(1);

        // Act
        voteService.changeCommentRating(1L, "username", "down", 1L);

        // Assert
        Assertions.assertEquals(-1, comment.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    // Upvote scenarios
    @Test
    public void testChangeCommentRating_UpvoteOnNullVote_ShouldAddOne() throws Exception {
        // Arrange
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.empty());
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(0);

        // Act
        voteService.changeCommentRating(1L, "username", "up", 1L);

        // Assert
        Assertions.assertEquals(1, comment.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangeCommentRating_UpvoteOnDownVote_ShouldAddTwo() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.DOWN).build();
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.of(vote));
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(-1);

        // Act
        voteService.changeCommentRating(1L, "username", "up", 1L);

        // Assert
        Assertions.assertEquals(1, comment.getRating());
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    public void testChangeCommentRating_UpvoteOnUpVote_ShouldSubtractOne() throws Exception {
        // Arrange
        Vote vote = Vote.builder().state(VoteState.UP).build();
        when(voteRepository.findByVoterAndComment(appUser, comment)).thenReturn(Optional.of(vote));
        when(commentService.findCommentById(anyLong())).thenReturn(comment);
        comment.setRating(1);

        // Act
        voteService.changeCommentRating(1L, "username", "up", 1L);

        // Assert
        Assertions.assertEquals(0, comment.getRating());
        verify(voteRepository).delete(any(Vote.class));
    }
}
