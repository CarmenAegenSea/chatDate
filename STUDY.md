# 数据库操作学习指南

基于本项目（Spring Boot 3.4.4 + JPA/Hibernate + MySQL）的实际代码编写。

---

## 目录

1. [整体架构](#1-整体架构)
2. [配置数据库连接](#2-配置数据库连接)
3. [Entity（实体/模型层）](#3-entity实体模型层)
4. [Repository（数据访问层）](#4-repository数据访问层)
5. [Service（业务逻辑层）](#5-service业务逻辑层)
6. [Controller（API 层）](#6-controllerapi-层)
7. [DTO（数据传输对象）](#7-dto数据传输对象)
8. [完整流程示例](#8-完整流程示例)
9. [常见关联关系](#9-常见关联关系)
10. [常用 SQL 操作](#10-常用-sql-操作)
11. [注意事项](#11-注意事项)

---

## 1. 整体架构

```
Controller (API 接口)
    ↓ 调用
Service (业务逻辑)
    ↓ 调用
Repository (数据访问)
    ↓ 操作
Entity (数据库表映射)
```

**数据流向：**

```
创建帖子：
  POST /api/posts?userId=1
    → PostController.create()
      → PostService.create()
        → userRepository.findById()
        → postRepository.save()
          → 数据库 posts 表
```

---

## 2. 配置数据库连接

文件：`chat/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chat_app?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 你的密码
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update    # 自动建表/更新表结构
    show-sql: true        # 控制台打印 SQL
    properties:
      hibernate:
        format_sql: true  # SQL 格式化
```

### `ddl-auto` 可选值

| 值 | 作用 |
|----|------|
| `none` | 不做任何操作 |
| `update` | 自动根据 Entity 创建/更新表（保留数据） |
| `create` | 每次启动删表重建（数据会丢） |
| `create-drop` | 启动创建，关闭删除（测试用） |
| `validate` | 验证 Entity 和表结构是否匹配 |

> **本项目使用 `update`**，开发阶段很方便。生产环境建议用 `validate` 或手动管理 DDL。

---

## 3. Entity（实体/模型层）

Entity 是 Java 对象和数据库表之间的映射。

### 3.1 基础 Entity

以 `User.java` 为例：

```java
@Entity                              // 标记为 JPA 实体
@Table(name = "users")               // 映射到 users 表
public class User {

    @Id                              // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增
    private Long id;

    @Column(unique = true, length = 20)  // 唯一约束，长度 20
    private String phone;

    @Column(nullable = false, length = 32)  // 非空，长度 32
    private String nickname;

    @Column(columnDefinition = "TEXT")  // 数据库类型为 TEXT
    private String content;
}
```

### 3.2 常用注解

| 注解 | 作用 |
|------|------|
| `@Entity` | 声明该类是实体 |
| `@Table(name = "表名")` | 指定映射的表名 |
| `@Id` | 标记主键 |
| `@GeneratedValue(strategy = IDENTITY)` | 主键自增 |
| `@Column(name, length, nullable, unique, columnDefinition)` | 字段配置 |
| `@Transient` | 不映射到数据库字段 |
| `@PrePersist` | 插入前自动执行 |
| `@PreUpdate` | 更新前自动执行 |

### 3.3 自动时间戳

本项目统一使用 `@PrePersist` 和 `@PreUpdate`：

```java
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

private LocalDateTime updatedAt;

@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

> 注意：`createdAt` 加了 `updatable = false`，创建后不允许修改。

---

## 4. Repository（数据访问层）

Repository 是操作数据库的接口，继承 `JpaRepository` 后自动获得 CRUD 方法。

### 4.1 基础 Repository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // 继承自带方法：findById, findAll, save, delete 等
}
```

`JpaRepository<实体类, 主键类型>` 自带的方法：

```java
findById(id)          // 按 ID 查询，返回 Optional
findAll()             // 查询全部
save(entity)          // 插入或更新
delete(entity)        // 删除
count()               // 计数
existsById(id)        // 判断是否存在
```

### 4.2 方法命名查询

按方法名自动生成 SQL，**不用写实现**：

```java
// 按 phone 字段精确查询
Optional<User> findByPhone(String phone);

// 判断 phone 是否存在
boolean existsByPhone(String phone);

// 按两个字段联合查询
Optional<User> findByOauthProviderAndOauthOpenId(String provider, String openId);

// 按创建时间倒序
List<Post> findAllByOrderByCreatedAtDesc();

// 按 postId 查询，按创建时间升序
List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
```

**方法命名规则：**

| 关键字 | 示例 | SQL |
|--------|------|-----|
| `findBy` | `findByName(String name)` | `WHERE name = ?` |
| `findAllBy` | `findAllByOrderByCreatedAtDesc()` | 全部，排序 |
| `existsBy` | `existsByPhone(String phone)` | `SELECT COUNT(*) WHERE phone = ?` |
| `countBy` | `countByStatus(String status)` | `SELECT COUNT(*) WHERE status = ?` |
| `deleteBy` | `deleteByUserId(Long userId)` | `DELETE WHERE user_id = ?` |
| `And` | `findByNameAndAge(String name, Integer age)` | `WHERE name = ? AND age = ?` |
| `Or` | `findByNameOrEmail(String name, String email)` | `WHERE name = ? OR email = ?` |
| `OrderBy` | `findByAgeOrderByCreatedAtDesc(Integer age)` | `WHERE age = ? ORDER BY created_at DESC` |
| `Top` / `First` | `findTop10ByOrderByLikeCountDesc()` | `LIMIT 10` |

### 4.3 `@Query` 自定义 SQL

当方法名不够用时，可以写自定义 JPQL：

```java
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    List<Post> searchByTitle(@Param("keyword") String keyword);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
```

---

## 5. Service（业务逻辑层）

Service 层写业务逻辑，调用 Repository。

### 5.1 基本写法

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    // 构造器注入（Spring 自动注入）
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 注册
    public UserResponse register(PasswordLoginRequest request) {
        // 1. 检查是否已存在
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("该手机号已注册");
        }
        // 2. 创建实体
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname("用户" + request.getPhone().substring(7));
        // 3. 保存并返回
        return UserResponse.from(userRepository.save(user));
    }
}
```

### 5.2 `@Transactional` 事务管理

```java
// 查询只读事务（提高性能）
@Transactional(readOnly = true)
public List<PostResponse> list() { ... }

// 写操作事务（默认，出错自动回滚）
@Transactional
public PostResponse like(Long postId, Long userId) {
    // 多个数据库操作在一个事务中
    // 任何一个失败都会回滚全部
}
```

**什么时候加 `@Transactional`？**

- 读取多个数据时加 `readOnly = true`（例如获取帖子+检查点赞状态）
- 写操作涉及多张表时加（例如点赞同时更新 `likeCount`）
- 增删改默认单条 SQL 不需要事务，但推荐统加上

### 5.3 查询 → 判断 → 修改模式

社区应用最常见的模式：

```java
@Transactional
public PostResponse like(Long postId, Long userId) {
    // 1. 查找帖子
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("帖子不存在"));
    // 2. 查找用户
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

    // 3. 判断是否已赞
    boolean alreadyLiked = post.getLikedByUsers().stream()
            .anyMatch(u -> u.getId().equals(userId));

    // 4. 执行业务
    if (alreadyLiked) {
        post.getLikedByUsers().remove(user);
        post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
    } else {
        post.getLikedByUsers().add(user);
        post.setLikeCount(post.getLikeCount() == null ? 1 : post.getLikeCount() + 1);
    }

    // 5. 保存
    return PostResponse.from(postRepository.save(post), !alreadyLiked);
}
```

---

## 6. Controller（API 层）

Controller 接收 HTTP 请求，调用 Service，返回响应。

```java
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // POST 创建
    @PostMapping
    public ResponseEntity<PostResponse> create(
            @RequestParam Long userId,
            @Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(userId, request));
    }

    // GET 列表
    @GetMapping
    public ResponseEntity<List<PostResponse>> list(
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.list(userId));
    }

    // GET 详情
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(postService.getById(id, userId));
    }

    // DELETE 删除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }
}
```

**常用注解：**

| 注解 | 用途 |
|------|------|
| `@GetMapping` | 查询 GET 请求 |
| `@PostMapping` | 创建 POST 请求 |
| `@PutMapping` | 更新 PUT 请求 |
| `@DeleteMapping` | 删除 DELETE 请求 |
| `@PathVariable` | 路径参数 `/{id}` |
| `@RequestParam` | 查询参数 `?userId=1` |
| `@RequestBody` | 请求体 JSON |
| `@Valid` | 参数校验 |

---

## 7. DTO（数据传输对象）

Entity 不直接返回给前端，而是通过 DTO 转换。

### 请求 DTO

```java
public class PostRequest {
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题不能超过200字")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    // getters/setters
}
```

### 响应 DTO

```java
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private boolean liked;

    // 静态工厂方法：从 Entity 创建 DTO
    public static PostResponse from(Post post) {
        return from(post, false);
    }

    public static PostResponse from(Post post, boolean liked) {
        PostResponse r = new PostResponse();
        r.id = post.getId();
        r.title = post.getTitle();
        r.content = post.getContent();
        r.author = UserResponse.from(post.getAuthor());
        r.createdAt = post.getCreatedAt();
        r.likeCount = post.getLikeCount();
        r.liked = liked;
        return r;
    }
}
```

> **原则：** Entity 的关联对象（如 `author`）也要转成 DTO，避免直接把 Entity 暴露给前端。

---

## 8. 完整流程示例

以「创建帖子」为例，看数据完整流转：

### 第1步：Controller 接收请求

```java
// POST /api/posts?userId=1
// Body: { "title": "标题", "content": "内容" }
@PostMapping
public ResponseEntity<PostResponse> create(
        @RequestParam Long userId,
        @Valid @RequestBody PostRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(postService.create(userId, request));
}
```

### 第2步：Service 处理业务

```java
public PostResponse create(Long userId, PostRequest request) {
    // 查用户（数据库读取）
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

    // 创建帖子实体
    Post post = new Post();
    post.setTitle(request.getTitle());     // 从 DTO 取值
    post.setContent(request.getContent());
    post.setAuthor(user);                  // 关联用户

    // 存数据库
    post = postRepository.save(post);

    // 转成 DTO 返回
    return PostResponse.from(post);
}
```

### 第3步：JPA 自动生成 SQL

```
Hibernate: insert into posts (title, content, user_id, created_at, updated_at, `like`)
values (?, ?, ?, ?, ?, ?)
```

### 第4步：返回给前端

```json
{
  "id": 1,
  "title": "标题",
  "content": "内容",
  "author": { "id": 1, "phone": "13800138000", "nickname": "张三", "avatar": null },
  "createdAt": "2026-05-22T10:00:00",
  "likeCount": 0,
  "liked": false
}
```

---

## 9. 常见关联关系

### 9.1 `@ManyToOne` 多对一（最常用）

多个评论 → 一个帖子：

```java
// Comment.java
@ManyToOne(fetch = FetchType.LAZY)          // LAZY 延迟加载
@JoinColumn(name = "post_id", nullable = false)  // 外键字段
private Post post;
```

### 9.2 `@OneToMany` 一对多

一个帖子 → 多个评论：

```java
// Post.java
@OneToMany(mappedBy = "post",                     // 对方关联字段名
           cascade = CascadeType.ALL,              // 级联操作
           orphanRemoval = true)                   // 删除孤子
@OrderBy("createdAt ASC")                          // 排序
private List<Comment> comments = new ArrayList<>();
```

### 9.3 `@ManyToMany` 多对多（用关联表）

帖子 ↔ 用户（点赞关系）：

```java
// Post.java
@ManyToMany
@JoinTable(
    name = "post_likes",                           // 关联表名
    joinColumns = @JoinColumn(name = "post_id"),   // 当前表外键
    inverseJoinColumns = @JoinColumn(name = "user_id")  // 对方表外键
)
private Set<User> likedByUsers = new HashSet<>();
```

自动生成关联表 `post_likes(post_id, user_id)`。

### 9.4 关联关系汇总

| 关系 | 注解 | 本项目例子 |
|------|------|-----------|
| 多对一 | `@ManyToOne` | 评论→帖子、评论→用户、帖子→用户 |
| 一对多 | `@OneToMany` | 帖子→评论列表 |
| 多对多 | `@ManyToMany` | 帖子↔点赞用户、评论↔点赞用户 |

---

## 10. 常用 SQL 操作

### 创建数据库

```sql
CREATE DATABASE chat_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 查看表结构

```sql
DESC users;
```

### 手动查数据

```sql
SELECT * FROM users;
SELECT * FROM posts ORDER BY created_at DESC;
SELECT * FROM post_likes;
```

### 修改字符集

```sql
ALTER DATABASE chat_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 11. 注意事项

### 11.1 `@Column(name = "\`like\`")` 反引号

`like` 是 MySQL 关键字，需要反引号转义：

```java
@Column(name = "`like`")
private Integer likeCount = 0;
```

### 11.2 Entity 直接返回给前端的坑

```java
// ❌ 错误：把 Entity 直接返回
@GetMapping("/{id}")
public Post getPost(@PathVariable Long id) {
    return postRepository.findById(id).orElseThrow();
}
// 问题：
// 1. 暴露了不该暴露的字段（password）
// 2. 关联对象懒加载可能报错
// 3. 无限递归（Post→Comment→Post→Comment...）

// ✅ 正确：转成 DTO
@GetMapping("/{id}")
public PostResponse getPost(@PathVariable Long id) {
    return PostResponse.from(postRepository.findById(id).orElseThrow());
}
```

### 11.3 `Optional` 的用法

```java
// 查不到时抛异常
User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

// 查不到时返回 null
User user = userRepository.findByPhone(phone).orElse(null);

// 查不到时用默认值
User user = userRepository.findByPhone(phone).orElse(defaultUser);
```

### 11.4 `@Transactional` 需要 import 正确的包

```java
import org.springframework.transaction.annotation.Transactional;
// 不要用 javax.transaction.Transactional
```

### 11.5 懒加载 `FetchType.LAZY`

```java
@ManyToOne(fetch = FetchType.LAZY)  // 用到时才查数据库
```

- 关联对象默认不加载，调用 getter 时才查数据库
- 优点：减少不必要的查询
- 注意：在事务外访问懒加载属性会报错 `LazyInitializationException`

### 11.6 `CascadeType` 的作用

```java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
```

| CascadeType | 效果 |
|-------------|------|
| `ALL` | 所有操作级联（增删改） |
| `PERSIST` | 保存主表时也保存从表 |
| `REMOVE` | 删除主表时也删除从表 |
| `MERGE` | 更新主表时也更新从表 |

### 11.7 `orphanRemoval = true`

从集合中移除的子实体自动从数据库删除。

```java
// 自动执行 DELETE FROM comments WHERE id = xx
post.getComments().remove(comment);
```

### 11.8 构建数据库操作的步骤总结

1. **建 Entity** — 用 `@Entity`, `@Table`, `@Column`, `@ManyToOne` 等注解映射表
2. **建 Repository** — 继承 `JpaRepository<Entity, Long>`，定义查询方法
3. **建 Service** — 写业务逻辑，注入 Repository，加 `@Transactional`
4. **建 DTO** — 写 Request/Response，Entity 和 Controller 之间传数据
5. **建 Controller** — 写 API 接口，调用 Service，加 `@GetMapping` 等
6. **配置 application.yml** — 配数据库连接
7. **（可选）配 pom.xml** — 加 `spring-boot-starter-data-jpa` 和 MySQL 驱动

> 本项目从第 1-6 步都有现成代码，照着写就对了。
