package com.chat.model;

import jakarta.persistence.*;

// 玩家-游戏关联实体，映射 player_games 表
// 记录玩家加入的频道及陪玩信息
@Entity
@Table(name = "player_games",
       uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "game_code"}))
public class PlayerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "game_code", nullable = false, length = 20)
    private String gameCode;

    @Column(nullable = false)
    private Boolean isCompanion = false;

    @Column(length = 500)
    private String qualifications;

    @Column(nullable = false)
    private Boolean acceptedRules = false;

    @Column(length = 20)
    private String skillLevel;

    public PlayerGame() {}

    public PlayerGame(Player player, String gameCode) {
        this.player = player;
        this.gameCode = gameCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public String getGameCode() { return gameCode; }
    public void setGameCode(String gameCode) { this.gameCode = gameCode; }
    public Boolean getIsCompanion() { return isCompanion; }
    public void setIsCompanion(Boolean isCompanion) { this.isCompanion = isCompanion; }
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    public Boolean getAcceptedRules() { return acceptedRules; }
    public void setAcceptedRules(Boolean acceptedRules) { this.acceptedRules = acceptedRules; }
    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
}
