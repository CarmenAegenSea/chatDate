package com.chat.dto;

import com.chat.model.GamePlayer;

import java.util.List;
import java.util.stream.Collectors;

// 用户游戏资料响应体（含喜欢的游戏列表）
public class GamePlayerResponse {

    private Long userId; // 用户 ID
    private String nickname; // 昵称（来自 User）
    private String avatar; // 头像（来自 User）
    private Boolean isCompanion; // 是否陪玩
    private String bio; // 陪玩简介
    private List<GameResponse> favoriteGames; // 喜欢的游戏列表

    // 将实体转换为响应 DTO
    public static GamePlayerResponse from(GamePlayer gp) {
        GamePlayerResponse r = new GamePlayerResponse();
        r.setUserId(gp.getUserId());
        r.setNickname(gp.getUser().getNickname());
        r.setAvatar(gp.getUser().getAvatar());
        r.setIsCompanion(gp.getIsCompanion());
        r.setBio(gp.getBio());
        r.setFavoriteGames(gp.getFavoriteGames().stream().map(GameResponse::from).collect(Collectors.toList()));
        return r;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Boolean getIsCompanion() { return isCompanion; }
    public void setIsCompanion(Boolean isCompanion) { this.isCompanion = isCompanion; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public List<GameResponse> getFavoriteGames() { return favoriteGames; }
    public void setFavoriteGames(List<GameResponse> favoriteGames) { this.favoriteGames = favoriteGames; }
}
