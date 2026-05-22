package com.chat.dto;

import jakarta.validation.constraints.NotBlank;

// 添加评论请求体
public class CommentRequest {

    @NotBlank(message = "评论内容不能为空")
    private String content;   // 评论内容，不能为空

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
