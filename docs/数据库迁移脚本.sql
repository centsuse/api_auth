-- 多应用用户支持数据库迁移脚本
-- 执行前请备份数据库！

USE api_auth;

-- ============================================
-- 第一步：创建新表
-- ============================================

-- 1. 创建新的用户表（全局用户，移除app_code）
CREATE TABLE IF NOT EXISTS `sys_user_new` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名（全局唯一）',
    `password` VARCHAR(128) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(64) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(500) COMMENT '头像',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `is_super_admin` TINYINT DEFAULT 0 COMMENT '是否超级管理员：0-否，1-是',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建人',
    `update_by` BIGINT COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表（全局）';

-- 2. 创建用户应用关联表
CREATE TABLE IF NOT EXISTS `sys_user_app` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
    `dept_id` BIGINT COMMENT '部门ID（在该应用中的部门）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_app` (`user_id`, `app_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_app_code` (`app_code`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户应用关联表';

-- 3. 创建新的用户角色关联表（增加app_code）
CREATE TABLE IF NOT EXISTS `sys_user_role_new` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码（角色所属应用）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT COMMENT '创建人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role_app` (`user_id`, `role_id`, `app_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_app_code` (`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================
-- 第二步：迁移数据
-- ============================================

-- 1. 迁移用户数据（去重，保留最新的记录）
INSERT INTO sys_user_new (id, username, password, nickname, email, phone, avatar, status, is_super_admin, last_login_time, last_login_ip, create_time, update_time, create_by, update_by)
SELECT 
    MIN(id) as id,
    username,
    password,
    nickname,
    email,
    phone,
    avatar,
    MAX(status) as status,
    MAX(is_super_admin) as is_super_admin,
    MAX(last_login_time) as last_login_time,
    MAX(last_login_ip) as last_login_ip,
    MIN(create_time) as create_time,
    MAX(update_time) as update_time,
    MIN(create_by) as create_by,
    MAX(update_by) as update_by
FROM sys_user
GROUP BY username;

-- 2. 创建用户应用关联（从旧的用户表迁移）
INSERT INTO sys_user_app (user_id, app_code, dept_id, status, create_time, create_by)
SELECT 
    un.id as user_id,
    uo.app_code,
    uo.dept_id,
    uo.status,
    uo.create_time,
    uo.create_by
FROM sys_user uo
INNER JOIN sys_user_new un ON uo.username = un.username
WHERE NOT EXISTS (
    SELECT 1 FROM sys_user_app ua 
    WHERE ua.user_id = un.id AND ua.app_code = uo.app_code
);

-- 3. 迁移用户角色关联（增加app_code）
INSERT INTO sys_user_role_new (user_id, role_id, app_code, create_time, create_by)
SELECT 
    un.id as user_id,
    ur.role_id,
    r.app_code,
    ur.create_time,
    ur.create_by
FROM sys_user_role ur
INNER JOIN sys_user uo ON ur.user_id = uo.id
INNER JOIN sys_user_new un ON uo.username = un.username
INNER JOIN sys_role r ON ur.role_id = r.id
WHERE NOT EXISTS (
    SELECT 1 FROM sys_user_role_new urn 
    WHERE urn.user_id = un.id AND urn.role_id = ur.role_id AND urn.app_code = r.app_code
);

-- ============================================
-- 第三步：重命名表（切换到新表）
-- ============================================

-- 备份旧表
RENAME TABLE sys_user TO sys_user_backup;
RENAME TABLE sys_user_role TO sys_user_role_backup;

-- 启用新表
RENAME TABLE sys_user_new TO sys_user;
RENAME TABLE sys_user_role_new TO sys_user_role;

-- ============================================
-- 第四步：验证数据
-- ============================================

-- 验证用户数量
SELECT '原用户表记录数' as '表名', COUNT(*) as '记录数' FROM sys_user_backup
UNION ALL
SELECT '新用户表记录数', COUNT(*) FROM sys_user
UNION ALL
SELECT '用户应用关联记录数', COUNT(*) FROM sys_user_app
UNION ALL
SELECT '原用户角色关联记录数', COUNT(*) FROM sys_user_role_backup
UNION ALL
SELECT '新用户角色关联记录数', COUNT(*) FROM sys_user_role;

-- 验证用户数据完整性
SELECT 
    u.username,
    GROUP_CONCAT(DISTINCT ua.app_code) as '所属应用',
    GROUP_CONCAT(DISTINCT r.name) as '角色列表'
FROM sys_user u
LEFT JOIN sys_user_app ua ON u.id = ua.user_id
LEFT JOIN sys_user_role ur ON u.id = ur.user_id
LEFT JOIN sys_role r ON ur.role_id = r.id
GROUP BY u.id, u.username
LIMIT 10;

-- ============================================
-- 第五步：清理（确认无误后执行）
-- ============================================

-- 删除备份表（谨慎操作！）
-- DROP TABLE IF EXISTS sys_user_backup;
-- DROP TABLE IF EXISTS sys_user_role_backup;

-- ============================================
-- 说明
-- ============================================
-- 1. 执行前请务必备份数据库
-- 2. 建议在测试环境先执行并验证
-- 3. 生产环境执行时请选择低峰期
-- 4. 执行完成后请验证数据完整性
-- 5. 确认无误后再删除备份表
