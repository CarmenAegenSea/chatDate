package com.chat.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

// 用户游戏资料实体，映射 game_players 表，与 User 一对一共享主键
@Entity
@Table(name = "game_players")
public class GamePlayer {

    @Id
    private Long userId; // 用户 ID（与 User 表共享主键）

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user; // 关联的用户信息（昵称/头像等复用 User 表）

    @Column(nullable = false)
    private Boolean isCompanion = false; // 是否愿意成为陪玩

    @Column(length = 200)
    private String bio; // 陪玩简介

    @ManyToMany
    @JoinTable(name = "game_player_favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Set<Game> favoriteGames = new HashSet<>(); // 用户喜欢的游戏列表

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Boolean getIsCompanion() { return isCompanion; }
    public void setIsCompanion(Boolean isCompanion) { this.isCompanion = isCompanion; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Set<Game> getFavoriteGames() { return favoriteGames; }
    public void setFavoriteGames(Set<Game> favoriteGames) { this.favoriteGames = favoriteGames; }
}
