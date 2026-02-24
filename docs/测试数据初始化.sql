-- 测试数据初始化脚本（修复版）
-- 执行此脚本前请确保已创建数据库 api_auth

USE api_auth;

-- 清空现有数据（谨慎使用）
-- TRUNCATE TABLE sys_user_role;
-- TRUNCATE TABLE sys_role_permission;
-- TRUNCATE TABLE sys_permission;
-- TRUNCATE TABLE sys_role;
-- TRUNCATE TABLE sys_user;
-- TRUNCATE TABLE sys_dept;
-- TRUNCATE TABLE sys_app;

-- 1. 首先插入默认应用系统
INSERT INTO sys_app (app_code, app_name, description, status, create_time) VALUES
('default', '默认应用系统', 'API认证服务的默认应用系统', 1, NOW());

-- 2. 插入测试部门
INSERT INTO sys_dept (app_code, name, parent_id, ancestors, sort, status, create_time) VALUES
('default', '总公司', 0, '0', 1, 1, NOW()),
('default', '技术部', 1, '0,1', 1, 1, NOW()),
('default', '市场部', 1, '0,1', 2, 1, NOW()),
('default', '人事部', 1, '0,1', 3, 1, NOW());

-- 3. 插入测试用户（密码为 BCrypt 加密的 123456）
INSERT INTO sys_user (app_code, username, password, nickname, email, phone, dept_id, status, is_super_admin, create_time) VALUES
('default', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '超级管理员', 'admin@example.com', '13800138000', 1, 1, 1, NOW()),
('default', 'testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '测试用户', 'test@example.com', '13800138001', 2, 1, 0, NOW()),
('default', 'disabled_user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '禁用用户', 'disabled@example.com', '13800138002', 3, 0, 0, NOW());

-- 4. 插入测试角色
INSERT INTO sys_role (app_code, name, code, data_scope, sort, status, create_time) VALUES
('default', '超级管理员', 'super_admin', 1, 1, 1, NOW()),
('default', '管理员', 'admin', 2, 2, 1, NOW()),
('default', '普通用户', 'user', 4, 3, 1, NOW());

-- 5. 插入测试权限
INSERT INTO sys_permission (app_code, name, code, type, parent_id, sort, status, create_time) VALUES
('default', '系统管理', 'system', 1, 0, 1, 1, NOW()),
('default', '用户管理', 'user', 1, 1, 1, 1, NOW()),
('default', '用户列表', 'user:list', 3, 2, 1, 1, NOW()),
('default', '用户详情', 'user:detail', 3, 2, 2, 1, NOW()),
('default', '创建用户', 'user:create', 3, 2, 3, 1, NOW()),
('default', '更新用户', 'user:update', 3, 2, 4, 1, NOW()),
('default', '删除用户', 'user:delete', 3, 2, 5, 1, NOW()),
('default', '角色管理', 'role', 1, 1, 2, 1, NOW()),
('default', '角色列表', 'role:list', 3, 8, 1, 1, NOW()),
('default', '角色详情', 'role:detail', 3, 8, 2, 1, NOW()),
('default', '创建角色', 'role:create', 3, 8, 3, 1, NOW()),
('default', '更新角色', 'role:update', 3, 8, 4, 1, NOW()),
('default', '删除角色', 'role:delete', 3, 8, 5, 1, NOW()),
('default', '权限管理', 'permission', 1, 1, 3, 1, NOW()),
('default', '权限列表', 'permission:list', 3, 14, 1, 1, NOW()),
('default', '权限详情', 'permission:detail', 3, 14, 2, 1, NOW()),
('default', '创建权限', 'permission:create', 3, 14, 3, 1, NOW()),
('default', '更新权限', 'permission:update', 3, 14, 4, 1, NOW()),
('default', '删除权限', 'permission:delete', 3, 14, 5, 1, NOW()),
('default', '菜单管理', 'menu', 1, 1, 4, 1, NOW()),
('default', '菜单列表', 'menu:list', 3, 20, 1, 1, NOW()),
('default', '菜单详情', 'menu:detail', 3, 20, 2, 1, NOW()),
('default', '创建菜单', 'menu:create', 3, 20, 3, 1, NOW()),
('default', '更新菜单', 'menu:update', 3, 20, 4, 1, NOW()),
('default', '删除菜单', 'menu:delete', 3, 20, 5, 1, NOW());

-- 6. 分配用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),  -- admin 用户分配超级管理员角色
(2, 3);  -- testuser 用户分配普通用户角色

-- 7. 分配角色权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE status = 1;  -- 超级管理员拥有所有权限

INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission WHERE code IN ('user:list', 'user:detail', 'role:list', 'role:detail', 'permission:list', 'permission:detail', 'menu:list', 'menu:detail');  -- 普通用户只有查看权限

-- 验证数据
SELECT '应用系统数据' AS '表名', COUNT(*) AS '记录数' FROM sys_app
UNION ALL
SELECT '用户数据', COUNT(*) FROM sys_user
UNION ALL
SELECT '角色数据', COUNT(*) FROM sys_role
UNION ALL
SELECT '权限数据', COUNT(*) FROM sys_permission
UNION ALL
SELECT '用户角色关系', COUNT(*) FROM sys_user_role
UNION ALL
SELECT '角色权限关系', COUNT(*) FROM sys_role_permission
UNION ALL
SELECT '部门数据', COUNT(*) FROM sys_dept;
