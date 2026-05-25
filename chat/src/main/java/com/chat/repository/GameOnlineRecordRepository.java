package com.chat.repository;

import com.chat.model.GameOnlineRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// 游戏在线人数记录数据访问接口
public interface GameOnlineRecordRepository extends JpaRepository<GameOnlineRecord, Long> {
    // 查询某游戏在指定时间之后的在线人数记录，按时间升序排列
    List<GameOnlineRecord> findByGameIdAndRecordedAtAfterOrderByRecordedAtAsc(Long gameId, LocalDateTime after);
}
