# 工作区上下文索引

## 概述
本文档作为 AI 模型理解和操作本工作区的索引和指南。它提供了工作区内容的结构化概览，并指导 AI 模型找到相关的上下文信息。

## 工作区信息

### 基本信息
- **工作区名称**: qa-live-healthcare-interview
- **描述**: 医疗健康问答直播访谈系统 - 一个支持医生和患者在线问诊、咨询的问答平台
- **创建日期**: 2025年
- **最后更新**: 2026年4月29日

### 技术栈
- **主要编程语言**: Java (后端), TypeScript/Vue (前端)
- **框架**: 
  - 后端: Spring Boot 3.5.7, Spring Data JPA
  - 前端: Vue 3.5.10, Vite 5.4.8, Ant Design Vue 4.2.6
- **构建工具**: Maven (后端), npm (前端)
- **数据库**: MySQL 8.0 (生产环境), H2 (开发环境)
- **测试框架**: JUnit 5 (后端), 待定 (前端)
- **部署平台**: Docker, Docker Compose

### 业务背景
- **业务领域**: 医疗健康、在线问诊
- **关键业务流程**:
  1. 患者注册/登录
  2. 医生登录
  3. 浏览医生列表
  4. 发起问诊咨询
  5. 医生接诊与回复
  6. 问诊记录管理
- **业务规则**:
  - 患者需注册账号才能发起问诊
  - 医生需通过专门登录入口访问
  - 问诊过程支持实时问答

## 工作区结构

### 文件树及指南
```
qa-live-healthcare-interview/
├── .asdm/                          # ASDM 配置和工具集
│   ├── contexts/                   # 上下文文件（当前目录）
│   │   ├── index.md               # 本文档
│   │   ├── architecture.md        # 系统架构
│   │   ├── data-models.md        # 数据模型
│   │   ├── api.md                # API 文档
│   │   ├── deployment.md         # 部署配置
│   │   ├── standard-coding-style.md   # 编码标准
│   │   └── standard-project-structure.md   # 项目结构
│   ├── workspace/                  # 工作区文件
│   │   └── features/             # 功能特性文档
│   └── toolsets/                   # 已安装的工具集
│       ├── context-builder/        # 上下文构建工具
│       ├── basic-tools/            # 基础工具集
│       └── prd-builder/            # PRD 构建工具
├── web/                            # 前端应用目录
│   └── qa-web/                     # 主前端项目（Vue 3）
│       ├── src/
│       │   ├── api/                # API 接口定义
│       │   │   ├── auth.ts        # 认证 API
│       │   │   ├── doctor.ts      # 医生 API
│       │   │   └── appointment.ts # 预约 API
│       │   ├── components/         # Vue 组件
│       │   ├── views/              # 页面视图
│       │   │   ├── appointment/   # 预约相关页面
│       │   │   │   ├── AddAppointment.vue
│       │   │   │   ├── AppointmentList.vue
│       │   │   │   └── DoctorSchedule.vue
│       │   │   ├── Home.vue
│       │   │   ├── Doctors.vue
│       │   │   ├── Consultation.vue
│       │   │   ├── PatientLogin.vue
│       │   │   ├── PatientRegister.vue
│       │   │   └── DoctorLogin.vue
│       │   ├── router/             # 路由配置
│       │   ├── store/              # 状态管理
│       │   ├── locales/            # 国际化资源文件
│       │   └── data/               # 本地数据文件
│       ├── public/                 # 静态资源
│       ├── package.json            # 前端依赖配置
│       └── vite.config.ts         # Vite 配置
├── server/                         # 后端服务目录
│   ├── qa-service-user/            # 用户管理服务（端口 8080）
│   │   ├── src/main/java/          # Java 源代码
│   │   │   └── com/leansofx/qaserviceuser/
│   │   │       ├── controller/     # REST 控制器
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── DoctorController.java
│   │   │       │   ├── AppointmentController.java  # 预约管理
│   │   │       │   └── ScheduleController.java      # 排班管理
│   │   │       ├── service/        # 业务逻辑层
│   │   │       │   ├── PatientService.java
│   │   │       │   ├── DoctorService.java
│   │   │       │   ├── AppointmentService.java
│   │   │       │   └── ScheduleService.java
│   │   │       ├── repository/     # 数据访问层
│   │   │       │   ├── PatientRepository.java
│   │   │       │   ├── DoctorRepository.java
│   │   │       │   ├── AppointmentRepository.java
│   │   │       │   └── DoctorScheduleRepository.java
│   │   │       ├── entity/        # 实体类
│   │   │       │   ├── Patient.java
│   │   │       │   ├── Doctor.java
│   │   │       │   ├── Appointment.java        # 预约实体
│   │   │       │   └── DoctorSchedule.java     # 排班实体
│   │   │       └── dto/           # 数据传输对象
│   │   │           ├── AppointmentRequest.java
│   │   │           └── ScheduleRequest.java
│   │   ├── src/main/resources/     # 配置文件
│   │   ├── src/test/java/         # 测试代码
│   │   ├── pom.xml                # Maven 配置
│   │   └── mvnw                   # Maven wrapper
│   └── qa-service-question/        # 问题管理服务（端口 8081）
│       ├── src/main/java/          # Java 源代码
│       ├── src/main/resources/     # 配置文件
│       ├── pom.xml                # Maven 配置
│       └── mvnw                   # Maven wrapper
├── docker/                         # Docker 配置目录
│   ├── init-db.sql                # 数据库初始化脚本
│   └── appointment-init-db.sql    # 预约数据库初始化脚本
├── docker-compose.yml              # Docker Compose 配置
├── docs/                           # 项目文档
├── _TRAINING_ASSETS/              # 培训资源
└── README.md                       # 项目说明文档
```

### 关键目录说明
- **`.asdm/contexts/`**: 包含所有供 AI 模型参考的上下文文件
- **`web/qa-web/src/`**: 前端主要源代码 - AI 应重点关注此目录进行前端实现
- **`server/qa-service-user/src/`**: 用户服务后端代码
- **`server/qa-service-question/src/`**: 问题服务后端代码
- **`docs/`**: 文档目录 - AI 应参考其中的 API 和使用详情

## 开发指南

### 构建和编译
```bash
# 前端开发服务器
cd web/qa-web
npm install
npm run dev

# 前端生产构建
npm run build

# 后端用户服务
cd server/qa-service-user
./mvnw spring-boot:run

# 后端问题服务
cd server/qa-service-question
./mvnw spring-boot:run
```

### 测试
```bash
# 前端测试（待配置）
cd web/qa-web
npm run test

# 后端测试
cd server/qa-service-user
./mvnw test

cd server/qa-service-question
./mvnw test
```

### 代码质量
- **Linting**: ESLint（前端，待配置）
- **Formatting**: Prettier（前端，待配置）
- **静态分析**: SonarQube（待配置）

## 上下文文件参考

本工作区在 `.asdm/contexts/` 中提供以下上下文文件：

1. **[index.md](./index.md)** - 本文档，工作区索引和指南
2. **[standard-project-structure.md](./standard-project-structure.md)** - 标准项目结构和组织方式（待生成）
3. **[standard-coding-style.md](./standard-coding-style.md)** - 编码标准和风格指南（待生成）
4. **[data-models.md](./data-models.md)** - 数据模型、关系和图表（待生成）
5. **[deployment.md](./deployment.md)** - 部署配置和流程（待生成）
6. **[api.md](./api.md)** - API 定义、端点和文档（待生成）
7. **[architecture.md](./architecture.md)** - 系统架构和设计决策（待生成）

## AI 模型指导

### 如何使用此上下文
1. **从本索引开始** 理解工作区结构
2. **根据当前任务参考特定上下文文件**
3. **遵循开发指南** 进行构建、测试和部署
4. **保持一致性** 与现有模式和约定保持一致

### 常见任务
- **添加新功能**: 首先检查架构和数据模型
- **修改 API**: 参考 API 文档并进行相应更新
- **数据库变更**: 更新数据模型和迁移脚本
- **部署更新**: 遵循部署流程文档

### 故障排除
- 如果某些功能未按预期工作，请检查相关上下文文件
- 对于构建问题，验证依赖项和配置
- 对于运行时问题，检查部署和环境配置

## 服务端口配置

| 服务 | 端口 | 说明 |
|------|------|------|
| qa-service-user | 8080 | 用户管理服务（医生、患者） |
| qa-service-question | 8081 | 问题管理服务 |
| 前端 (Vite) | 5173 | Vue 开发服务器 |
| MySQL | 3306 | 数据库服务 |
| phpMyAdmin | 8081 | 数据库管理界面 |

## 数据库配置

### 开发环境（H2）
- URL: `jdbc:h2:mem:qahealthcare`
- 用户名: `sa`
- 密码: 空
- 控制台: `http://localhost:8080/h2-console`

### 生产环境（MySQL）
- URL: `jdbc:mysql://localhost:3306/qa_healthcare`
- 用户名: `qa_user`
- 密码: `qa_password`
- 管理界面: `http://localhost:8081` (phpMyAdmin)

## 版本历史
| 版本 | 日期 | 变更内容 | 作者 |
|------|------|---------|------|
| 1.0.0 | 2026-04-29 | 初始上下文创建 | AI Assistant |
| 1.1.0 | 2026-04-30 | 添加预约功能相关上下文（Appointment、Schedule） | AI Assistant |

---

*此上下文文件由 Context Builder 工具集维护。当工作区发生变化时，使用 `/asdm-context-update` 命令进行更新。*
