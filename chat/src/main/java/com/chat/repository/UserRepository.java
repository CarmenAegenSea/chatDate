package com.chat.repository;

import com.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 用户数据访问接口
public interface UserRepository extends JpaRepository<User, Long> {
    // 按手机号查询用户
    Optional<User> findByPhone(String phone);

    // 判断手机号是否已注册
    boolean existsByPhone(String phone);

    // 按第三方登录提供商标识查找用户
    Optional<User> findByOauthProviderAndOauthOpenId(String oauthProvider, String oauthOpenId);
}
