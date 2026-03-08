# est-core-aop - 小白从入门到精通

## 目录
- [什么是 est-core-aop](#什么是-est-core-aop)
- [快速入门](#快速入门)
- [核心功能](#核心功能)
- [相关资源](#相关资源)

---

## 什么是 est-core-aop

### 用大白话理解
est-core-aop 就像"代码拦截器"，可以在方法执行前后插入额外的逻辑，比如记录日志、检查权限、处理事务，不用修改原方法代码。

### 核心特点
- **声明式 AOP**：注解驱动的 AOP 配置
- **切面编程**：支持前置、后置、环绕通知
- **高性能**：基于 ASM 的字节码生成
- **与 DI 集成**：与依赖注入容器无缝集成

---

## 快速入门

### 1. 添加依赖
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-aop</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 2. 创建切面
```java
@Aspect
public class LoggingAspect {
    
    @Before("execution(* ltd.idcu.est.demo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("方法执行前: " + joinPoint.getSignature());
    }
    
    @AfterReturning(pointcut = "execution(* ltd.idcu.est.demo.service.*.*(..))", returning = "result")
    public void logAfterReturning(Object result) {
        System.out.println("方法返回: " + result);
    }
}
```

---

## 核心功能

### 环绕通知
```java
@Aspect
public class PerformanceAspect {
    
    @Around("execution(* ltd.idcu.est.demo.service.*.*(..))")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long time = System.currentTimeMillis() - start;
            System.out.println(joinPoint.getSignature() + " 耗时: " + time + "ms");
        }
    }
}
```

### 自定义注解切面
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}

@Aspect
public class CustomLogAspect {
    
    @Around("@annotation(loggable)")
    public Object logMethod(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        System.out.println("自定义日志: " + joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
```

---

## 相关资源

- [父模块文档](../README.md)
- [依赖注入容器](../est-core-container/README.md)
