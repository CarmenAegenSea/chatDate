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
    private String mood;
    private Integer likeCount;
    private Boolean liked;
    private Integer bookmarkCount;
    private Boolean bookmarked;
    private Integer commentCount;

    public static PostResponse from(Post post) {
        PostResponse resp = new PostResponse();
        resp.setId(post.getId());
        resp.setTitle(post.getTitle());
        resp.setContent(post.getContent());
        resp.setAuthor(UserResponse.from(post.getAuthor()));
        resp.setCreatedAt(post.getCreatedAt());
        resp.setMood(post.getMood());
        resp.setLikeCount(post.getLikeCount());
        resp.setBookmarkCount(post.getBookmarkCount());
        resp.setCommentCount(post.getCommentCount());
        resp.setLiked(false);
        resp.setBookmarked(false);
        return resp;
    }

    public static PostResponse from(Post post, boolean liked, boolean bookmarked) {
        PostResponse resp = from(post);
        resp.setLiked(liked);
        resp.setBookmarked(bookmarked);
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
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }
    public Integer getBookmarkCount() { return bookmarkCount; }
    public void setBookmarkCount(Integer bookmarkCount) { this.bookmarkCount = bookmarkCount; }
    public Boolean getBookmarked() { return bookmarked; }
    public void setBookmarked(Boolean bookmarked) { this.bookmarked = bookmarked; }
    public Integer getCommentCount() { return commentCount; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
}
