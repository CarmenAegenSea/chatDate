package com.chat.service;

import com.chat.dto.GameOptionResponse;
import com.chat.repository.GameOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// 游戏选项业务逻辑层
// 提供游戏列表查询，数据由 DataInitializer 在启动时初始化
@Service
public class GameOptionService {

    private final GameOptionRepository gameOptionRepository;

    public GameOptionService(GameOptionRepository gameOptionRepository) {
        this.gameOptionRepository = gameOptionRepository;
    }

    // 获取所有游戏选项，按分类分组、分类内按序号排序
    public List<GameOptionResponse> getAll() {
        return gameOptionRepository.findAllByOrderByCategoryAscSortOrderAsc().stream()
                .map(GameOptionResponse::from)
                .toList();
    }
}
