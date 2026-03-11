-- EST Admin Database Schema
-- MySQL 8.0+
-- Database: est_admin

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` varchar(64) NOT NULL COMMENT 'Tenant ID',
  `name` varchar(128) NOT NULL COMMENT 'Tenant name',
  `code` varchar(64) NOT NULL COMMENT 'Tenant code',
  `domain` varchar(256) DEFAULT NULL COMMENT 'Tenant domain',
  `mode` varchar(32) NOT NULL DEFAULT 'COLUMN' COMMENT 'Tenant mode: COLUMN, SCHEMA, DATABASE',
  `active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is active',
  `created_at` bigint NOT NULL COMMENT 'Created timestamp',
  `expires_at` bigint DEFAULT NULL COMMENT 'Expires timestamp',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`code`),
  KEY `idx_tenant_domain` (`domain`),
  KEY `idx_tenant_active` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Tenant Table';

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` varchar(64) NOT NULL COMMENT 'Department ID',
  `parent_id` varchar(64) DEFAULT NULL COMMENT 'Parent department ID',
  `name` varchar(128) NOT NULL COMMENT 'Department name',
  `code` varchar(64) NOT NULL COMMENT 'Department code',
  `sort` int NOT NULL DEFAULT 0 COMMENT 'Sort order',
  `leader` varchar(64) DEFAULT NULL COMMENT 'Leader user ID',
  `phone` varchar(32) DEFAULT NULL COMMENT 'Phone number',
  `email` varchar(128) DEFAULT NULL COMMENT 'Email',
  `active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is active',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code` (`code`, `tenant_id`),
  KEY `idx_dept_parent` (`parent_id`),
  KEY `idx_dept_tenant` (`tenant_id`),
  KEY `idx_dept_active` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Department Table';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT 'Role ID',
  `name` varchar(128) NOT NULL COMMENT 'Role name',
  `description` varchar(512) DEFAULT NULL COMMENT 'Role description',
  `permissions` text COMMENT 'Permissions (JSON array)',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Is default role',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`, `tenant_id`),
  KEY `idx_role_tenant` (`tenant_id`),
  KEY `idx_role_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Role Table';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT 'User ID',
  `username` varchar(64) NOT NULL COMMENT 'Username',
  `password` varchar(256) NOT NULL COMMENT 'Password (encrypted)',
  `email` varchar(128) DEFAULT NULL COMMENT 'Email',
  `phone` varchar(32) DEFAULT NULL COMMENT 'Phone number',
  `nickname` varchar(64) DEFAULT NULL COMMENT 'Nickname',
  `avatar` varchar(512) DEFAULT NULL COMMENT 'Avatar URL',
  `active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is active',
  `locked` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Is locked',
  `department_id` varchar(64) DEFAULT NULL COMMENT 'Department ID',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `roles` text COMMENT 'Roles (JSON array)',
  `permissions` text COMMENT 'Additional permissions (JSON array)',
  `last_login_at` bigint DEFAULT NULL COMMENT 'Last login timestamp',
  `last_login_ip` varchar(64) DEFAULT NULL COMMENT 'Last login IP',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint NOT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_user_dept` (`department_id`),
  KEY `idx_user_tenant` (`tenant_id`),
  KEY `idx_user_active` (`active`),
  KEY `idx_user_locked` (`locked`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System User Table';

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT 'Menu ID',
  `parent_id` varchar(64) DEFAULT NULL COMMENT 'Parent menu ID',
  `name` varchar(128) NOT NULL COMMENT 'Menu name',
  `path` varchar(256) DEFAULT NULL COMMENT 'Route path',
  `component` varchar(256) DEFAULT NULL COMMENT 'Component path',
  `icon` varchar(128) DEFAULT NULL COMMENT 'Icon',
  `sort` int NOT NULL DEFAULT 0 COMMENT 'Sort order',
  `type` varchar(32) NOT NULL DEFAULT 'MENU' COMMENT 'Menu type: DIRECTORY, MENU, BUTTON',
  `visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is visible',
  `cache` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is cache enabled',
  `permissions` text COMMENT 'Permissions (JSON array)',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  KEY `idx_menu_parent` (`parent_id`),
  KEY `idx_menu_tenant` (`tenant_id`),
  KEY `idx_menu_visible` (`visible`),
  KEY `idx_menu_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Menu Table';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` varchar(64) NOT NULL COMMENT 'Role ID',
  `menu_id` varchar(64) NOT NULL COMMENT 'Menu ID',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
  KEY `idx_rm_role` (`role_id`),
  KEY `idx_rm_menu` (`menu_id`),
  KEY `idx_rm_tenant` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Role-Menu Relation Table';

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` varchar(64) NOT NULL COMMENT 'Log ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'User ID',
  `username` varchar(64) DEFAULT NULL COMMENT 'Username',
  `ip` varchar(64) DEFAULT NULL COMMENT 'Login IP',
  `location` varchar(128) DEFAULT NULL COMMENT 'Login location',
  `browser` varchar(128) DEFAULT NULL COMMENT 'Browser',
  `os` varchar(128) DEFAULT NULL COMMENT 'Operating system',
  `status` varchar(32) NOT NULL COMMENT 'Status: SUCCESS, FAILURE',
  `message` varchar(512) DEFAULT NULL COMMENT 'Message',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_at` bigint NOT NULL COMMENT 'Created timestamp',
  PRIMARY KEY (`id`),
  KEY `idx_ll_user` (`user_id`),
  KEY `idx_ll_username` (`username`),
  KEY `idx_ll_tenant` (`tenant_id`),
  KEY `idx_ll_status` (`status`),
  KEY `idx_ll_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Login Log Table';

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` varchar(64) NOT NULL COMMENT 'Log ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'User ID',
  `username` varchar(64) DEFAULT NULL COMMENT 'Username',
  `module` varchar(64) DEFAULT NULL COMMENT 'Module',
  `operation` varchar(128) DEFAULT NULL COMMENT 'Operation',
  `method` varchar(16) DEFAULT NULL COMMENT 'HTTP method',
  `url` varchar(512) DEFAULT NULL COMMENT 'Request URL',
  `params` text COMMENT 'Request parameters (JSON)',
  `result` text COMMENT 'Result (JSON)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP address',
  `location` varchar(128) DEFAULT NULL COMMENT 'Location',
  `duration` bigint DEFAULT NULL COMMENT 'Duration (ms)',
  `status` varchar(32) NOT NULL COMMENT 'Status: SUCCESS, FAILURE',
  `error_msg` text COMMENT 'Error message',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `created_at` bigint NOT NULL COMMENT 'Created timestamp',
  PRIMARY KEY (`id`),
  KEY `idx_ol_user` (`user_id`),
  KEY `idx_ol_username` (`username`),
  KEY `idx_ol_module` (`module`),
  KEY `idx_ol_tenant` (`tenant_id`),
  KEY `idx_ol_status` (`status`),
  KEY `idx_ol_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Operation Log Table';

-- ----------------------------
-- Table structure for sys_email_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_template`;
CREATE TABLE `sys_email_template` (
  `id` varchar(64) NOT NULL COMMENT 'Template ID',
  `name` varchar(128) NOT NULL COMMENT 'Template name',
  `code` varchar(64) NOT NULL COMMENT 'Template code',
  `subject` varchar(256) NOT NULL COMMENT 'Email subject',
  `content` text NOT NULL COMMENT 'Email content (HTML)',
  `variables` text COMMENT 'Variables (JSON array)',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is active',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_email_code` (`code`, `tenant_id`),
  KEY `idx_email_tenant` (`tenant_id`),
  KEY `idx_email_active` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System Email Template Table';

-- ----------------------------
-- Table structure for sys_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_sms_template`;
CREATE TABLE `sys_sms_template` (
  `id` varchar(64) NOT NULL COMMENT 'Template ID',
  `name` varchar(128) NOT NULL COMMENT 'Template name',
  `code` varchar(64) NOT NULL COMMENT 'Template code',
  `content` varchar(512) NOT NULL COMMENT 'SMS content',
  `variables` text COMMENT 'Variables (JSON array)',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT 'Tenant ID',
  `active` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'Is active',
  `created_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
  `updated_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
  `created_at` bigint DEFAULT NULL COMMENT 'Created timestamp',
  `updated_at` bigint DEFAULT NULL COMMENT 'Updated timestamp',
  `remark` varchar(512) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sms_code` (`code`, `tenant_id`),
  KEY `idx_sms_tenant` (`tenant_id`),
  KEY `idx_sms_active` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System SMS Template Table';

SET FOREIGN_KEY_CHECKS = 1;
