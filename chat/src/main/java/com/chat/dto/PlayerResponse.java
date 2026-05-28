package com.chat.dto;

import com.chat.model.Player;
import com.chat.model.PlayerGame;
import java.time.LocalDateTime;
import java.util.List;

// 陪玩账号响应体
public class PlayerResponse {

    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
    private String bio;
    private Integer hourlyRate;
    private String status;
    private Boolean gamesSetup;
    private LocalDateTime createdAt;
    private Long totalDuration;
    private List<GameInfo> games;

    public static PlayerResponse from(Player player) {
        return from(player, false, List.of());
    }

    public static PlayerResponse from(Player player, boolean gamesSetup) {
        return from(player, gamesSetup, List.of());
    }

    public static PlayerResponse from(Player player, boolean gamesSetup, List<PlayerGame> games) {
        PlayerResponse r = new PlayerResponse();
        r.setId(player.getId());
        r.setUserId(player.getUser().getId());
        r.setNickname(player.getUser().getNickname());
        r.setAvatar(player.getUser().getAvatar());
        r.setBio(player.getBio());
        r.setHourlyRate(player.getHourlyRate());
        r.setStatus(player.getStatus());
        r.setGamesSetup(gamesSetup);
        r.setCreatedAt(player.getCreatedAt());
        r.setTotalDuration(player.getTotalDuration());
        r.setGames(games.stream().map(pg -> {
            GameInfo info = new GameInfo();
            info.setGameCode(pg.getGameCode());
            info.setSkillLevel(pg.getSkillLevel());
            info.setIsCompanion(pg.getIsCompanion());
            info.setQualifications(pg.getQualifications());
            info.setAcceptedRules(pg.getAcceptedRules());
            return info;
        }).toList());
        return r;
    }

    public static class GameInfo {
        private String gameCode;
        private String gameName;
        private String skillLevel;
        private Boolean isCompanion;
        private String qualifications;
        private Boolean acceptedRules;

        public String getGameCode() { return gameCode; }
        public void setGameCode(String gameCode) { this.gameCode = gameCode; }
        public String getGameName() { return gameName; }
        public void setGameName(String gameName) { this.gameName = gameName; }
        public String getSkillLevel() { return skillLevel; }
        public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
        public Boolean getIsCompanion() { return isCompanion; }
        public void setIsCompanion(Boolean isCompanion) { this.isCompanion = isCompanion; }
        public String getQualifications() { return qualifications; }
        public void setQualifications(String qualifications) { this.qualifications = qualifications; }
        public Boolean getAcceptedRules() { return acceptedRules; }
        public void setAcceptedRules(Boolean acceptedRules) { this.acceptedRules = acceptedRules; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Integer getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Integer hourlyRate) { this.hourlyRate = hourlyRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getGamesSetup() { return gamesSetup; }
    public void setGamesSetup(Boolean gamesSetup) { this.gamesSetup = gamesSetup; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
    public List<GameInfo> getGames() { return games; }
    public void setGames(List<GameInfo> games) { this.games = games; }
}
