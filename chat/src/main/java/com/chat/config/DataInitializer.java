package com.chat.config;

import com.chat.model.GameOption;
import com.chat.repository.GameOptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// 数据初始化器
// 应用启动时自动填充 game_options 表，确保热门游戏可选
@Component
public class DataInitializer implements CommandLineRunner {

    private final GameOptionRepository gameOptionRepository;

    public DataInitializer(GameOptionRepository gameOptionRepository) {
        this.gameOptionRepository = gameOptionRepository;
    }

    @Override
    public void run(String... args) {
        // 已有数据则跳过，避免重复插入
        if (gameOptionRepository.count() > 0) return;

        // 预置 21 款热门游戏，按分类和序号排列
        List<GameOption> games = List.of(
            new GameOption("LOL", "英雄联盟", "MOBA", 1, "⚔️"),
            new GameOption("WZRY", "王者荣耀", "MOBA", 2, "🎮"),
            new GameOption("DOTA2", "Dota 2", "MOBA", 3, "🛡️"),
            new GameOption("PUBG", "绝地求生", "FPS", 4, "🪂"),
            new GameOption("PUBGM", "和平精英", "FPS", 5, "🔫"),
            new GameOption("CF", "穿越火线", "FPS", 6, "🔥"),
            new GameOption("VALORANT", "无畏契约", "FPS", 7, "💥"),
            new GameOption("OW", "守望先锋", "FPS", 8, "🎯"),
            new GameOption("NARAKA", "永劫无间", "动作", 9, "🗡️"),
            new GameOption("GENSHIN", "原神", "动作", 10, "✨"),
            new GameOption("WW", "鸣潮", "动作", 11, "🌊"),
            new GameOption("SR", "崩坏：星穹铁道", "RPG", 12, "🚂"),
            new GameOption("ZZZ", "绝区零", "RPG", 13, "⚡"),
            new GameOption("DNF", "地下城与勇士", "RPG", 14, "👊"),
            new GameOption("MHXY", "梦幻西游", "RPG", 15, "🐉"),
            new GameOption("JX3", "剑网3", "RPG", 16, "🏯"),
            new GameOption("JCC", "金铲铲之战", "策略", 17, "♟️"),
            new GameOption("HS", "炉石传说", "策略", 18, "🃏"),
            new GameOption("DZPD", "蛋仔派对", "休闲", 19, "🥚"),
            new GameOption("IDV", "第五人格", "休闲", 20, "🎭"),
            new GameOption("MC", "我的世界", "休闲", 21, "⛏️")
        );
        gameOptionRepository.saveAll(games);
    }
}
