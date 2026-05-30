package com.chat.dto;

// 游戏信息响应体
public class GameResponse {

    private String code;
    private String name;
    private String icon;
    private Integer currentPlayers;
    private Boolean interested;    // 当前用户是否感兴趣

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getCurrentPlayers() { return currentPlayers; }
    public void setCurrentPlayers(Integer currentPlayers) { this.currentPlayers = currentPlayers; }
    public Boolean getInterested() { return interested; }
    public void setInterested(Boolean interested) { this.interested = interested; }
}
