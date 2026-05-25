package com.chat.dto;

import com.chat.model.Game;

// 游戏信息响应体
public class GameResponse {

    private Long id; // 游戏 ID
    private String name; // 游戏名称
    private String icon; // 游戏图标
    private Integer currentPlayers; // 当前在线人数

    // 将游戏实体转换为响应 DTO
    public static GameResponse from(Game game) {
        GameResponse r = new GameResponse();
        r.setId(game.getId());
        r.setName(game.getName());
        r.setIcon(game.getIcon());
        r.setCurrentPlayers(game.getCurrentPlayers());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }
}
