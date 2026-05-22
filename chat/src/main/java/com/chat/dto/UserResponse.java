package com.chat.dto;

import com.chat.model.User;

// 用户信息响应体
public class UserResponse {

    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
    private Long balance;    // 余额

    // 将 User 实体转换为响应 DTO
    public static UserResponse from(User user) {
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setPhone(user.getPhone());
        resp.setNickname(user.getNickname());
        resp.setAvatar(user.getAvatar());
        resp.setBalance(user.getBalance());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Long getBalance() { return balance; }
    public void setBalance(Long balance) { this.balance = balance; }
}
