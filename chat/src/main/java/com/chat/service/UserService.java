package com.chat.service;

import com.chat.dto.NicknameUpdateRequest;
import com.chat.dto.OAuthLoginRequest;
import com.chat.dto.PasswordLoginRequest;
import com.chat.dto.UserResponse;
import com.chat.model.User;
import com.chat.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// 用户业务逻辑：登录、修改昵称
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // 第三方登录（微信/QQ），首次登录自动注册
    public UserResponse oauthLogin(OAuthLoginRequest request) {
        String provider = request.getProvider();
        String openId = request.getOpenId();
        User user = userRepository.findByOauthProviderAndOauthOpenId(provider, openId).orElse(null);
        if (user == null) {
            user = new User();
            user.setOauthProvider(provider);
            user.setOauthOpenId(openId);
            user.setPhone("oauth_" + provider + "_" + openId.substring(Math.max(0, openId.length() - 7)));
            user.setNickname(request.getNickname() != null ? request.getNickname() : provider + "_" + openId.substring(0, 8));
            user.setAvatar(request.getAvatar());
            user = userRepository.save(user);
        }
        return UserResponse.from(user);
    }

    // 手机号+密码注册
    public UserResponse register(PasswordLoginRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("该手机号已注册");
        }
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname("用户" + request.getPhone().substring(7));
        return UserResponse.from(userRepository.save(user));
    }

    // 手机号+密码登录，仅已注册用户可登录
    public UserResponse loginByPassword(PasswordLoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("该手机号未注册"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return UserResponse.from(user);
    }

    // 充值余额
    public UserResponse topUpBalance(Long userId, Long amount) {
        if (amount == null || amount <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setBalance(user.getBalance() == null ? amount : user.getBalance() + amount);
        return UserResponse.from(userRepository.save(user));
    }

    // 修改昵称
    public UserResponse updateNickname(Long userId, NicknameUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setNickname(request.getNickname());
        return UserResponse.from(userRepository.save(user));
    }
}
