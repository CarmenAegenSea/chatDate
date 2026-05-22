package com.chat.repository;

import com.chat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 帖子数据访问接口
public interface PostRepository extends JpaRepository<Post, Long> {
    // 查询所有帖子，按创建时间倒序
    List<Post> findAllByOrderByCreatedAtDesc();
}
