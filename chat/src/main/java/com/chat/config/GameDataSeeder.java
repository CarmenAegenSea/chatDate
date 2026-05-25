package com.chat.config;

import com.chat.model.Game;
import com.chat.model.GameOnlineRecord;
import com.chat.repository.GameOnlineRecordRepository;
import com.chat.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

// 游戏数据初始化器，应用启动时自动向 games 表插入预置游戏及样本在线记录
@Component
public class GameDataSeeder implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final GameOnlineRecordRepository gameOnlineRecordRepository;

    public GameDataSeeder(GameRepository gameRepository,
                          GameOnlineRecordRepository gameOnlineRecordRepository) {
        this.gameRepository = gameRepository;
        this.gameOnlineRecordRepository = gameOnlineRecordRepository;
    }

    @Override
    public void run(String... args) {
        // 已有数据则跳过
        if (gameRepository.count() > 0) return;

        // 预置 6 款热门游戏
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
            game.setCurrentPlayers(0);
            gameRepository.save(game);
        }

        // 为每款游戏生成过去一小时的样本在线记录（每 5 分钟一个数据点）
        List<Game> savedGames = gameRepository.findAll();
        Random random = new Random(42);
        LocalDateTime now = LocalDateTime.now();

        for (Game game : savedGames) {
            int base = 10 + random.nextInt(50);
            for (int i = 12; i >= 0; i--) {
                GameOnlineRecord r = new GameOnlineRecord();
                r.setGame(game);
                r.setRecordedAt(now.minusMinutes(5L * i));
                int variance = random.nextInt(21) - 10;
                r.setPlayerCount(Math.max(1, base + variance));
                gameOnlineRecordRepository.save(r);
            }
            // 初始在线人数设为 base 附近的随机值
            game.setCurrentPlayers(base + random.nextInt(21) - 10);
            gameRepository.save(game);
        }
    }
}
