package com.chat.controller;

import com.chat.dto.CommentRequest;
import com.chat.dto.CommentResponse;
import com.chat.dto.PostRequest;
import com.chat.dto.PostResponse;
import com.chat.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 帖子相关接口：创建、列表、详情、点赞、评论
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 创建帖子
    @PostMapping
    public ResponseEntity<PostResponse> create(
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(userId, request));
    }

    // 获取帖子列表（按发布时间倒序），可选传 userId 获取点赞状态
    @GetMapping
    public ResponseEntity<List<PostResponse>> list(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.list(userId));
    }

    // 获取帖子详情，可选传 userId 获取点赞状态
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id,
                                                 @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.getById(id, userId));
    }

    // 点赞帖子，每人每帖只能点一次，重复点赞返回 400
    @PostMapping("/{id}/like")
    public ResponseEntity<PostResponse> like(@PathVariable Long id,
                                              @RequestParam Long userId) {
        return ResponseEntity.ok(postService.like(id, userId));
    }

    // 获取评论列表，可选传 userId 获取点赞状态
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id,
                                                              @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.getComments(id, userId));
    }

    // 添加评论，需传 userId 记录作者
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @RequestParam Long userId,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.addComment(id, userId, request));
    }

    // 删除帖子（仅作者），级联删除评论及点赞关系
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    // 收藏/取消收藏帖子
    @PostMapping("/{id}/bookmark")
    public ResponseEntity<PostResponse> bookmark(@PathVariable Long id,
                                                  @RequestParam Long userId) {
        return ResponseEntity.ok(postService.bookmark(id, userId));
    }

    // 点赞/取消点赞评论
    @PostMapping("/{id}/comments/{commentId}/like")
    public ResponseEntity<CommentResponse> likeComment(@PathVariable Long id,
                                                        @PathVariable Long commentId,
                                                        @RequestParam Long userId) {
        return ResponseEntity.ok(postService.likeComment(commentId, userId));
    }

    // 删除评论（仅评论作者）
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id,
                                               @PathVariable Long commentId,
                                               @RequestParam Long userId) {
        postService.deleteComment(id, commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
