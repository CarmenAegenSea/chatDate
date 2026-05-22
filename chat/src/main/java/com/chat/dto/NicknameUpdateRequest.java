package com.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 修改昵称请求体
public class NicknameUpdateRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 32, message = "昵称不能超过32字")
    private String nickname;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
