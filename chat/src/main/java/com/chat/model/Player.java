package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 陪玩账号实体，映射 players 表
// phone / nickname / avatar / password 均通过 @OneToOne 关联 User 表获取
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // 陪玩账号主键

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;                    // 关联的用户账号

    @Column(length = 500)
    private String bio;                   // 个人简介

    private Integer hourlyRate;           // 每小时陪玩价格（元）

    @Column(length = 20)
    private String status = "OFFLINE";    // 状态: ONLINE / OFFLINE / BUSY

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;      // 创建时间

    private LocalDateTime updatedAt;      // 更新时间

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Integer getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Integer hourlyRate) { this.hourlyRate = hourlyRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
