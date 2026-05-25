package com.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

// 游戏详情响应体（含在线人数记录和陪玩列表）
public class GameDetailResponse {

    private GameResponse game; // 游戏信息
    private List<OnlineRecord> records; // 过去一小时在线人数记录
    private List<CompanionInfo> companions; // 可接单的陪玩列表

    public GameResponse getGame() { return game; }
    public void setGame(GameResponse game) { this.game = game; }
    public List<OnlineRecord> getRecords() { return records; }
    public void setRecords(List<OnlineRecord> records) { this.records = records; }
    public List<CompanionInfo> getCompanions() { return companions; }
    public void setCompanions(List<CompanionInfo> companions) { this.companions = companions; }

    // 在线人数记录
    public static class OnlineRecord {
        private LocalDateTime recordedAt; // 记录时间
        private Integer playerCount; // 当时在线人数

        public LocalDateTime getRecordedAt() { return recordedAt; }
        public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
        public Integer getPlayerCount() { return playerCount; }
        public void setPlayerCount(Integer playerCount) { this.playerCount = playerCount; }
    }

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
