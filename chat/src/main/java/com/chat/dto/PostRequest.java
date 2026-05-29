package com.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 创建帖子请求体
public class PostRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题不能超过200字")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String mood; // 心情 / 标签（可选）

    private String type; // 帖子类型: rant(吐槽墙) / wise(万事通) / sell(售卖墙)，不传默认 rant

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
