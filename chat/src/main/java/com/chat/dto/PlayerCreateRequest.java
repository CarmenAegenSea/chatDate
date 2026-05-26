package com.chat.dto;

import jakarta.validation.constraints.Size;

// 创建陪玩账号请求体
// 前端填写的个人资料，userId 通过请求参数传递
public class PlayerCreateRequest {

    @Size(max = 500, message = "个人简介不能超过500字")
    private String bio;                   // 个人简介

    private Integer hourlyRate;           // 每小时陪玩价格（元）

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Integer getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Integer hourlyRate) { this.hourlyRate = hourlyRate; }
}
