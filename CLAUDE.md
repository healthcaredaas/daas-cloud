# daas-cloud - 微服务框架

## 项目概述

daas-cloud 是 DataSphere 项目的微服务框架，提供服务治理、配置中心、服务网关等微服务基础设施。

## 模块结构

```
daas-cloud/
├── daas-common/            # 公共模块
│   ├── daas-common-core/   # 核心公共组件
│   └── daas-common-redis/  # Redis 配置
│
├── daas-gateway/           # API 网关
│
├── daas-auth/              # 认证服务
│
└── daas-admin/             # 管理服务
```

## 核心功能

### API 网关 (daas-gateway)

- 路由转发
- 负载均衡
- 限流熔断
- 认证鉴权
- 日志记录

### 认证服务 (daas-auth)

- 用户登录
- Token 生成
- Token 验证
- 权限校验

### 服务治理

- 服务注册发现
- 配置中心
- 服务熔断
- 负载均衡

## 网关配置

### 路由配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: quality-service
          uri: lb://quality-service
          predicates:
            - Path=/api/v1/quality/**
          filters:
            - StripPrefix=0

        - id: master-service
          uri: lb://master-service
          predicates:
            - Path=/api/v1/master/**
```

### 限流配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: quality-service
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100
                redis-rate-limiter.burstCapacity: 200
```

### 认证过滤器

```java
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (StringUtils.isBlank(token)) {
            return unauthorized(exchange);
        }

        // 验证 Token
        if (!validateToken(token)) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
```

## 服务注册配置

### application.yml

```yaml
spring:
  application:
    name: quality-service
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_HOST:localhost}:8848
        namespace: ${NACOS_NAMESPACE:public}
```

### 启动类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class QualityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QualityServiceApplication.class, args);
    }
}
```

## 配置中心

### Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      config:
        server-addr: ${NACOS_HOST:localhost}:8848
        namespace: ${NACOS_NAMESPACE:public}
        group: DEFAULT_GROUP
        file-extension: yaml
```

### 配置读取

```java
@RefreshScope
@RestController
public class ConfigController {

    @Value("${app.config.value}")
    private String configValue;

    @GetMapping("/config")
    public String getConfig() {
        return configValue;
    }
}
```

## 熔断降级

### Sentinel 配置

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080
      eager: true
```

### 熔断规则

```java
@RestController
public class QualityController {

    @GetMapping("/rules")
    @SentinelResource(value = "getRules", blockHandler = "handleBlock")
    public List<QualityRule> getRules() {
        return qualityService.list();
    }

    public List<QualityRule> handleBlock(BlockException ex) {
        return Collections.emptyList();
    }
}
```

## 服务调用

### Feign 客户端

```java
@FeignClient(name = "master-service", fallback = MasterFallback.class)
public interface MasterClient {

    @GetMapping("/api/v1/master/orgs/{id}")
    Organization getOrgById(@PathVariable String id);
}

@Component
public class MasterFallback implements MasterClient {

    @Override
    public Organization getOrgById(String id) {
        return null;
    }
}
```

## 链路追踪

### Sleuth 配置

```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0
  zipkin:
    base-url: http://localhost:9411
```

## 构建命令

```bash
# 编译
mvn clean compile

# 打包
mvn clean package -DskipTests

# 运行网关
java -jar daas-gateway/target/daas-gateway.jar
```

## 开发指南

### 新服务注册到网关

1. 添加 Nacos 依赖
2. 配置 Nacos 地址
3. 启动类添加 @EnableDiscoveryClient
4. 网关配置路由规则

### 服务间调用

1. 添加 Feign 依赖
2. 创建 Feign 客户端接口
3. 注入 Feign 客户端使用

## 注意事项

1. 网关是流量入口，配置变更需谨慎
2. 服务间调用注意超时设置
3. 熔断降级策略要根据业务场景配置