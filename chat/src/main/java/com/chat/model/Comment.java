package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// 评论实体，映射 comments 表
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // 主键

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;                    // 所属帖子

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;                  // 评论作者

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;               // 评论内容

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;      // 评论时间

    private Integer likeCount = 0;        // 点赞数

    @ManyToMany
    @JoinTable(name = "comment_likes",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedByUsers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Set<User> getLikedByUsers() { return likedByUsers; }
    public void setLikedByUsers(Set<User> likedByUsers) { this.likedByUsers = likedByUsers; }
}
