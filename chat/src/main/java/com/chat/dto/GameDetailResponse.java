package com.chat.dto;

import java.util.List;

// 游戏详情响应体（含陪玩列表）
public class GameDetailResponse {

    private GameResponse game; // 游戏信息
    private List<CompanionInfo> companions; // 可接单的陪玩列表

    public GameResponse getGame() { return game; }
    public void setGame(GameResponse game) { this.game = game; }
    public List<CompanionInfo> getCompanions() { return companions; }
    public void setCompanions(List<CompanionInfo> companions) { this.companions = companions; }

    // 陪玩人员信息
    public static class CompanionInfo {
        private Long userId; // 陪玩用户 ID
        private String nickname; // 昵称
        private String avatar; // 头像
        private String bio; // 陪玩简介

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
    }
}
