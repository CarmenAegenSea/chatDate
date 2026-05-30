package com.chat.repository;

import com.chat.model.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

// 频道成员数据访问接口
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    // 统计某频道的成员数
    long countByGameCode(String gameCode);

    // 判断用户是否已加入某频道
    boolean existsByUserIdAndGameCode(Long userId, String gameCode);
}
