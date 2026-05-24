package com.chat.model;

import jakarta.persistence.*;

// 玩家-游戏关联实体，映射 player_games 表
// 记录陪玩玩家选择的游戏及对应的技术水平
@Entity
@Table(name = "player_games",
       uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "game_code"}))
public class PlayerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // 主键

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;                // 所属陪玩账号

    @Column(name = "game_code", nullable = false, length = 20)
    private String gameCode;              // 游戏代码，关联 game_options.code

    @Column(length = 20)
    private String skillLevel;            // 技术水平: 入门 / 熟悉 / 精通 / 大神

    public PlayerGame() {}

    public PlayerGame(Player player, String gameCode, String skillLevel) {
        this.player = player;
        this.gameCode = gameCode;
        this.skillLevel = skillLevel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
}
