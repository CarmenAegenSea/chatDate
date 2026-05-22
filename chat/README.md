# Chat - 社区应用

## 环境配置

### 配置文件位置

`src/main/resources/application.yml`

### 当前配置

| 配置项 | 值 |
|--------|-----|
| 服务端口 | `8080` |
| 数据库 | `chat_app` (MySQL 8.4) |
| 数据库用户 | `root` |
| 数据库密码 | `root123` |
| 数据库地址 | `localhost:3306` |
| JPA DDL 策略 | `update` |

### 修改配置

编辑 `src/main/resources/application.yml`，修改对应字段即可。

## 目录结构

```
chat/
├── pom.xml                           # Maven 依赖管理
├── README.md
└── src/
    ├── main/
    │   ├── java/com/chat/
    │   │   ├── ChatApplication.java      # Spring Boot 入口
    │   │   ├── config/                    # 配置类（跨域、异常处理、数据初始化等）
    │   │   ├── controller/                # REST API 控制器，处理 HTTP 请求
    │   │   ├── model/                     # JPA 实体类，映射数据库表
    │   │   ├── repository/                # 数据访问层，操作数据库
    │   │   ├── service/                   # 业务逻辑层
    │   │   └── dto/                       # 请求/响应数据传输对象
    │   └── resources/
    │       └── application.yml            # 应用配置文件（数据库等）
    └── test/
        └── java/com/chat/
            └── ChatApplicationTests.java  # 测试入口
```

### 各文件夹说明

| 目录 | 作用 |
|------|------|
| `config/` | 全局配置、安全拦截器、跨域等配置类 |
| `controller/` | 接收 HTTP 请求，调用 service 层，返回响应 |
| `model/` | 数据库表映射实体，使用 JPA 注解 |
| `repository/` | Spring Data JPA 接口，提供 CRUD 方法 |
| `service/` | 核心业务逻辑层，供 controller 调用 |
| `dto/` | 封装前端请求参数和 API 返回结果 |

## API 接口

### 认证

```
POST /api/auth/login
```

手机号+密码登录，手机号不存在则自动注册并返回新用户信息。

**请求参数 (JSON Body):**

```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

**响应:**

```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "用户8000",
  "avatar": null,
  "balance": 0
}
```

### 帖子

```
POST /api/posts?userId=1
```

发布新帖子。

**请求参数 (JSON Body):**

```json
{
  "title": "帖子标题",
  "content": "帖子内容"
}
```

**响应:**

```json
{
  "id": 1,
  "title": "帖子标题",
  "content": "帖子内容",
  "author": { "id": 1, "phone": "13800138000", "nickname": "张三", "balance": 0 },
  "createdAt": "2026-05-18T12:00:00"
}
```

---

```
GET /api/posts
```

获取帖子列表（按发布时间倒序）。

**响应:**

```json
[
  { "id": 1, "title": "...", "author": {...}, "createdAt": "..." },
  { "id": 2, "title": "...", "author": {...}, "createdAt": "..." }
]
```

---

```
GET /api/posts/{id}
```

获取帖子详情。

## 启动

```bash
cd chat
mvn spring-boot:run
```

或打包后执行:

```bash
mvn package -DskipTests
java -jar target/chat-0.0.1-SNAPSHOT.jar
```

http://localhost:8080

---

```
PUT /api/auth/nickname?userId=1
```

修改昵称。

**请求参数 (JSON Body):**

```json
{
  "nickname": "新昵称"
}
```

**响应:**

```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "新昵称",
  "avatar": null,
  "balance": 0
}
```

## 数据库

### DDL（JPA 自动建表，也可手动执行）

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE,
    nickname VARCHAR(32) NOT NULL,
    avatar VARCHAR(500),
    password VARCHAR(128),
    balance BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### 常用查询

```bash
mysql -u root -proot123 -e "USE chat_app; SHOW TABLES;"
```

```bash
mysql -u root -proot123 chat_app -e "SELECT * FROM users"
```

```bash
mysql -u root -proot123 chat_app -e "SELECT * FROM posts ORDER BY created_at DESC"
```

```bash
mysql -u root -proot123 chat_app -e "
SELECT p.id, p.title, u.nickname AS author, p.created_at
FROM posts p JOIN users u ON p.user_id = u.id
ORDER BY p.created_at DESC"
```