# EST 2.0 迁移指南

## 概述
本指南提供从EST 1.x迁移到EST 2.0的详细步骤。

## 核心层架构变更

### 旧架构（1.x）

```
est-core/
├── est-core-api/
└── est-core-impl/
```

### 新架构（2.0）

```
est-core/
├── est-core-container/
│   ├── est-core-container-api/
│   └── est-core-container-impl/
├── est-core-config/
│   ├── est-core-config-api/
│   └── est-core-config-impl/
├── est-core-lifecycle/
│   ├── est-core-lifecycle-api/
│   └── est-core-lifecycle-impl/
├── est-core-module/
│   ├── est-core-module-api/
│   └── est-core-module-impl/
├── est-core-aop/
│   ├── est-core-aop-api/
│   └── est-core-aop-impl/
├── est-core-tx/
│   ├── est-core-tx-api/
│   └── est-core-tx-impl/
├── est-core-api/ (兼容层)
└── est-core-impl/ (兼容层)
```

## 包名变更

### 旧包名（1.x）

```
ltd.idcu.est.core.api.*
ltd.idcu.est.core.impl.*
```

### 新包名（2.0）

```
ltd.idcu.est.core.container.api.*
ltd.idcu.est.core.container.impl.*
ltd.idcu.est.core.config.api.*
ltd.idcu.est.core.config.impl.*
ltd.idcu.est.core.lifecycle.api.*
ltd.idcu.est.core.lifecycle.impl.*
ltd.idcu.est.core.module.api.*
ltd.idcu.est.core.module.impl.*
ltd.idcu.est.core.aop.api.*
ltd.idcu.est.core.aop.impl.*
ltd.idcu.est.core.tx.api.*
ltd.idcu.est.core.tx.impl.*
```

## 依赖变更

### Maven依赖

**旧依赖（1.x）**

```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-impl</artifactId>
</dependency>
```

**新依赖（2.0）**

```xml
<!-- Container -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-container-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-container-impl</artifactId>
</dependency>

<!-- Config -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-config-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-config-impl</artifactId>
</dependency>

<!-- Lifecycle -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-lifecycle-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-lifecycle-impl</artifactId>
</dependency>

<!-- Module -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-module-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-module-impl</artifactId>
</dependency>

<!-- AOP -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-aop-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-aop-impl</artifactId>
</dependency>

<!-- Transaction -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-tx-api</artifactId>
</dependency>
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-tx-impl</artifactId>
</dependency>
```

## 向后兼容性

est-core-api 和 est-core-impl 仍然提供向后兼容层：
- 所有 1.x 代码可以继续使用
- 旧包名下的接口和类都标记为 @Deprecated
- 建议逐步迁移到新模块

## 迁移步骤

1. 更新Maven依赖
2. 更新导入语句
3. 逐步重构代码
4. 运行测试
5. 提交代码审查

## 测试

确保所有测试通过后，再进行部署。
