package com.chat.dto;

import com.chat.model.Player;
import java.time.LocalDateTime;

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

    public static PlayerResponse from(Player player) {
        return from(player, false);
    }

    public static PlayerResponse from(Player player, boolean gamesSetup) {
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
        return r;
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
}
