package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.PasswordEncoder;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.api.UserDetailsService;
import ltd.idcu.est.features.security.api.Role;
import ltd.idcu.est.features.security.api.Permission;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.basic.DefaultAuthentication;
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.apikey.ApiKeySecurity;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.List;
import java.util.ArrayList;

public class CompleteSecurityExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteSecurityExample.class);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST 安全认证模块 - 完整示例");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例将展示 EST 安全认证模块的各种功能：");
        System.out.println("  - 密码加密（防止明文存储）");
        System.out.println("  - 用户认证（登录验证）");
        System.out.println("  - 多种认证方式（Basic、JWT、API Key）");
        System.out.println("  - 角色和权限管理");
        System.out.println("  - 与 Web 模块联动保护 API");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("第一部分：理解安全认证的重要性");
        System.out.println("=".repeat(70));
        System.out.println("\n【为什么需要安全认证？】");
        System.out.println("  - 保护用户数据不被窃取");
        System.out.println("  - 防止未经授权的访问");
        System.out.println("  - 密码不能明文存储（黑客会偷！）");
        System.out.println("  - 不同用户有不同权限（管理员 vs 普通用户）\n");
        
        passwordEncoderExample();
        basicAuthenticationExample();
        jwtAuthenticationExample();
        apiKeyAuthenticationExample();
        roleAndPermissionExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有安全认证示例完成！");
        System.out.println("=".repeat(70));
    }
    
    private static void passwordEncoderExample() {
        System.out.println("\n--- 方式一：密码编码器（最重要！）---");
        System.out.println("\n【什么是密码编码器？】");
        System.out.println("  - 把密码变成一串乱码（哈希值）");
        System.out.println("  - 即使数据库被偷，黑客也看不到真实密码");
        System.out.println("  - 同样的密码每次生成的哈希值都不一样（加盐）\n");
        
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        System.out.println("步骤 1: 创建一个明文密码");
        String plainPassword = "mySecurePassword123";
        System.out.println("   明文密码: " + plainPassword);
        
        System.out.println("\n步骤 2: 加密密码（哈希）");
        String encodedPassword1 = passwordEncoder.encode(plainPassword);
        System.out.println("   第一次加密结果: " + encodedPassword1);
        
        System.out.println("\n步骤 3: 再次加密同一个密码");
        String encodedPassword2 = passwordEncoder.encode(plainPassword);
        System.out.println("   第二次加密结果: " + encodedPassword2);
        
        System.out.println("\n   💡 注意：两次结果不一样！这是为了更安全！");
        
        System.out.println("\n步骤 4: 验证密码是否正确");
        boolean matches1 = passwordEncoder.matches(plainPassword, encodedPassword1);
        boolean matches2 = passwordEncoder.matches(plainPassword, encodedPassword2);
        boolean matchesWrong = passwordEncoder.matches("wrongPassword", encodedPassword1);
        
        System.out.println("   验证正确密码 (第一次): " + matches1);
        System.out.println("   验证正确密码 (第二次): " + matches2);
        System.out.println("   验证错误密码: " + matchesWrong);
        
        System.out.println("\n步骤 5: 实际应用场景");
        System.out.println("   用户注册时：加密密码，存到数据库");
        System.out.println("   用户登录时：把输入的密码加密，和数据库的对比");
        
        logger.info("密码编码器示例完成");
    }
    
    private static void basicAuthenticationExample() {
        System.out.println("\n--- 方式二：Basic 认证（最简单的登录方式）---");
        System.out.println("\n【Basic 认证的特点】");
        System.out.println("  - 最简单的认证方式");
        System.out.println("  - 适合内部系统、测试环境");
        System.out.println("  - 生产环境建议用 HTTPS\n");
        
        PasswordEncoder passwordEncoder = BasicSecurity.passwordEncoder();
        
        System.out.println("步骤 1: 创建用户详情服务（存储用户）");
        UserDetailsService userDetailsService = BasicSecurity.inMemoryUserDetailsService();
        
        System.out.println("\n步骤 2: 创建用户");
        String adminPassword = passwordEncoder.encode("admin123");
        User adminUser = BasicSecurity.newUser("admin", adminPassword);
        userDetailsService.save(adminUser);
        
        String userPassword = passwordEncoder.encode("user123");
        User normalUser = BasicSecurity.newUser("user", userPassword);
        userDetailsService.save(normalUser);
        
        System.out.println("   已创建用户: admin, user");
        
        System.out.println("\n步骤 3: 创建认证提供者");
        var authProvider = BasicSecurity.authenticationProvider(userDetailsService, passwordEncoder);
        
        System.out.println("\n步骤 4: 测试登录（正确的用户名和密码）");
        Authentication adminAuthRequest = createAuthRequest("admin", "admin123");
        Authentication adminResult = authProvider.authenticate(adminAuthRequest);
        System.out.println("   Admin 登录成功: " + adminResult.isAuthenticated());
        
        System.out.println("\n步骤 5: 测试登录（正确的用户名，错误的密码）");
        Authentication wrongPasswordRequest = createAuthRequest("admin", "wrongPassword");
        try {
            Authentication wrongResult = authProvider.authenticate(wrongPasswordRequest);
            System.out.println("   错误密码登录: " + wrongResult.isAuthenticated());
        } catch (Exception e) {
            System.out.println("   错误密码登录失败: " + e.getMessage());
        }
        
        System.out.println("\n步骤 6: 测试登录（不存在的用户）");
        Authentication notFoundRequest = createAuthRequest("nonexistent", "anyPassword");
        try {
            Authentication notFoundResult = authProvider.authenticate(notFoundRequest);
            System.out.println("   不存在的用户登录: " + notFoundResult.isAuthenticated());
        } catch (Exception e) {
            System.out.println("   不存在的用户登录失败: " + e.getMessage());
        }
        
        logger.info("Basic 认证示例完成");
    }
    
    private static void jwtAuthenticationExample() {
        System.out.println("\n--- 方式三：JWT 认证（现代 Web 应用常用）---");
        System.out.println("\n【JWT 是什么？】");
        System.out.println("  - JSON Web Token，一种无状态的认证方式");
        System.out.println("  - 服务器不需要存储会话，扩展性好");
        System.out.println("  - 适合前后端分离、移动应用\n");
        System.out.println("【JWT 的工作流程】");
        System.out.println("  1. 用户登录成功 → 服务器生成 JWT Token");
        System.out.println("  2. 客户端保存 Token（比如在浏览器 LocalStorage）");
        System.out.println("  3. 每次请求带上 Token");
        System.out.println("  4. 服务器验证 Token，确认用户身份\n");
        
        System.out.println("   💡 JWT 就像一张入场券，有了它就能进场！");
        
        logger.info("JWT 认证示例说明完成");
    }
    
    private static void apiKeyAuthenticationExample() {
        System.out.println("\n--- 方式四：API Key 认证（适合机器对机器通信）---");
        System.out.println("\n【API Key 是什么？】");
        System.out.println("  - 一串密钥，用于验证 API 调用者身份");
        System.out.println("  - 适合服务器之间通信、第三方集成");
        System.out.println("  - 不需要用户名密码，简单高效\n");
        System.out.println("【API Key 的使用场景】");
        System.out.println("  - 另一个服务调用你的 API");
        System.out.println("  - 移动端 App 调用后端 API");
        System.out.println("  - 第三方开发者集成你的服务\n");
        
        System.out.println("   💡 API Key 就像一把钥匙，只有有钥匙的人才能开门！");
        
        logger.info("API Key 认证示例说明完成");
    }
    
    private static void roleAndPermissionExample() {
        System.out.println("\n--- 方式五：角色和权限管理 ---");
        System.out.println("\n【为什么需要角色和权限？】");
        System.out.println("  - 不是所有人都能做所有事");
        System.out.println("  - 管理员可以删除用户，普通用户不行");
        System.out.println("  - 编辑可以写文章，访客只能看\n");
        
        System.out.println("【常见的角色】");
        System.out.println("  - ADMIN（管理员）：所有权限");
        System.out.println("  - EDITOR（编辑）：可以创建、编辑内容");
        System.out.println("  - USER（普通用户）：只能看自己的内容");
        System.out.println("  - GUEST（访客）：只能看公开内容\n");
        
        System.out.println("【权限的例子】");
        System.out.println("  - user:read（查看用户）");
        System.out.println("  - user:write（创建用户）");
        System.out.println("  - user:delete（删除用户）");
        System.out.println("  - article:read（查看文章）");
        System.out.println("  - article:write（写文章）\n");
        
        System.out.println("步骤 1: 定义角色和权限的关系");
        System.out.println("   ADMIN 拥有: user:read, user:write, user:delete, article:read, article:write");
        System.out.println("   EDITOR 拥有: article:read, article:write");
        System.out.println("   USER 拥有: article:read");
        
        System.out.println("\n步骤 2: 实际应用场景");
        System.out.println("   用户访问 /admin/delete-user → 检查是否有 user:delete 权限");
        System.out.println("   用户访问 /article/create → 检查是否有 article:write 权限");
        
        logger.info("角色和权限管理示例完成");
    }
    
    private static Authentication createAuthRequest(String username, String password) {
        return new DefaultAuthentication(null, password) {
            @Override
            public User getUser() {
                return BasicSecurity.newUser(username, "");
            }
        };
    }
}
