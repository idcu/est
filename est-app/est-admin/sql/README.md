# EST Admin Database Scripts

This directory contains database initialization scripts for EST Admin.

## Files

- `schema.sql` - Database schema definition (table structures)
- `data.sql` - Initial data (users, roles, menus, etc.)

## Requirements

- MySQL 8.0+ or MariaDB 10.5+

## Quick Start

### 1. Create Database

```sql
CREATE DATABASE IF NOT EXISTS `est_admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `est_admin`;
```

### 2. Import Schema

```bash
mysql -u root -p est_admin < schema.sql
```

Or in MySQL client:

```sql
source /path/to/est-app/est-admin/sql/schema.sql;
```

### 3. Import Initial Data

```bash
mysql -u root -p est_admin < data.sql
```

Or in MySQL client:

```sql
source /path/to/est-app/est-admin/sql/data.sql;
```

## Default Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Super Admin |

## Database Tables

### Core Tables

- `sys_tenant` - Tenant management
- `sys_department` - Department organization
- `sys_role` - Role definitions
- `sys_user` - User accounts
- `sys_menu` - Menu and permission configuration
- `sys_role_menu` - Role-menu relationships

### Log Tables

- `sys_login_log` - Login history logs
- `sys_operation_log` - System operation logs

### Integration Tables

- `sys_email_template` - Email template management
- `sys_sms_template` - SMS template management

## Character Set

All tables use `utf8mb4` character set to support full Unicode including emojis.

## Notes

1. The password in `data.sql` is a BCrypt hash of `admin123`
2. Modify `schema.sql` and `data.sql` according to your needs before importing
3. Always backup your database before importing
4. For production use, change the default passwords immediately

## Cleanup

To drop all tables and start fresh:

```sql
DROP DATABASE IF EXISTS `est_admin`;
CREATE DATABASE `est_admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
