package com.chat.service;

import com.chat.dto.CommentRequest;
import com.chat.dto.CommentResponse;
import com.chat.dto.PostRequest;
import com.chat.dto.PostResponse;
import com.chat.model.Comment;
import com.chat.model.Post;
import com.chat.model.User;
import com.chat.repository.CommentRepository;
import com.chat.repository.PostRepository;
import com.chat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 帖子业务逻辑：创建、列表、详情、点赞、评论
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 创建帖子
    public PostResponse create(Long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setMood(request.getMood());
        post.setAuthor(user);
        return PostResponse.from(postRepository.save(post));
    }

    // 获取帖子列表，按时间倒序
    @Transactional(readOnly = true)
    public List<PostResponse> list() {
        return list(null);
    }

    // 获取帖子列表，按时间倒序，带当前用户点赞状态
    @Transactional(readOnly = true)
    public List<PostResponse> list(Long currentUserId) {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> {
                    boolean liked = currentUserId != null
                            && post.getLikedByUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));
                    boolean bookmarked = currentUserId != null
                            && post.getBookmarkedByUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));
                    return PostResponse.from(post, liked, bookmarked);
                })
                .toList();
    }

    // 根据 ID 获取帖子详情
    public PostResponse getById(Long id) {
        return getById(id, null);
    }

    // 根据 ID 获取帖子详情，带当前用户点赞状态
    @Transactional(readOnly = true)
    public PostResponse getById(Long id, Long currentUserId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        boolean liked = currentUserId != null
                && post.getLikedByUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));
        boolean bookmarked = currentUserId != null
                && post.getBookmarkedByUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));
        return PostResponse.from(post, liked, bookmarked);
    }

    // 点赞/取消点赞，重复点击切换状态
    @Transactional
    public PostResponse like(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        boolean alreadyLiked = post.getLikedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        boolean bookmarked = post.getBookmarkedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyLiked) {
            post.getLikedByUsers().remove(user);
            post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
            return PostResponse.from(postRepository.save(post), false, bookmarked);
        }

        post.getLikedByUsers().add(user);
        post.setLikeCount(post.getLikeCount() == null ? 1 : post.getLikeCount() + 1);
        return PostResponse.from(postRepository.save(post), true, bookmarked);
    }

    // 收藏/取消收藏，切换状态
    @Transactional
    public PostResponse bookmark(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        boolean alreadyBookmarked = post.getBookmarkedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        boolean liked = post.getLikedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyBookmarked) {
            post.getBookmarkedByUsers().remove(user);
            post.setBookmarkCount(Math.max(0, post.getBookmarkCount() - 1));
            return PostResponse.from(postRepository.save(post), liked, false);
        }

        post.getBookmarkedByUsers().add(user);
        post.setBookmarkCount(post.getBookmarkCount() == null ? 1 : post.getBookmarkCount() + 1);
        return PostResponse.from(postRepository.save(post), liked, true);
    }

    // 获取帖子评论列表，可选传当前用户 ID 获取点赞状态
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId, Long currentUserId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(c -> {
                    boolean liked = currentUserId != null
                            && c.getLikedByUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));
                    return CommentResponse.from(c, liked);
                })
                .toList();
    }

    // 添加评论，后端记录评论作者，前端不展示
    @Transactional
    public CommentResponse addComment(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent(request.getContent());
        Comment saved = commentRepository.save(comment);
        post.setCommentCount(post.getCommentCount() == null ? 1 : post.getCommentCount() + 1);
        postRepository.save(post);
        return CommentResponse.from(saved);
    }

    // 点赞/取消点赞评论，切换状态
    @Transactional
    public CommentResponse likeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("评论不存在"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        boolean alreadyLiked = comment.getLikedByUsers().stream()
                .anyMatch(u -> u.getId().equals(userId));
        if (alreadyLiked) {
            comment.getLikedByUsers().remove(user);
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
            return CommentResponse.from(commentRepository.save(comment), false);
        }

        comment.getLikedByUsers().add(user);
        comment.setLikeCount(comment.getLikeCount() == null ? 1 : comment.getLikeCount() + 1);
        return CommentResponse.from(commentRepository.save(comment), true);
    }

    // 删除帖子（仅作者可删），级联删除评论及点赞关系
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权删除该帖子");
        }
        postRepository.delete(post);
    }

    // 删除评论（仅评论作者可删）
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("评论不存在"));
        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("评论不属于该帖子");
        }
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("无权删除该评论");
        }
        commentRepository.delete(comment);
        post.setCommentCount(Math.max(0, post.getCommentCount() - 1));
        postRepository.save(post);
    }
}
