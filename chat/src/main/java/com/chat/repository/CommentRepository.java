package com.chat.repository;

import com.chat.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 评论数据访问接口
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 查询指定帖子的所有评论，按时间升序排列
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}
