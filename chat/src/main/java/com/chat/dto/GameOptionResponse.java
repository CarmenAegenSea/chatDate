package com.chat.dto;

import com.chat.model.GameOption;

// 游戏选项响应体
// 返回给前端供选项卡展示的游戏列表
public class GameOptionResponse {

    private Long id;                      // 主键
    private String code;                  // 游戏代码
    private String name;                  // 游戏名称
    private String category;              // 游戏分类
    private String icon;                  // 游戏图标

    public static GameOptionResponse from(GameOption g) {
        GameOptionResponse r = new GameOptionResponse();
        r.setId(g.getId());
        r.setCode(g.getCode());
        r.setName(g.getName());
        r.setCategory(g.getCategory());
        r.setIcon(g.getIcon());
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
