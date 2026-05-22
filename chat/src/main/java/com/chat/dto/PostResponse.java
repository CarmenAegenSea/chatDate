package com.chat.dto;

import com.chat.model.Post;
import java.time.LocalDateTime;

// 帖子响应体
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
    private Integer likeCount;  // 点赞数
    private Boolean liked;       // 当前登录用户是否已赞

    // 将 Post 实体转换为响应 DTO（默认未点赞）
    public static PostResponse from(Post post) {
        PostResponse resp = new PostResponse();
        resp.setId(post.getId());
        resp.setTitle(post.getTitle());
        resp.setContent(post.getContent());
        resp.setAuthor(UserResponse.from(post.getAuthor()));
        resp.setCreatedAt(post.getCreatedAt());
        resp.setLikeCount(post.getLikeCount());
        resp.setLiked(false);
        return resp;
    }

    // 将 Post 实体转换为响应 DTO，指定点赞状态
    public static PostResponse from(Post post, boolean liked) {
        PostResponse resp = from(post);
        resp.setLiked(liked);
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public UserResponse getAuthor() { return author; }
    public void setAuthor(UserResponse author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
}
