-- 在线考试管理系统数据库初始化脚本
-- MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS online_exam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE online_exam;

-- 1. 用户表
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 角色表
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. 用户角色关联表
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 4. 题库表
CREATE TABLE `question_bank` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题库ID',
    `bank_name` VARCHAR(100) NOT NULL COMMENT '题库名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题库表';

-- 5. 题目表
CREATE TABLE `question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `bank_id` BIGINT NOT NULL COMMENT '题库ID',
    `question_type` TINYINT NOT NULL COMMENT '题目类型: 1-单选题, 2-多选题, 3-判断题, 4-填空题, 5-简答题',
    `difficulty` TINYINT DEFAULT 1 COMMENT '难度: 1-简单, 2-中等, 3-困难',
    `question_content` TEXT NOT NULL COMMENT '题目内容',
    `options` TEXT DEFAULT NULL COMMENT '选项(JSON格式)',
    `answer` TEXT NOT NULL COMMENT '答案',
    `analysis` TEXT DEFAULT NULL COMMENT '解析',
    `score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '分值',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_bank_id` (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='题目表';

-- 6. 试卷表
CREATE TABLE `exam_paper` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '试卷ID',
    `paper_name` VARCHAR(100) NOT NULL COMMENT '试卷名称',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `total_score` DECIMAL(5,2) DEFAULT 100.00 COMMENT '总分',
    `pass_score` DECIMAL(5,2) DEFAULT 60.00 COMMENT '及格分',
    `duration` INT DEFAULT 60 COMMENT '考试时长(分钟)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷表';

-- 7. 试卷题目关联表
CREATE TABLE `exam_paper_question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `paper_id` BIGINT NOT NULL COMMENT '试卷ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `question_order` INT DEFAULT 0 COMMENT '题目顺序',
    `question_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '题目分值',
    PRIMARY KEY (`id`),
    KEY `idx_paper_id` (`paper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='试卷题目关联表';

-- 8. 考试表
CREATE TABLE `exam` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考试ID',
    `exam_name` VARCHAR(100) NOT NULL COMMENT '考试名称',
    `paper_id` BIGINT NOT NULL COMMENT '试卷ID',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未开始, 1-进行中, 2-已结束, 3-已取消',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_user_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    PRIMARY KEY (`id`),
    KEY `idx_paper_id` (`paper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试表';

-- 9. 考试记录表
CREATE TABLE `exam_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `exam_id` BIGINT NOT NULL COMMENT '考试ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `paper_id` BIGINT NOT NULL COMMENT '试卷ID',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始作答时间',
    `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
    `score` DECIMAL(5,2) DEFAULT NULL COMMENT '得分',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未开始, 1-进行中, 2-已提交, 3-已批改',
    `answers` TEXT DEFAULT NULL COMMENT '考生答案(JSON格式)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_exam_id` (`exam_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考试记录表';

-- ==================== 初始化数据 ====================

-- 初始化角色数据
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`) VALUES
('超级管理员', 'ADMIN', '超级管理员，拥有所有权限'),
('教师', 'TEACHER', '教师，可以管理题库、试卷、考试'),
('学生', 'STUDENT', '学生，可以参加考试、查看成绩');

-- 初始化用户数据 (密码都是 123456，使用BCrypt加密)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `email`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 'admin@example.com', 1),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张老师', 'teacher1@example.com', 1),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李四', 'student1@example.com', 1),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王五', 'student2@example.com', 1);

-- 用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3);

-- 初始化题库数据
INSERT INTO `question_bank` (`bank_name`, `description`, `create_user_id`) VALUES
('Java基础题库', 'Java基础知识相关题目', 1),
('计算机网络题库', '计算机网络基础题目', 1);

-- 初始化题目数据
INSERT INTO `question` (`bank_id`, `question_type`, `difficulty`, `question_content`, `options`, `answer`, `analysis`, `score`, `create_user_id`) VALUES
(1, 1, 1, 'Java中int类型占用多少字节？', '["2字节","4字节","8字节","1字节"]', '1', 'Java中int类型固定占用4字节', 5.00, 1),
(1, 1, 1, '以下哪个是Java的关键字？', '["goto","sizeof","main","String"]', '0', 'goto是Java的保留关键字', 5.00, 1),
(1, 2, 2, '以下哪些是Java的基本数据类型？', '["int","String","boolean","Integer"]', '0,2', 'int和boolean是基本数据类型', 10.00, 1),
(1, 3, 1, 'Java中数组的长度可以通过length属性获取。', '["正确","错误"]', '0', '数组使用length属性，String使用length()方法', 5.00, 1),
(2, 1, 1, 'HTTP协议默认使用的端口号是？', '["80","443","8080","21"]', '0', 'HTTP默认端口80，HTTPS默认443', 5.00, 1),
(2, 1, 2, 'TCP协议属于哪一层？', '["应用层","传输层","网络层","数据链路层"]', '1', 'TCP和UDP都属于传输层协议', 5.00, 1);

-- 初始化试卷
INSERT INTO `exam_paper` (`paper_name`, `description`, `total_score`, `pass_score`, `duration`, `create_user_id`) VALUES
('Java基础测试卷', 'Java基础知识测试', 30.00, 18.00, 30, 1),
('计算机网络基础测试', '计算机网络基础测试', 10.00, 6.00, 20, 1);

-- 试卷题目关联
INSERT INTO `exam_paper_question` (`paper_id`, `question_id`, `question_order`, `question_score`) VALUES
(1, 1, 1, 5.00),
(1, 2, 2, 5.00),
(1, 3, 3, 10.00),
(1, 4, 4, 5.00),
(2, 5, 1, 5.00),
(2, 6, 2, 5.00);

-- 初始化考试
INSERT INTO `exam` (`exam_name`, `paper_id`, `start_time`, `end_time`, `status`, `description`, `create_user_id`) VALUES
('2024年Java基础期中考试', 1, '2024-05-01 08:00:00', '2024-06-30 23:59:59', 1, 'Java基础知识考核', 1),
('计算机网络摸底测试', 2, '2024-05-01 08:00:00', '2024-06-30 23:59:59', 1, '网络基础摸底', 1);
