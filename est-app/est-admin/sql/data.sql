-- EST Admin Database Initial Data
-- MySQL 8.0+

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Tenant data
-- ----------------------------
INSERT INTO `sys_tenant` (`id`, `name`, `code`, `domain`, `mode`, `active`, `created_at`, `expires_at`, `created_by`, `updated_by`, `updated_at`, `remark`) VALUES
('tenant_001', 'Default Tenant', 'default', 'localhost', 'COLUMN', 1, UNIX_TIMESTAMP() * 1000, NULL, 'system', NULL, NULL, 'Default system tenant');

-- ----------------------------
-- Department data
-- ----------------------------
INSERT INTO `sys_department` (`id`, `parent_id`, `name`, `code`, `sort`, `leader`, `phone`, `email`, `active`, `tenant_id`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('dept_001', NULL, 'Headquarters', 'HQ', 1, NULL, '010-88888888', 'hq@example.com', 1, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Headquarters department'),
('dept_002', 'dept_001', 'Technology Department', 'TECH', 1, NULL, '010-88888889', 'tech@example.com', 1, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Technology department'),
('dept_003', 'dept_001', 'Sales Department', 'SALES', 2, NULL, '010-88888890', 'sales@example.com', 1, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Sales department'),
('dept_004', 'dept_001', 'Human Resources', 'HR', 3, NULL, '010-88888891', 'hr@example.com', 1, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Human Resources department');

-- ----------------------------
-- Role data
-- ----------------------------
INSERT INTO `sys_role` (`id`, `name`, `description`, `permissions`, `is_default`, `tenant_id`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('role_admin', 'Super Admin', 'System super administrator with all permissions', '["*"]', 0, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Super administrator role'),
('role_user', 'Normal User', 'Normal user with basic permissions', '["user:info", "user:profile"]', 1, 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Default user role');

-- ----------------------------
-- User data (password: admin123, using BCrypt hash)
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `email`, `phone`, `nickname`, `avatar`, `active`, `locked`, `department_id`, `tenant_id`, `roles`, `permissions`, `last_login_at`, `last_login_ip`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('user_admin', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', 'admin@example.com', '13800138000', 'Administrator', NULL, 1, 0, 'dept_002', 'tenant_001', '["role_admin"]', '[]', NULL, NULL, 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'System administrator');

-- ----------------------------
-- Menu data
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `icon`, `sort`, `type`, `visible`, `cache`, `permissions`, `tenant_id`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('menu_dashboard', NULL, 'Dashboard', '/dashboard', 'dashboard/Dashboard', 'Dashboard', 1, 'MENU', 1, 1, '["dashboard:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Dashboard menu'),
('menu_system', NULL, 'System Management', '/system', NULL, 'Setting', 2, 'DIRECTORY', 1, 1, '[]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'System management directory'),
('menu_user', 'menu_system', 'User Management', '/system/user', 'system/User', 'User', 1, 'MENU', 1, 1, '["system:user:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'User management menu'),
('menu_user_add', 'menu_user', 'Add User', NULL, NULL, NULL, 1, 'BUTTON', 1, 1, '["system:user:add"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Add user button'),
('menu_user_edit', 'menu_user', 'Edit User', NULL, NULL, NULL, 2, 'BUTTON', 1, 1, '["system:user:edit"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Edit user button'),
('menu_user_delete', 'menu_user', 'Delete User', NULL, NULL, NULL, 3, 'BUTTON', 1, 1, '["system:user:delete"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Delete user button'),
('menu_role', 'menu_system', 'Role Management', '/system/role', 'system/Role', 'Lock', 2, 'MENU', 1, 1, '["system:role:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Role management menu'),
('menu_menu', 'menu_system', 'Menu Management', '/system/menu', 'system/Menu', 'Menu', 3, 'MENU', 1, 1, '["system:menu:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Menu management menu'),
('menu_dept', 'menu_system', 'Department Management', '/system/department', 'system/Department', 'OfficeBuilding', 4, 'MENU', 1, 1, '["system:dept:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Department management menu'),
('menu_tenant', 'menu_system', 'Tenant Management', '/system/tenant', 'system/Tenant', 'Building', 5, 'MENU', 1, 1, '["system:tenant:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Tenant management menu'),
('menu_log', NULL, 'Log Management', '/log', NULL, 'Document', 3, 'DIRECTORY', 1, 1, '[]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Log management directory'),
('menu_login_log', 'menu_log', 'Login Log', '/log/login', 'log/LoginLog', 'Document', 1, 'MENU', 1, 1, '["log:login:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Login log menu'),
('menu_operation_log', 'menu_log', 'Operation Log', '/log/operation', 'log/OperationLog', 'DocumentCopy', 2, 'MENU', 1, 1, '["log:operation:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Operation log menu'),
('menu_monitor', NULL, 'System Monitor', '/monitor', NULL, 'Monitor', 4, 'DIRECTORY', 1, 1, '[]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'System monitor directory'),
('menu_online_user', 'menu_monitor', 'Online Users', '/monitor/online', 'monitor/OnlineUser', 'UserFilled', 1, 'MENU', 1, 1, '["monitor:online:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Online users menu'),
('menu_jvm', 'menu_monitor', 'JVM Monitor', '/monitor/jvm', 'monitor/JvmMonitor', 'Cpu', 2, 'MENU', 1, 1, '["monitor:jvm:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'JVM monitor menu'),
('menu_cache', 'menu_monitor', 'Cache Monitor', '/monitor/cache', 'monitor/CacheMonitor', 'DataLine', 3, 'MENU', 1, 1, '["monitor:cache:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Cache monitor menu'),
('menu_integration', NULL, 'Integration', '/integration', NULL, 'Connection', 5, 'DIRECTORY', 1, 1, '[]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Integration directory'),
('menu_email', 'menu_integration', 'Email', '/integration/email', 'integration/Email', 'Message', 1, 'MENU', 1, 1, '["integration:email:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Email integration menu'),
('menu_sms', 'menu_integration', 'SMS', '/integration/sms', 'integration/Sms', 'ChatDotRound', 2, 'MENU', 1, 1, '["integration:sms:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'SMS integration menu'),
('menu_oss', 'menu_integration', 'OSS', '/integration/oss', 'integration/Oss', 'FolderOpened', 3, 'MENU', 1, 1, '["integration:oss:view"]', 'tenant_001', 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'OSS integration menu');

-- ----------------------------
-- Role-Menu relation data
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `tenant_id`, `created_at`) VALUES
('role_admin', 'menu_dashboard', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_system', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_user', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_user_add', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_user_edit', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_user_delete', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_role', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_menu', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_dept', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_tenant', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_log', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_login_log', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_operation_log', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_monitor', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_online_user', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_jvm', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_cache', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_integration', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_email', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_sms', 'tenant_001', UNIX_TIMESTAMP() * 1000),
('role_admin', 'menu_oss', 'tenant_001', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- Email template data
-- ----------------------------
INSERT INTO `sys_email_template` (`id`, `name`, `code`, `subject`, `content`, `variables`, `tenant_id`, `active`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('email_verify', 'Email Verification', 'VERIFY_CODE', 'Email Verification Code', '&lt;!DOCTYPE html&gt;&lt;html&gt;&lt;body&gt;&lt;h3&gt;Hello {{username}},&lt;/h3&gt;&lt;p&gt;Your verification code is: &lt;strong&gt;{{code}}&lt;/strong&gt;&lt;/p&gt;&lt;p&gt;This code will expire in {{expireMinutes}} minutes.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;', '["username","code","expireMinutes"]', 'tenant_001', 1, 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Email verification template'),
('email_reset_pwd', 'Password Reset', 'RESET_PASSWORD', 'Reset Your Password', '&lt;!DOCTYPE html&gt;&lt;html&gt;&lt;body&gt;&lt;h3&gt;Hello {{username}},&lt;/h3&gt;&lt;p&gt;Click the link below to reset your password:&lt;/p&gt;&lt;p&gt;&lt;a href="{{resetLink}}"&gt;Reset Password&lt;/a&gt;&lt;/p&gt;&lt;p&gt;This link will expire in {{expireMinutes}} minutes.&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;', '["username","resetLink","expireMinutes"]', 'tenant_001', 1, 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'Password reset template');

-- ----------------------------
-- SMS template data
-- ----------------------------
INSERT INTO `sys_sms_template` (`id`, `name`, `code`, `content`, `variables`, `tenant_id`, `active`, `created_by`, `updated_by`, `created_at`, `updated_at`, `remark`) VALUES
('sms_verify', 'SMS Verification', 'VERIFY_CODE', 'Your verification code is {{code}}, valid for {{expireMinutes}} minutes.', '["code","expireMinutes"]', 'tenant_001', 1, 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'SMS verification template'),
('sms_notify', 'SMS Notification', 'NOTIFICATION', 'Hello {{username}}, {{content}}', '["username","content"]', 'tenant_001', 1, 'system', NULL, UNIX_TIMESTAMP() * 1000, NULL, 'SMS notification template');

SET FOREIGN_KEY_CHECKS = 1;
