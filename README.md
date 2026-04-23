# 在线考试管理系统

## 项目简介

这是一个基于前后端分离架构的在线考试管理系统。

### 技术栈

**后端：**
- Java 8
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3.1
- MySQL 8
- JWT

**前端：**
- Vue 3
- TypeScript
- Vite 5
- Element Plus
- Pinia
- Axios

## 项目结构

```
online_exam_management/
├── backend/                 # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/exam/
│   │       │   ├── common/       # 通用类
│   │       │   ├── config/       # 配置类
│   │       │   ├── context/      # 上下文
│   │       │   ├── controller/   # 控制器
│   │       │   ├── dto/          # 数据传输对象
│   │       │   ├── entity/       # 实体类
│   │       │   ├── exception/    # 异常处理
│   │       │   ├── interceptor/  # 拦截器
│   │       │   ├── mapper/       # 持久层
│   │       │   ├── service/      # 业务层
│   │       │   ├── util/         # 工具类
│   │       │   └── vo/           # 视图对象
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── layouts/
│   │   ├── router/
│   │   ├── stores/
│   │   ├── utils/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.ts
│   ├── index.html
│   ├── vite.config.ts
│   └── package.json
└── sql/                     # 数据库脚本
    └── init.sql
```

## 快速开始

### 1. 数据库配置

1. 创建数据库
2. 执行 `sql/init.sql` 脚本

```sql
source /path/to/sql/init.sql
```

3. 修改 `backend/src/main/resources/application.yml` 中的数据库连接信息

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_exam?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
```

### 2. 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 `http://localhost:5173` 启动

## 功能模块

### 1. 用户登录
- JWT认证
- 角色权限管理

### 2. 题库管理
- 创建、编辑、删除题库
- 题目类型：单选、多选、判断、填空、简答

### 3. 题目管理
- 题目CRUD操作
- 题目分类管理

### 4. 试卷管理
- 试卷的创建和编辑
- 添加题目到试卷
- 设置分值

### 5. 考试发布
- 设置考试时间
- 考试状态管理

### 6. 参加考试
- 在线答题
- 自动计时
- 自动判分

### 7. 成绩查询
- 查看考试记录
- 查看成绩详情

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 教师 | teacher1 | 123456 |
| 学生 | student1 | 123456 |

## 主要接口说明

### 认证模块
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/currentUser` - 获取当前用户信息

### 题库管理
- `GET /api/questionBank/page` - 分页查询
- `POST /api/questionBank` - 新增
- `PUT /api/questionBank` - 修改
- `DELETE /api/questionBank/{id}` - 删除

### 题目管理
- `GET /api/question/page` - 分页查询
- `POST /api/question` - 新增
- `PUT /api/question` - 修改
- `DELETE /api/question/{id}` - 删除

### 试卷管理
- `GET /api/examPaper/page` - 分页查询
- `POST /api/examPaper` - 新增
- `GET /api/examPaper/{id}/questions` - 查询试卷题目
- `POST /api/examPaper/{id}/addQuestion` - 添加题目
- `DELETE /api/examPaper/question/{id}` - 移除题目

### 考试管理
- `GET /api/exam/page` - 分页查询
- `POST /api/exam` - 发布考试
- `PUT /api/exam` - 修改
- `DELETE /api/exam/{id}` - 删除

### 考试记录
- `POST /api/examRecord/start/{examId}` - 开始考试
- `POST /api/examRecord/submit/{recordId}` - 提交答卷
- `GET /api/examRecord/my` - 我的考试记录

## 系统特性

1. **JWT认证**：安全的用户认证体系
2. **全局异常处理**：统一的异常响应格式
3. **统一返回结果**：标准化的API响应
4. **分页查询**：使用MyBatis-Plus分页插件
5. **自动判分**：客观题自动评分
6. **跨域处理**：前后端分离架构的CORS配置

## 注意事项

1. 确保Java版本 >= 8
2. 确保Node版本 >= 16
3. 确保MySQL服务正常运行
4. 首次运行前请先执行数据库脚本
