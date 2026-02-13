# API认证服务

## 项目简介

API认证服务是一个基于Spring Boot的微服务，提供用户认证、授权和权限管理功能。该服务采用JWT（JSON Web Token）作为认证机制，支持无状态认证，便于水平扩展。

## 技术栈

| 类别 | 技术/框架 | 版本 |
|------|-----------|------|
| 基础框架 | Spring Boot | 3.5.6 |
| 安全框架 | Spring Security | 内置 |
| ORM框架 | MyBatis Plus | 3.5.5 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 7.0+ |
| 认证 | JWT | 0.11.5 |
| 工具 | Lombok | 1.18.30 |
| 文档 | Swagger | 2.0.2 |
| 监控 | Spring Boot Actuator | 内置 |
| 语言 | Java | 17 |

## 功能特性

### 1. 用户认证
- **登录**：验证用户凭据并生成访问令牌
- **注销**：使令牌失效并记录操作
- **注册**：创建新用户，包含密码强度验证
- **令牌刷新**：使用刷新令牌获取新的访问令牌

### 2. 权限管理
- **基于角色的权限控制**：用户关联角色，角色关联权限
- **数据权限**：支持基于部门的数据权限控制
- **权限缓存**：使用Redis缓存权限信息，提高性能

### 3. 安全特性
- **密码加密**：使用BCrypt加密存储密码
- **JWT签名**：令牌使用密钥签名，防止篡改
- **令牌黑名单**：已注销的令牌加入黑名单，防止复用
- **限流机制**：基于Redis的限流，防止暴力破解
- **操作审计**：详细的操作日志记录，便于安全审计

### 4. 其他特性
- **跨域支持**：配置CORS，支持跨域请求
- **健康检查**：提供健康检查端点，便于监控
- **API文档**：集成Swagger，自动生成API文档
- **全局异常处理**：统一的异常处理机制，返回标准化的错误响应

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 7.0+

### 数据库准备
1. 创建数据库：`CREATE DATABASE api_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. 执行SQL脚本：`src/main/resources/sql/auth_schema.sql`

### 配置修改
修改 `src/main/resources/application.yml` 文件，配置数据库和Redis连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/api_auth?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8
    username: root
    password: root123456
  data:
    redis:
      host: localhost
      port: 6379
      password: 123456
      database: 0

# JWT配置
jwt:
  secret: your-jwt-secret-key-change-in-production
  expiration: 7200    # 2小时
  refresh-expiration: 2592000  # 30天
```

### 构建和运行

#### 构建项目
```bash
mvn clean package -DskipTests
```

#### 运行项目
```bash
java -jar target/api_auth-0.0.1-SNAPSHOT.jar
```

## API文档

项目集成了Swagger，可通过以下地址访问API文档：

- **Swagger UI**：http://localhost:8080/swagger-ui.html
- **API文档JSON**：http://localhost:8080/v3/api-docs

## 目录结构

```
├── src/
│   ├── main/
│   │   ├── java/com/centsuse/api_auth/
│   │   │   ├── configs/        # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── dao/             # 数据模型
│   │   │   ├── dtos/            # 数据传输对象
│   │   │   ├── entities/        # 实体类
│   │   │   ├── enums/           # 枚举类
│   │   │   ├── exception/       # 异常类
│   │   │   ├── filters/         # 过滤器
│   │   │   ├── mapper/          # MyBatis映射器
│   │   │   ├── service/         # 服务层
│   │   │   ├── utils/           # 工具类
│   │   │   └── ApiAuthApplication.java  # 应用入口
│   │   └── resources/
│   │       ├── com/centsuse/api_auth/mapper/  # MyBatis XML映射文件
│   │       ├── sql/             # SQL脚本
│   │       └── application.yml  # 应用配置
│   └── test/                    # 测试代码
├── .mvn/                        # Maven包装器
├── mvnw                         # Maven包装器脚本
├── mvnw.cmd                     # Maven包装器Windows脚本
├── pom.xml                      # Maven项目配置
└── README.md                    # 项目说明文档
```

## 核心API

### 认证接口

| API路径 | 方法 | 功能 | 认证要求 |
|---------|------|------|----------|
| `/auth/login` | POST | 用户登录 | 否 |
| `/auth/logout` | POST | 用户注销 | 是 |
| `/auth/register` | POST | 用户注册 | 否 |
| `/auth/refresh` | POST | 刷新令牌 | 否 |

### 健康检查

| API路径 | 方法 | 功能 | 认证要求 |
|---------|------|------|----------|
| `/actuator/health` | GET | 健康检查 | 否 |
| `/actuator/info` | GET | 应用信息 | 否 |

## 配置说明

### 核心配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `jwt.secret` | JWT签名密钥 | your-jwt-secret-key-change-in-production |
| `jwt.expiration` | 访问令牌过期时间（秒） | 7200 |
| `jwt.refresh-expiration` | 刷新令牌过期时间（秒） | 2592000 |
| `spring.datasource.url` | 数据库连接URL | jdbc:mysql://localhost:3306/api_auth |
| `spring.datasource.username` | 数据库用户名 | root |
| `spring.datasource.password` | 数据库密码 | root123456 |
| `spring.data.redis.host` | Redis主机 | localhost |
| `spring.data.redis.port` | Redis端口 | 6379 |
| `spring.data.redis.password` | Redis密码 | 123456 |

### 限流配置

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| 登录/注册接口限流 | 同一IP每分钟最多请求次数 | 10 |

## 开发指南

### 代码规范
- 遵循Java编码规范
- 使用Lombok减少样板代码
- 方法和类添加适当的注释
- 异常处理使用统一的异常处理机制

### 开发流程
1. 克隆代码库
2. 修改配置文件
3. 运行数据库脚本
4. 启动开发服务器
5. 编写代码
6. 运行测试
7. 提交代码

### 运行开发服务器
```bash
mvn spring-boot:run
```

### 运行测试
```bash
mvn test
```

### 代码质量检查
```bash
mvn checkstyle:check
```

## 常见问题

### 1. 登录失败
- **原因**：用户名或密码错误
- **解决方法**：检查用户名和密码是否正确

### 2. 令牌过期
- **原因**：访问令牌已过期
- **解决方法**：使用刷新令牌获取新的访问令牌，或重新登录

### 3. 权限不足
- **原因**：用户没有访问该资源的权限
- **解决方法**：联系管理员分配相应的权限

### 4. 数据库连接失败
- **原因**：数据库配置错误或数据库服务未启动
- **解决方法**：检查数据库配置和数据库服务状态

### 5. Redis连接失败
- **原因**：Redis配置错误或Redis服务未启动
- **解决方法**：检查Redis配置和Redis服务状态

## 许可证

MIT License

## 联系方式

- **项目维护者**：bobo
- **电子邮件**：bobo@example.com
- **GitHub**：https://github.com/example/api_auth

## 更新日志

### v1.0.0
- 初始版本发布
- 实现用户认证、授权和权限管理功能
- 集成Swagger API文档
- 添加健康检查端点
- 实现基于Redis的限流机制
- 完善日志记录和异常处理
