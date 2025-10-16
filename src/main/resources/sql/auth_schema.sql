-- 应用系统表（多系统支持）
CREATE TABLE `sys_app` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '应用ID',
                           `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码，全局唯一',
                           `app_name` VARCHAR(100) NOT NULL COMMENT '应用名称',
                           `description` VARCHAR(500) COMMENT '应用描述',
                           `redirect_uri` VARCHAR(500) COMMENT '回调地址',
                           `client_id` VARCHAR(100) COMMENT '客户端ID',
                           `client_secret` VARCHAR(200) COMMENT '客户端密钥',
                           `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_app_code` (`app_code`),
                           UNIQUE KEY `uk_client_id` (`client_id`),
                           KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用系统表';

-- 部门表（组织架构）
CREATE TABLE `sys_dept` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
                            `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                            `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
                            `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
                            `ancestors` VARCHAR(500) COMMENT '祖级列表，逗号分隔',
                            `leader` VARCHAR(50) COMMENT '负责人',
                            `phone` VARCHAR(20) COMMENT '联系电话',
                            `email` VARCHAR(100) COMMENT '邮箱',
                            `sort` INT DEFAULT 0 COMMENT '排序',
                            `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `create_by` BIGINT COMMENT '创建人',
                            `update_by` BIGINT COMMENT '更新人',
                            PRIMARY KEY (`id`),
                            KEY `idx_app_code` (`app_code`),
                            KEY `idx_parent_id` (`parent_id`),
                            KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
CREATE TABLE `sys_user` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                            `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                            `username` VARCHAR(64) NOT NULL COMMENT '用户名',
                            `password` VARCHAR(128) NOT NULL COMMENT '密码',
                            `nickname` VARCHAR(64) COMMENT '昵称',
                            `email` VARCHAR(100) COMMENT '邮箱',
                            `phone` VARCHAR(20) COMMENT '手机号',
                            `avatar` VARCHAR(500) COMMENT '头像',
                            `dept_id` BIGINT COMMENT '部门ID',
                            `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `is_super_admin` TINYINT DEFAULT 0 COMMENT '是否超级管理员：0-否，1-是',
                            `last_login_time` DATETIME COMMENT '最后登录时间',
                            `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `create_by` BIGINT COMMENT '创建人',
                            `update_by` BIGINT COMMENT '更新人',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_app_username` (`app_code`, `username`),
                            UNIQUE KEY `uk_app_email` (`app_code`, `email`),
                            UNIQUE KEY `uk_app_phone` (`app_code`, `phone`),
                            KEY `idx_app_code` (`app_code`),
                            KEY `idx_dept_id` (`dept_id`),
                            KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `sys_role` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                            `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                            `name` VARCHAR(100) NOT NULL COMMENT '角色名称',
                            `code` VARCHAR(100) NOT NULL COMMENT '角色编码',
                            `data_scope` TINYINT DEFAULT 1 COMMENT '数据权限范围：1-全部，2-本部门，3-本部门及以下，4-仅自己，5-自定义',
                            `sort` INT DEFAULT 0 COMMENT '排序',
                            `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                            `remark` VARCHAR(500) COMMENT '备注',
                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `create_by` BIGINT COMMENT '创建人',
                            `update_by` BIGINT COMMENT '更新人',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_app_role_code` (`app_code`, `code`),
                            KEY `idx_app_code` (`app_code`),
                            KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限资源表（融合功能权限和数据权限）
CREATE TABLE `sys_permission` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
                                  `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                                  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
                                  `name` VARCHAR(100) NOT NULL COMMENT '权限名称',
                                  `code` VARCHAR(100) NOT NULL COMMENT '权限编码',
                                  `type` TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口，4-数据',
                                  `path` VARCHAR(255) COMMENT '路由路径/接口路径',
                                  `component` VARCHAR(255) COMMENT '前端组件',
                                  `method` VARCHAR(10) COMMENT 'HTTP方法',
                                  `icon` VARCHAR(100) COMMENT '图标',
                                  `sort` INT DEFAULT 0 COMMENT '排序',
                                  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                                  `description` VARCHAR(500) COMMENT '描述',
                                  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `create_by` BIGINT COMMENT '创建人',
                                  `update_by` BIGINT COMMENT '更新人',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_app_permission_code` (`app_code`, `code`),
                                  KEY `idx_app_code` (`app_code`),
                                  KEY `idx_parent_id` (`parent_id`),
                                  KEY `idx_type` (`type`),
                                  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限资源表';

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
                                 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
                                 `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                 `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                 `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `create_by` BIGINT COMMENT '创建人',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
                                 KEY `idx_user_id` (`user_id`),
                                 KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE `sys_role_permission` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
                                       `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                       `permission_id` BIGINT NOT NULL COMMENT '权限ID',
                                       `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `create_by` BIGINT COMMENT '创建人',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
                                       KEY `idx_role_id` (`role_id`),
                                       KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 数据权限规则表（ABAC扩展）
CREATE TABLE `sys_data_permission_rule` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则ID',
                                            `name` VARCHAR(100) NOT NULL COMMENT '规则名称',
                                            `type` TINYINT NOT NULL COMMENT '规则类型：1-部门权限，2-自定义SQL，3-字段过滤',
                                            `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                            `permission_id` BIGINT NOT NULL COMMENT '权限ID',
                                            `condition_expression` JSON COMMENT '条件表达式',
                                            `custom_sql` TEXT COMMENT '自定义SQL',
                                            `field_filters` JSON COMMENT '字段过滤规则',
                                            `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            `create_by` BIGINT COMMENT '创建人',
                                            `update_by` BIGINT COMMENT '更新人',
                                            PRIMARY KEY (`id`),
                                            KEY `idx_role_id` (`role_id`),
                                            KEY `idx_permission_id` (`permission_id`),
                                            KEY `idx_type` (`type`),
                                            KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限规则表';

-- 操作日志表
CREATE TABLE `sys_operate_log` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                                   `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                                   `module` VARCHAR(100) COMMENT '操作模块',
                                   `type` TINYINT NOT NULL COMMENT '操作类型：1-新增，2-修改，3-删除，4-查询',
                                   `content` TEXT NOT NULL COMMENT '操作内容',
                                   `user_id` BIGINT NOT NULL COMMENT '操作人ID',
                                   `user_name` VARCHAR(64) COMMENT '操作人姓名',
                                   `ip_address` VARCHAR(50) COMMENT '操作IP',
                                   `user_agent` VARCHAR(500) COMMENT '用户代理',
                                   `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                   `status` TINYINT DEFAULT 1 COMMENT '操作状态：0-失败，1-成功',
                                   `error_message` TEXT COMMENT '错误信息',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_app_code` (`app_code`),
                                   KEY `idx_user_id` (`user_id`),
                                   KEY `idx_operate_time` (`operate_time`),
                                   KEY `idx_module_type` (`module`, `type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 令牌管理表
CREATE TABLE `sys_token` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '令牌ID',
                             `user_id` BIGINT NOT NULL COMMENT '用户ID',
                             `app_code` VARCHAR(50) NOT NULL COMMENT '应用编码',
                             `access_token` VARCHAR(500) NOT NULL COMMENT '访问令牌',
                             `refresh_token` VARCHAR(500) COMMENT '刷新令牌',
                             `token_type` VARCHAR(20) DEFAULT 'Bearer' COMMENT '令牌类型',
                             `expires_in` INT COMMENT '过期时间(秒)',
                             `refresh_expires_in` INT COMMENT '刷新令牌过期时间(秒)',
                             `scope` VARCHAR(200) COMMENT '权限范围',
                             `issue_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签发时间',
                             `status` TINYINT DEFAULT 1 COMMENT '状态：0-失效，1-有效',
                             `client_id` VARCHAR(100) COMMENT '客户端ID',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_access_token` (`access_token`),
                             UNIQUE KEY `uk_refresh_token` (`refresh_token`),
                             KEY `idx_user_app` (`user_id`, `app_code`),
                             KEY `idx_status` (`status`),
                             KEY `idx_issue_time` (`issue_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='令牌管理表';