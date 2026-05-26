package com.chat.config;

import com.chat.model.Game;
import com.chat.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// 游戏数据初始化器，应用启动时自动向 games 表插入预置游戏
@Component
public class GameDataSeeder implements CommandLineRunner {

    private final GameRepository gameRepository;

    public GameDataSeeder(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) {
        if (gameRepository.count() > 0) return;

        String[][] games = {
            {"王者荣耀", "🎮"},
            {"和平精英", "🔫"},
            {"原神", "✨"},
            {"英雄联盟", "⚔️"},
            {"绝地求生", "🪂"},
            {"永劫无间", "🗡️"}
        };

        for (String[] g : games) {
            Game game = new Game();
            game.setName(g[0]);
            game.setIcon(g[1]);
            gameRepository.save(game);
        }
    }
}
