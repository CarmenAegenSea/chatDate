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
        private Long playerId; // 陪玩账号 ID
        private Long userId; // 用户 ID
        private String nickname; // 昵称
        private String avatar; // 头像
        private String bio; // 陪玩简介
        private String skillLevel; // 技术水平
        private String status; // 在线状态
        private Integer hourlyRate; // 时薪

        public Long getPlayerId() { return playerId; }
        public void setPlayerId(Long playerId) { this.playerId = playerId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public String getSkillLevel() { return skillLevel; }
        public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Integer getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(Integer hourlyRate) { this.hourlyRate = hourlyRate; }
    }
}
