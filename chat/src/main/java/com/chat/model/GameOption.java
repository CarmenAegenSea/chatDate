package com.chat.model;

import jakarta.persistence.*;

// 游戏选项实体，映射 game_options 表
// 存储平台预置的热门游戏，每个游戏有唯一 code 和分类
@Entity
@Table(name = "game_options")
public class GameOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // 主键

    @Column(nullable = false, unique = true, length = 20)
    private String code;                  // 游戏唯一代码，如 LOL / WZRY

    @Column(nullable = false, length = 100)
    private String name;                  // 游戏名称，如 英雄联盟

    @Column(length = 50)
    private String category;              // 游戏分类，如 MOBA / FPS / RPG

    @Column(name = "sort_order")
    private Integer sortOrder;            // 排序序号，同一分类内按此排序

    @Column(length = 10)
    private String icon;                  // 游戏图标（emoji）

    public GameOption() {}

    public GameOption(String code, String name, String category, Integer sortOrder, String icon) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.sortOrder = sortOrder;
        this.icon = icon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
