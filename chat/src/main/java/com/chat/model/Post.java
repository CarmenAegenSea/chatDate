package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 帖子实体，映射 posts 表
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title; // 标题

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 内容

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author; // 作者

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 创建时间

    private LocalDateTime updatedAt; // 更新时间

    @Column(length = 20)
    private String mood; // 心情 / 标签

    @Column(length = 20)
    private String type; // 帖子类型: rant(吐槽墙) / wise(万事通) / sell(售卖墙)

    @Column(name = "`like`")
    private Integer likeCount = 0;

    private Integer bookmarkCount = 0;

    private Integer commentCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "post_likes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedByUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "post_bookmarks",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> bookmarkedByUsers = new HashSet<>();

    // 插入前自动设置时间
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // 更新前自动设置时间
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getter/Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
    public Integer getBookmarkCount() { return bookmarkCount; }
    public void setBookmarkCount(Integer bookmarkCount) { this.bookmarkCount = bookmarkCount; }
    public Integer getCommentCount() { return commentCount; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public Set<User> getLikedByUsers() { return likedByUsers; }
    public void setLikedByUsers(Set<User> likedByUsers) { this.likedByUsers = likedByUsers; }
    public Set<User> getBookmarkedByUsers() { return bookmarkedByUsers; }
    public void setBookmarkedByUsers(Set<User> bookmarkedByUsers) { this.bookmarkedByUsers = bookmarkedByUsers; }
}
