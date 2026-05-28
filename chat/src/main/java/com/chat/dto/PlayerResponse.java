package com.chat.dto;

import com.chat.model.Player;
import com.chat.model.PlayerGame;
import java.time.LocalDateTime;
import java.util.List;

// 陪玩账号响应体
// 包含关联 User 的 phone / nickname / avatar 字段，前端无需额外查询
public class PlayerResponse {

    private Long id;                    // 陪玩账号 ID
    private Long userId;                // 关联的用户 ID
    private String phone;               // 手机号（来自 User）
    private String nickname;            // 昵称（来自 User）
    private String avatar;              // 头像 URL（来自 User）
    private String bio;                 // 个人简介
    private Integer hourlyRate;         // 时薪
    private String status;              // 在线状态
    private Boolean gamesSetup;         // 是否已设置喜欢的游戏
    private LocalDateTime createdAt;    // 创建时间
    private Long totalDuration;         // 总计陪玩时长（分钟）
    private List<GameInfo> games;       // 玩家选择的游戏列表

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
        r.setPhone(player.getUser().getPhone());
        r.setNickname(player.getUser().getNickname());
        r.setAvatar(player.getUser().getAvatar());
        r.setBio(player.getBio());
        r.setHourlyRate(player.getHourlyRate());
        r.setStatus(player.getStatus());
        r.setGamesSetup(gamesSetup);
        r.setCreatedAt(player.getCreatedAt());
        r.setTotalDuration(player.getTotalDuration());
        r.setGames(games.stream().map(pg -> new GameInfo(pg.getGameCode(), pg.getSkillLevel())).toList());
        return r;
    }

    // 游戏信息内部类
    public static class GameInfo {
        private String gameCode;
        private String gameName;
        private String skillLevel;

        public GameInfo() {}

        public GameInfo(String gameCode, String skillLevel) {
            this.gameCode = gameCode;
            this.skillLevel = skillLevel;
        }

        public String getGameCode() { return gameCode; }
        public void setGameCode(String gameCode) { this.gameCode = gameCode; }
        public String getGameName() { return gameName; }
        public void setGameName(String gameName) { this.gameName = gameName; }
        public String getSkillLevel() { return skillLevel; }
        public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
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
