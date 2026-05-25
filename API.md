# API 文档

---

## 目录

- [认证](#认证)
  - [手机号+密码登录](#手机号密码登录)
  - [修改昵称](#修改昵称)
- [帖子](#帖子)
  - [创建帖子](#创建帖子)
  - [获取帖子列表](#获取帖子列表)
  - [获取帖子详情](#获取帖子详情)
  - [点赞帖子](#点赞帖子)
  - [获取评论列表](#获取评论列表)
  - [添加评论](#添加评论)

---

## 认证

### 手机号+密码登录

> 已注册用户通过手机号和密码登录；手机号未注册时返回错误。

**请求**

```
POST /api/auth/login
Content-Type: application/json
```

**请求体**

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|------|----------|------|
| `phone` | string | 是 | 匹配 `^1[3-9]\d{9}$` | 手机号 |
| `password` | string | 是 | 长度 6-32 位 | 密码 |

**示例**

```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

**成功响应 200**

```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "用户8000",
  "avatar": null
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 用户 ID |
| `phone` | string | 手机号 |
| `nickname` | string | 昵称 |
| `avatar` | string | 头像 URL（可为 null） |

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 手机号未注册 / 密码错误 / 参数校验失败 |
| 409 | 数据冲突 |

---

### 修改昵称

**请求**

```
POST /api/auth/nickname?userId=1
Content-Type: application/json
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 用户 ID |

**请求体**

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|------|----------|------|
| `nickname` | string | 是 | 长度 1-32 字 | 新昵称 |

**示例**

```json
{
  "nickname": "张三"
}
```

**成功响应 200**

```json
{
  "id": 1,
  "phone": "13800138000",
  "nickname": "张三",
  "avatar": null
}
```

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 用户不存在 / 昵称为空或超长 |

---

## 帖子

### 创建帖子

**请求**

```
POST /api/posts?userId=1
Content-Type: application/json
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 作者用户 ID |

**请求体**

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|------|----------|------|
| `title` | string | 是 | 长度 1-200 字 | 标题 |
| `content` | string | 是 | 不能为空 | 内容 |

**示例**

```json
{
  "title": "帖子标题",
  "content": "帖子内容"
}
```

**成功响应 201**

```json
{
  "id": 1,
  "title": "帖子标题",
  "content": "帖子内容",
  "author": {
    "id": 1,
    "phone": "13800138000",
    "nickname": "张三",
    "avatar": null
  },
  "createdAt": "2026-05-19T12:00:00"
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |
| `title` | string | 标题 |
| `content` | string | 内容 |
| `author` | object | 作者信息（同 UserResponse） |
| `createdAt` | datetime | 创建时间 |

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 参数校验失败 |
| 404 | 用户不存在 |

---

### 获取帖子列表

> 返回所有帖子，按创建时间倒序排列。

**请求**

```
GET /api/posts
GET /api/posts?userId=1   （可选，传了会返回当前用户是否已赞）
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 否 | 当前登录用户 ID，传此参数时响应中 `liked` 字段有效 |

**成功响应 200**

```json
[
  {
    "id": 2,
    "title": "第二篇帖子",
    "content": "内容...",
    "author": { "id": 1, "phone": "13800138000", "nickname": "张三", "avatar": null },
    "createdAt": "2026-05-19T14:00:00",
    "likeCount": 3,
    "liked": true
  },
  {
    "id": 1,
    "title": "第一篇帖子",
    "content": "内容...",
    "author": { "id": 2, "phone": "13900139000", "nickname": "李四", "avatar": null },
    "createdAt": "2026-05-19T12:00:00",
    "likeCount": 0,
    "liked": false
  }
]
```

---

### 获取帖子详情

**请求**

```
GET /api/posts/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |

**成功响应 200**

```json
{
  "id": 1,
  "title": "帖子标题",
  "content": "帖子内容",
  "author": { "id": 1, "phone": "13800138000", "nickname": "张三", "avatar": null },
  "createdAt": "2026-05-19T12:00:00"
}
```

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 404 | 帖子不存在 |

### 点赞/取消点赞帖子

> 点击切换点赞状态：未赞时点赞，已赞时取消点赞。

**请求**

```
POST /api/posts/{id}/like?userId=1
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 当前登录用户 ID |

**成功响应 200**

```json
{
  "id": 1,
  "title": "帖子标题",
  "content": "帖子内容",
    "author": { "id": 1, "phone": "13800138000", "nickname": "张三", "avatar": null },
    "createdAt": "2026-05-19T12:00:00",
  "likeCount": 5,
  "liked": true
}
```

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 帖子不存在 |

---

### 获取评论列表

**请求**

```
GET /api/posts/{id}/comments
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |

**成功响应 200**

```json
[
  { "id": 1, "content": "好帖", "createdAt": "2026-05-19T12:30:00" },
  { "id": 2, "content": "顶一个", "createdAt": "2026-05-19T13:00:00" }
]
```

---

### 添加评论

**请求**

```
POST /api/posts/{id}/comments
Content-Type: application/json
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `content` | string | 是 | 评论内容，不能为空 |

**成功响应 201**

```json
{
  "id": 1,
  "content": "好帖",
  "createdAt": "2026-05-19T12:30:00"
}
```

---

### 删除帖子

> 仅帖子作者可删除，级联删除该帖所有评论及点赞关系。

**请求**

```
DELETE /api/posts/{id}?userId=1
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 当前登录用户 ID（必须为帖子作者） |

**成功响应 204**

无响应体。

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 无权删除 |
| 404 | 帖子不存在 |

---

### 删除评论

> 仅评论作者可删除。

**请求**

```
DELETE /api/posts/{id}/comments/{commentId}?userId=1
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | long | 帖子 ID |
| `commentId` | long | 评论 ID |

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 当前登录用户 ID（必须为评论作者） |

**成功响应 204**

无响应体。

**错误响应**

| HTTP 状态码 | 说明 |
|-------------|------|
| 400 | 无权删除 / 评论不属于该帖子 |
| 404 | 帖子或评论不存在 |

---

## 全局错误响应格式

所有错误响应统一格式：

```json
{
  "message": "错误描述信息"
}
```

**HTTP 状态码汇总**

| 状态码 | 含义 |
|--------|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 400 | 参数错误 / 业务异常 |
| 404 | 资源不存在 |
| 409 | 数据冲突（如手机号重复） |

---

## 游戏陪玩

### 获取游戏列表

**请求**

```
GET /api/game/games
```

**成功响应 200**

```json
[
  { "id": 1, "name": "王者荣耀", "icon": "🎮", "currentPlayers": 42 },
  { "id": 2, "name": "和平精英", "icon": "🔫", "currentPlayers": 28 }
]
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | long | 游戏唯一 ID |
| `name` | string | 游戏名称 |
| `icon` | string | 游戏图标 |
| `currentPlayers` | int | 当前在线人数 |

---

### 检查用户游戏资料

**请求**

```
GET /api/game/player/check?userId=1
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 用户 ID |

**成功响应 200**

```json
{
  "hasProfile": true
}
```

---

### 获取用户游戏资料

**请求**

```
GET /api/game/player?userId=1
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 用户 ID |

**成功响应 200**

```json
{
  "userId": 1,
  "nickname": "张三",
  "avatar": null,
  "isCompanion": false,
  "bio": "",
  "favoriteGames": [
    { "id": 1, "name": "王者荣耀", "icon": "🎮", "currentPlayers": 42 }
  ]
}
```

**错误响应**

| 状态码 | 说明 |
|--------|------|
| 404 | 游戏资料不存在 |

---

### 首次设置游戏资料

> 用户首次进入游戏模块时，选择喜欢的游戏，可选成为陪玩。

**请求**

```
POST /api/game/player/setup?userId=1
Content-Type: application/json
```

**查询参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | long | 是 | 用户 ID |

**请求体**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `favoriteGameIds` | long[] | 是 | 喜欢的游戏 ID 列表 |
| `isCompanion` | boolean | 否 | 是否成为陪玩，默认 false |
| `bio` | string | 否 | 陪玩简介 |

**示例**

```json
{
  "favoriteGameIds": [1, 3],
  "isCompanion": true,
  "bio": "王者荣耀荣耀王者段位"
}
```

**成功响应 201**

```json
{
  "userId": 1,
  "nickname": "张三",
  "avatar": null,
  "isCompanion": true,
  "bio": "王者荣耀荣耀王者段位",
  "favoriteGames": [
    { "id": 1, "name": "王者荣耀", "icon": "🎮", "currentPlayers": 42 },
    { "id": 3, "name": "原神", "icon": "✨", "currentPlayers": 35 }
  ]
}
```

**错误响应**

| 状态码 | 说明 |
|--------|------|
| 400 | 游戏资料已存在 / 用户不存在 |

---

### 获取联机大厅

> 展示所有游戏及其当前在线人数。

**请求**

```
GET /api/game/lobby
```

**成功响应 200**

```json
[
  { "id": 1, "name": "王者荣耀", "icon": "🎮", "currentPlayers": 42 },
  { "id": 2, "name": "和平精英", "icon": "🔫", "currentPlayers": 28 },
  { "id": 3, "name": "原神", "icon": "✨", "currentPlayers": 35 }
]
```

---

### 获取游戏详情

> 展示游戏详情，包括过去一小时的在线人数记录和可接单的陪玩人员。

**请求**

```
GET /api/game/{gameId}/detail
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `gameId` | long | 游戏 ID |

**成功响应 200**

```json
{
  "game": { "id": 1, "name": "王者荣耀", "icon": "🎮", "currentPlayers": 42 },
  "records": [
    { "recordedAt": "2026-05-25T10:00:00", "playerCount": 35 },
    { "recordedAt": "2026-05-25T10:05:00", "playerCount": 38 }
  ],
  "companions": [
    { "userId": 1, "nickname": "张三", "avatar": null, "bio": "荣耀王者段位" }
  ]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `game` | object | 游戏信息（同 GameResponse） |
| `records` | array | 过去一小时在线人数记录（每 5 分钟一个点） |
| `records[].recordedAt` | datetime | 记录时间 |
| `records[].playerCount` | int | 当时在线人数 |
| `companions` | array | 可接单的陪玩人员列表 |
| `companions[].userId` | long | 陪玩用户 ID |
| `companions[].nickname` | string | 陪玩昵称 |
| `companions[].avatar` | string | 陪玩头像 |
| `companions[].bio` | string | 陪玩简介 |

---

### 获取陪玩列表

**请求**

```
GET /api/game/{gameId}/companions
```

**路径参数**

| 参数 | 类型 | 说明 |
|------|------|------|
| `gameId` | long | 游戏 ID |

**成功响应 200**

```json
[
  { "userId": 1, "nickname": "张三", "avatar": null, "bio": "荣耀王者段位" }
]
```
