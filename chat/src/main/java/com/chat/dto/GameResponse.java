package com.chat.dto;

// 游戏信息响应体
public class GameResponse {

    private String code; // 游戏代码
    private String name; // 游戏名称
    private String icon; // 游戏图标
    private Integer currentPlayers; // 当前频道人数

    public static GameResponse from(GameOptionResponse option) {
        GameResponse r = new GameResponse();
        r.setCode(option.getCode());
        r.setName(option.getName());
        r.setIcon(option.getIcon());
        r.setCurrentPlayers(0);
        return r;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }
}
