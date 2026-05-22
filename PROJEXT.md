# 本项目所用语法 / 语言一览

---

## 1. Java

- **版本**: Java 17
- **框架**: Spring Boot 3.4.4
- **位置**: `chat/src/main/java/` 及 `chat/src/test/java/`
- **特性**: 泛型、Stream API、Lambda、Optional、var (未使用)、record (未使用)
- **构建工具**: Maven (`pom.xml`)

### 子语法

| 语法 | 说明 | 使用位置 |
|------|------|----------|
| **JPA / Hibernate** | `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@JoinColumn`, `@JoinTable`, `@PrePersist`, `@PreUpdate`, `@OrderBy`, `FetchType`, `CascadeType` | model 层 |
| **Spring Boot** | `@SpringBootApplication`, `@RestController`, `@RequestMapping`, `@PostMapping`, `@GetMapping`, `@RequestParam`, `@PathVariable`, `@RequestBody`, `@Service`, `@Component`, `@Configuration`, `@Bean`, `@Transactional`, `@RestControllerAdvice`, `@ExceptionHandler`, `ResponseEntity` | controller / service / config 层 |
| **Jakarta Validation** | `@Valid`, `@NotBlank`, `@Size`, `@Pattern` | dto 层 |
| **JPA Repository** | 继承 `JpaRepository`，方法命名查询（`findByPhone`, `existsByPhone`, `findByOauthProviderAndOauthOpenId`, `findAllByOrderByCreatedAtDesc`, `findByPostIdOrderByCreatedAtAsc`） | repository 层 |
| **Spring Security Crypto** | `BCryptPasswordEncoder` | service 层 |
| **JUnit 5** | `@SpringBootTest`, `@Test` | test 层 |

---

## 2. XML

- **位置**: `chat/pom.xml`
- **用途**: Maven 项目配置（依赖管理、构建插件）
- **语法要点**: `<project>`, `<parent>`, `<dependencies>`, `<dependency>`, `<build>`, `<plugins>`, `<plugin>`, `<configuration>`, `<excludes>`, `<scope>`, `<optional>`

---

## 3. YAML

- **位置**: `chat/src/main/resources/application.yml`
- **用途**: Spring Boot 应用配置（server, datasource, JPA）
- **语法要点**: 缩进层级、键值对

---

## 4. HTML

- **位置**: `chat/src/main/resources/static/index.html`
- **用途**: 前端测试页面
- **语法要点**: `<!DOCTYPE html>`, `<html>`, `<head>`, `<body>`, 各种语义标签，`class`/`id` 属性，`onclick` 事件

---

## 5. CSS

- **位置**: `index.html` 内嵌 `<style>` 块
- **用途**: 页面样式
- **语法要点**: 选择器（标签、类、伪类、后代）、Flexbox（`display: flex`, `gap`, `flex-wrap`）、固定/相对定位、过渡动画、圆角、阴影、`:hover`、`:focus`、`:disabled`、`:last-child`、媒体查询（未使用）

---

## 6. JavaScript (ES6+)

- **位置**: `index.html` 内嵌 `<script>` 块
- **用途**: 前端交互逻辑（API 调用、DOM 操作）
- **语法要点**:
  - 箭头函数 `() => {}`
  - `async/await`
  - `fetch` API
  - 模板字面量（`` `...${}...` ``）
  - `const` / `let`
  - 解构赋值（未使用）
  - `try/catch`
  - `classList` 操作、`querySelector`、`innerHTML`
  - `JSON.parse` / `JSON.stringify`
  - 可选链（未使用）
  - 三元运算符
  - 数组方法：`map`, `join`, `forEach`

---

## 7. Markdown

- **位置**: `API.md`, `chat/README.md`
- **用途**: 文档编写
- **语法要点**: 标题 (`#`), 列表 (`-`, `1.`), 代码块 (`` ``` ``), 表格 (`|`), 链接 (`[]()`), 引用 (`>`), 分割线 (`---`), 加粗 (`**`)

---

## 8. SQL

- **位置**: `chat/README.md` 内嵌代码块
- **用途**: 数据库 DDL 定义 & 查询示例
- **语法要点**: `CREATE TABLE`, `ALTER TABLE`, `BIGINT`, `VARCHAR`, `TEXT`, `AUTO_INCREMENT`, `PRIMARY KEY`, `FOREIGN KEY`, `REFERENCES`, `NOT NULL`, `UNIQUE`, `DEFAULT`, `JOIN`, `ORDER BY`, `SELECT`, `INSERT`

---

## 9. JSON

- **位置**: `.vscode/settings.json`
- **用途**: VS Code 编辑器配置
- **语法要点**: 键值对、对象、数组

---

## 10. Bash / Shell

- **位置**: `chat/README.md` 内嵌代码块
- **用途**: 启动命令、数据库查询命令
- **语法要点**: `cd`, `mvn spring-boot:run`, `mvn package`, `java -jar`, `mysql -u -p -e`

---

## 11. Maven 生命周期 / CLI

- `mvn spring-boot:run` — 直接运行
- `mvn package -DskipTests` — 打包
- `java -jar target/chat-0.0.1-SNAPSHOT.jar` — 运行 jar
