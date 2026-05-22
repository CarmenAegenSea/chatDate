package com.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 第三方登录（微信/QQ）请求体
public class OAuthLoginRequest {

    @NotBlank(message = "provider 不能为空")
    private String provider;  // wechat 或 qq

    @NotBlank(message = "openId 不能为空")
    private String openId;

    @Size(max = 32, message = "昵称不能超过32字")
    private String nickname;  // 可选，首次注册时使用的昵称

    private String avatar;    // 可选，头像 URL

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getOpenId() { return openId; }
    public void setOpenId(String openId) { this.openId = openId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
