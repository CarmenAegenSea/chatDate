package com.chat.repository;

import com.chat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 帖子数据访问接口
public interface PostRepository extends JpaRepository<Post, Long> {
    // 查询所有帖子，按创建时间倒序
    List<Post> findAllByOrderByCreatedAtDesc();

    // 按类型查询帖子（含NULL类型旧帖），按创建时间倒序
    @Query("SELECT p FROM Post p WHERE p.type = :type OR p.type IS NULL ORDER BY p.createdAt DESC")
    List<Post> findByTypeIncludingNullOrderByCreatedAtDesc(@Param("type") String type);
}
