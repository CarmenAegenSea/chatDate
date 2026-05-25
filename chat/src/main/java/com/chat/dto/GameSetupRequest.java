package com.chat.dto;

import java.util.List;

// 首次设置游戏资料请求体
public class GameSetupRequest {

    private List<Long> favoriteGameIds; // 喜欢的游戏 ID 列表
    private Boolean isCompanion; // 是否成为陪玩
    private String bio; // 陪玩简介

    public List<Long> getFavoriteGameIds() { return favoriteGameIds; }
    public void setFavoriteGameIds(List<Long> favoriteGameIds) { this.favoriteGameIds = favoriteGameIds; }
    public Boolean getIsCompanion() { return isCompanion; }
    public void setIsCompanion(Boolean isCompanion) { this.isCompanion = isCompanion; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
