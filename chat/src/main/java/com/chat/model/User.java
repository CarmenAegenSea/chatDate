package com.chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 用户实体，映射 users 表
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"oauth_provider", "oauth_open_id"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String phone; // 手机号，登录凭据，唯一（可为空，第三方登录用户无手机号）

    @Column(nullable = false, length = 32)
    private String nickname; // 昵称

    @Column(length = 500)
    private String avatar; // 头像 URL

    @Column(length = 128)
    private String password; // BCrypt 加密后的密码（可为空，第三方登录用户无密码）

    private Long balance = 0L; // 余额

    @Column(length = 20)
    private String oauthProvider; // 第三方登录提供商：wechat / qq

    @Column(length = 64)
    private String oauthOpenId; // 第三方登录 OpenID

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 注册时间

    private LocalDateTime updatedAt; // 信息更新时间

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

    // Setter和Getter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }
    public String getOauthProvider() { return oauthProvider; }
    public void setOauthProvider(String oauthProvider) { this.oauthProvider = oauthProvider; }
    public String getOauthOpenId() { return oauthOpenId; }
    public void setOauthOpenId(String oauthOpenId) { this.oauthOpenId = oauthOpenId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
