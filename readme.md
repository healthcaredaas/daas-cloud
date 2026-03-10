# DaaS Cloud - 微服务应用

## 简介

DaaS Cloud 是基于 Spring Cloud 2025 的微服务应用，包含认证服务、系统管理服务和 API 网关。

## 模块结构

```
daas-cloud/
├── cloud-domain-foundation/        # 基础领域模型
│   ├── mgmt/                       # 系统管理
│   │   ├── model/                  # 实体类
│   │   ├── dao/                    # 数据访问
│   │   ├── service/                # 业务服务
│   │   └── controller/             # REST 接口
│   ├── rbac/                       # 权限管理
│   │   ├── model/                  # 用户、角色、资源
│   │   ├── dao/                    # 数据访问
│   │   ├── service/                # 权限服务
│   │   └── helper/                 # OAuth2 用户转换
│   └── audit/                      # 审计日志
│
├── cloud-svc-auth/                 # 认证服务 (端口: 8080)
│   ├── authentication/             # 认证处理器
│   │   └── password/               # 密码模式
│   ├── cache/                      # 登录失败限制缓存
│   ├── configuration/              # OAuth2 配置
│   ├── controller/                 # 客户端管理
│   ├── listener/                   # 认证事件监听
│   ├── log/                        # 登录日志
│   └── service/                    # 用户详情服务
│
├── cloud-svc-foundation/           # 系统管理服务 (端口: 8081)
│   └── controller/                 # 业务接口
│       ├── DictController          # 字典管理
│       ├── DictItemController      # 字典项管理
│       ├── ApplicationController   # 应用管理
│       ├── ConfigPropertyController # 配置管理
│       ├── RbacUserController      # 用户管理
│       ├── RbacRoleController      # 角色管理
│       ├── RbacResourceController  # 资源管理
│       ├── LoginLogController      # 登录日志
│       └── OperationLogController  # 操作日志
│
└── cloud-svc-gateway/              # API 网关 (端口: 9000)
    └── filter/                     # 网关过滤器
```

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.5.11 |
| Spring Cloud | 2025.0.0 |
| Spring Cloud Alibaba | 2025.0.0.0 |
| Spring Security OAuth2 Authorization Server | 1.5.6 |
| SpringDoc OpenAPI | 2.7.0 |
| Nacos | 2.5.1 |

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| cloud-svc-gateway | 9000 | API 网关 |
| cloud-svc-auth | 8080 | 认证服务 |
| cloud-svc-foundation | 8081 | 系统管理服务 |

## 核心 API

### 认证服务 (cloud-svc-auth)

#### OAuth2 端点

| 端点 | 方法 | 说明 |
|------|------|------|
| /oauth2/token | POST | 获取 Token |
| /oauth2/revoke | POST | 撤销 Token |
| /oauth2/introspect | POST | Token 内省 |

#### Token 请求示例

**密码模式**
```bash
curl -X POST "http://localhost:8080/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "username=admin" \
  -d "password=123456" \
  -d "client_id=client" \
  -d "client_secret=secret"
```

**客户端凭证模式**
```bash
curl -X POST "http://localhost:8080/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=client" \
  -d "client_secret=secret"
```

**刷新令牌**
```bash
curl -X POST "http://localhost:8080/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token" \
  -d "refresh_token=<refresh_token>" \
  -d "client_id=client" \
  -d "client_secret=secret"
```

### 系统管理服务 (cloud-svc-foundation)

#### 用户管理 API

| 端点 | 方法 | 说明 |
|------|------|------|
| /api/foundation/rbac/user | GET | 分页查询用户 |
| /api/foundation/rbac/user | POST | 新增用户 |
| /api/foundation/rbac/user/{id} | PUT | 更新用户 |
| /api/foundation/rbac/user/{id} | DELETE | 删除用户 |
| /api/foundation/rbac/user/info | GET | 获取用户信息 |
| /api/foundation/rbac/user/roles/{userId} | GET | 获取用户角色 |

#### 角色管理 API

| 端点 | 方法 | 说明 |
|------|------|------|
| /api/foundation/rbac/role | GET | 分页查询角色 |
| /api/foundation/rbac/role | POST | 新增角色 |
| /api/foundation/rbac/role/{id} | PUT | 更新角色 |
| /api/foundation/rbac/role/{id} | DELETE | 删除角色 |
| /api/foundation/rbac/role/{roleId}/resource | GET | 获取角色资源 |

#### 资源管理 API

| 端点 | 方法 | 说明 |
|------|------|------|
| /api/foundation/rbac/resource | GET | 分页查询资源 |
| /api/foundation/rbac/resource | POST | 新增资源 |
| /api/foundation/rbac/resource/{id} | PUT | 更新资源 |
| /api/foundation/rbac/resource/{id} | DELETE | 删除资源 |
| /api/foundation/rbac/resource/tree | GET | 获取资源树 |

#### 字典管理 API

| 端点 | 方法 | 说明 |
|------|------|------|
| /api/foundation/mgmt/dict | GET | 分页查询字典 |
| /api/foundation/mgmt/dictItem/content | GET | 获取字典内容 |

## 快速开始

### 前置条件

1. **启动 Nacos**
   ```bash
   cd nacos/bin
   sh startup.sh -m standalone
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE daas DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **导入 SQL 脚本**
   - 执行 `doc/sql/daas.sql`

4. **配置 Nacos**
   - 创建命名空间 `daas`
   - 导入配置文件

### 编译项目

```bash
mvn clean install -DskipTests
```

### 启动服务

按以下顺序启动：

```bash
# 1. 启动网关
java -jar cloud-svc-gateway/target/cloud-svc-gateway.jar

# 2. 启动认证服务
java -jar cloud-svc-auth/target/cloud-svc-auth.jar

# 3. 启动系统管理服务
java -jar cloud-svc-foundation/target/cloud-svc-foundation.jar
```

### 验证服务

```bash
# 检查服务注册
curl http://localhost:8848/nacos/v1/ns/instance/list?serviceName=cloud-svc-auth

# 通过网关访问
curl http://localhost:9000/api/foundation/rbac/user -H "Authorization: Bearer <token>"
```

## API 文档

项目使用 SpringDoc OpenAPI 生成 API 文档。

### 访问地址

| 服务 | Swagger UI | OpenAPI JSON |
|------|------------|--------------|
| 认证服务 | http://localhost:8080/swagger-ui.html | http://localhost:8080/v3/api-docs |
| 系统管理服务 | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| API 网关 | http://localhost:9000/swagger-ui.html | http://localhost:9000/v3/api-docs |

## 配置说明

### Nacos 配置

**application.yml (公共配置)**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/daas
    username: root
    password: root
  data:
    redis:
      host: localhost
      port: 6379
```

**cloud-svc-auth.yml**
```yaml
server:
  port: 8080

spring:
  security:
    oauth2:
      authorizationserver:
        issuer: http://localhost:8080
```

**cloud-svc-foundation.yml**
```yaml
server:
  port: 8081
```

**cloud-svc-gateway.yml**
```yaml
server:
  port: 9000

spring:
  cloud:
    gateway:
      routes:
        - id: auth
          uri: lb://cloud-svc-auth
          predicates:
            - Path=/api/auth/**
        - id: foundation
          uri: lb://cloud-svc-foundation
          predicates:
            - Path=/api/foundation/**
```

## 安全配置

### 默认客户端

| 客户端ID | 密钥 | 权限 |
|----------|------|------|
| client | secret | password,client_credentials,refresh_token |

### 默认用户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 超级管理员 |

## 版本历史

### v0.0.1-SNAPSHOT (2026-03-03)

#### 版本升级
- Spring Boot 升级到 3.5.11
- Spring Cloud 升级到 2025.0.0
- Spring Cloud Alibaba 升级到 2025.0.0.0
- Spring Security OAuth2 Authorization Server 升级到 1.5.6

#### 功能变更
- 移除多租户功能
- 简化模块结构
- API 文档从 Knife4j 迁移到 SpringDoc OpenAPI 2.7.0

## 许可证

Apache 2.0