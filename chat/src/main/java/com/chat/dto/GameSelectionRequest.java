package com.chat.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

// 保存游戏选择请求体
// 前端提交玩家选择的游戏列表及对应水平
public class GameSelectionRequest {

    @NotNull(message = "游戏列表不能为空")
    @Valid
    private List<GameItem> games;         // 选中的游戏列表

    public List<GameItem> getGames() { return games; }
    public void setGames(List<GameItem> games) { this.games = games; }

    // 单个游戏选择项
    public static class GameItem {
        @NotBlank(message = "游戏代码不能为空")
        private String gameCode;          // 游戏代码

        private String skillLevel;        // 技术水平: 入门 / 熟悉 / 精通 / 大神

        public String getGameCode() { return gameCode; }
        public void setGameCode(String gameCode) { this.gameCode = gameCode; }
        public String getSkillLevel() { return skillLevel; }
        public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }
    }
}
