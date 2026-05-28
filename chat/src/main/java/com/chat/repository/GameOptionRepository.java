package com.chat.repository;

import com.chat.model.GameOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 游戏选项数据访问接口
public interface GameOptionRepository extends JpaRepository<GameOption, Long> {
    // 查询所有游戏，按分类分组、分类内按序号排序
    List<GameOption> findAllByOrderByCategoryAscSortOrderAsc();

    // 按代码查找游戏
    Optional<GameOption> findByCode(String code);
}
