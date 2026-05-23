package com.chat.dto;

import com.chat.model.Comment;
import java.time.LocalDateTime;

// 评论响应体
public class CommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private Boolean liked;
    private Long authorId;

    public static CommentResponse from(Comment comment) {
        CommentResponse resp = new CommentResponse();
        resp.setId(comment.getId());
        resp.setContent(comment.getContent());
        resp.setCreatedAt(comment.getCreatedAt());
        resp.setLikeCount(comment.getLikeCount());
        resp.setLiked(false);
        resp.setAuthorId(comment.getAuthor().getId());
        return resp;
    }

    public static CommentResponse from(Comment comment, boolean liked) {
        CommentResponse resp = from(comment);
        resp.setLiked(liked);
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}
