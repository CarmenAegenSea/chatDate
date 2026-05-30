package com.chat.repository;

import com.chat.model.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 用户游戏兴趣数据访问接口
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    // 按用户 ID 查找兴趣列表
    List<UserInterest> findByUserId(Long userId);

    // 判断用户是否设置过兴趣
    boolean existsByUserId(Long userId);

    // 删除用户所有兴趣（修改兴趣时先删后插）
    void deleteByUserId(Long userId);
}
