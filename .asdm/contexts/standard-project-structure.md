# 标准项目结构文档

## 概述
本文档定义了 qa-live-healthcare-interview 工作区的标准项目结构。它提供了组织文件和目录的指南，以保持一致性并促进协作。

## 项目结构概览

### 整体结构
```
qa-live-healthcare-interview/
├── .asdm/                              # ASDM 配置和工具集
│   ├── contexts/                       # AI 模型上下文文件
│   │   ├── index.md                   # 工作区索引和指南
│   │   ├── data-models.md             # 数据模型和关系
│   │   ├── standard-project-structure.md  # 本文档
│   │   ├── standard-coding-style.md   # 编码标准（待生成）
│   │   ├── deployment.md              # 部署配置（待生成）
│   │   ├── api.md                     # API 文档（待生成）
│   │   └── architecture.md            # 系统架构（待生成）
│   └── toolsets/                       # 已安装的 ASDM 工具集
│       ├── context-builder/            # 上下文构建工具
│       ├── basic-tools/                # 基础工具集
│       └── prd-builder/                # PRD 构建工具
├── server/                             # 后端服务目录
│   ├── qa-service-user/                # 用户管理服务（端口 8080）
│   │   ├── src/main/java/              # Java 源代码
│   │   │   └── com/leansofx/qaserviceuser/
│   │   │       ├── controller/         # REST 控制器
│   │   │       ├── service/           # 业务逻辑层
│   │   │       ├── repository/         # 数据访问层
│   │   │       ├── entity/            # 实体类
│   │   │       └── config/            # 配置类
│   │   ├── src/main/resources/         # 资源文件
│   │   │   ├── application.properties # 应用配置
│   │   │   └── data.sql              # 测试数据脚本
│   │   ├── src/test/java/             # 测试代码
│   │   ├── pom.xml                    # Maven 配置
│   │   ├── mvnw                       # Maven Wrapper (Unix)
│   │   └── mvnw.cmd                  # Maven Wrapper (Windows)
│   ├── qa-service-question/           # 问题管理服务（端口 8081）
│   │   ├── src/main/java/              # Java 源代码
│   │   ├── src/main/resources/         # 资源文件
│   │   ├── src/test/java/             # 测试代码
│   │   ├── pom.xml                    # Maven 配置
│   │   ├── mvnw                       # Maven Wrapper (Unix)
│   │   └── mvnw.cmd                  # Maven Wrapper (Windows)
│   └── qa-service-statistic/          # 统计服务（待开发）
├── web/                                # 前端应用目录
│   └── qa-web/                        # 主前端项目（Vue 3 + Vite）
│       ├── src/                        # 源代码目录
│       │   ├── api/                    # API 接口定义
│       │   ├── components/             # Vue 组件
│       │   ├── views/                  # 页面视图
│       │   ├── router/                 # 路由配置
│       │   ├── store/                  # 状态管理（Pinia）
│       │   ├── locales/                # 国际化资源文件
│       │   ├── data/                   # 本地数据文件（模拟）
│       │   ├── assets/                 # 静态资源
│       │   ├── App.vue                 # 根组件
│       │   ├── main.ts                 # 应用入口
│       │   └── style.css              # 全局样式
│       ├── public/                     # 公共资源目录
│       ├── dist/                       # 生产构建输出
│       ├── package.json                # npm 依赖配置
│       ├── vite.config.ts             # Vite 配置
│       └── tsconfig*.json             # TypeScript 配置
├── docker/                             # Docker 配置目录
│   └── init-db.sql                    # 数据库初始化脚本
├── docs/                               # 项目文档
│   ├── API_TEST.md                     # API 测试文档
│   ├── PATIENT_LOGIN_PRD.md           # 患者登录 PRD 文档
│   ├── PRD_TEMPLATE.md                 # PRD 模板
│   ├── prompts.md                     # AI 提示词记录
│   └── TEST_REPORT.md                 # 测试报告
├── .codebuddy/                        # Tencent CodeBuddy 配置
│   └── commands/                      # CodeBuddy 命令
├── docker-compose.yml                  # Docker Compose 配置
├── README.md                           # 项目说明文档
└── todo.md                            # 待办事项
```

## 目录用途和规范

### 根目录文件
| 文件/目录 | 用途 | 规范 |
|-----------|------|------|
| `docker-compose.yml` | Docker 容器编排配置 | 定义 MySQL、phpMyAdmin 等服务 |
| `README.md` | 项目说明文档 | 使用 Markdown 格式，包含项目介绍、快速启动 |
| `todo.md` | 待办事项记录 | 记录开发计划和进度 |

### 后端服务结构（Spring Boot）

#### 标准 Maven 项目结构
```
qa-service-user/
├── src/main/java/com/leansofx/qaserviceuser/
│   ├── controller/         # REST API 控制器
│   ├── service/            # 业务逻辑层
│   ├── repository/         # 数据访问接口（JPA Repository）
│   ├── entity/             # JPA 实体类
│   ├── dto/                # 数据传输对象（待添加）
│   ├── config/             # 配置类（CORS、Security 等）
│   └── QaServiceUserApplication.java  # 应用入口
├── src/main/resources/
│   ├── application.properties  # 应用配置文件
│   ├── data.sql              # 数据初始化脚本
│   └── schema.sql            # 表结构脚本（可选）
└── src/test/java/         # 单元测试和集成测试
```

#### 包组织原则
- **按层次组织**: controller → service → repository → entity
- **保持单一职责**: 每个类只负责一个功能
- **避免循环依赖**: service 依赖 repository，controller 依赖 service

#### 命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 控制器 | `XxxController` | `DoctorController.java` |
| 服务类 | `XxxService` | `DoctorService.java` |
| 服务实现 | `XxxServiceImpl` | `DoctorServiceImpl.java`（待引入） |
| 数据仓库 | `XxxRepository` | `DoctorRepository.java` |
| 实体类 | `Xxx` | `Doctor.java` |
| DTO | `XxxDto` 或 `XxxRequest/Response` | `PatientDto.java` |
| 配置文件 | `XxxConfig` | `CorsConfig.java` |

### 前端结构（Vue 3 + TypeScript）

#### 标准 Vue 3 项目结构
```
qa-web/
├── src/
│   ├── api/                # API 请求封装
│   │   ├── auth.ts         # 认证相关 API
│   │   └── doctor.ts       # 医生相关 API
│   ├── components/         # 可复用组件
│   │   ├── AppHeader.vue   # 头部组件
│   │   └── AppFooter.vue   # 底部组件
│   ├── views/              # 页面组件
│   │   ├── Home.vue        # 首页
│   │   ├── Doctors.vue     # 医生列表页
│   │   ├── Consultation.vue # 问诊页面
│   │   ├── PatientLogin.vue # 患者登录页
│   │   ├── PatientRegister.vue # 患者注册页
│   │   └── DoctorLogin.vue # 医生登录页
│   ├── router/             # Vue Router 配置
│   │   └── index.ts        # 路由定义
│   ├── store/              # Pinia 状态管理
│   │   └── index.ts        # Store 定义
│   ├── locales/            # 国际化文件
│   │   ├── zh-CN.ts        # 中文语言包
│   │   └── en-US.ts        # 英文语言包
│   ├── data/               # 本地模拟数据
│   │   ├── doctor-user-list.json  # 医生数据
│   │   ├── patient-user.json      # 患者数据
│   │   └── question-list.json     # 问题数据
│   └── assets/             # 静态资源
├── public/                 # 公共资源（不会被 Vite 处理）
└── dist/                   # 生产构建输出
```

#### 命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 页面组件 | `PascalCase.vue` | `Home.vue`, `Doctors.vue` |
| 复用组件 | `PascalCase.vue` | `AppHeader.vue` |
| API 文件 | `kebab-case.ts` | `doctor-api.ts` |
| 路由文件 | `index.ts` | - |
| Store 文件 | `index.ts` 或 `kebab-case.ts` | `user-store.ts` |
| 类型定义 | `PascalCase.ts` 或 `types.ts` | `Doctor.ts` |

## 技术栈特定结构

### Spring Boot 项目结构
```
src/main/java/com/example/
├── controller/             # REST 控制器（表现层）
├── service/                # 业务逻辑层
│   └── impl/              # 服务实现类（待引入）
├── repository/             # 数据访问层（JPA Repository）
├── entity/                 # 实体类（JPA Entity）
├── dto/                    # 数据传输对象（待引入）
├── vo/                     # 视图对象（待引入）
├── config/                 # 配置类
├── exception/              # 异常处理（待引入）
├── interceptor/            # 拦截器（待引入）
└── QaServiceUserApplication.java  # 应用入口
```

### Vue 3 + TypeScript 项目结构
```
src/
├── api/                    # API 封装（axios 封装）
├── types/                  # TypeScript 类型定义
├── utils/                  # 工具函数
├── composables/            # 组合式函数（待引入）
├── directives/             # 自定义指令（待引入）
├── plugins/                # 插件（待引入）
└── layouts/                # 布局组件（待引入）
```

## 目录组织最佳实践

### 1. 模块化组织
- **后端**: 按业务功能划分子包（如：`auth/`, `doctor/`, `patient/`）
- **前端**: 按功能模块组织视图和组件

### 2. 层次分离
- **后端**: 严格遵循 Controller → Service → Repository → Entity 层次
- **前端**: 分离视图（views）、组件（components）、逻辑（composables）

### 3. 配置文件外置
- **后端**: 使用 `application.properties` 或 `application.yml`
- **前端**: 使用 `.env` 文件管理环境变量

### 4. 测试文件镜像
- **后端**: `src/test/` 镜像 `src/main/` 的包结构
- **前端**: `tests/` 或 `__tests__/` 目录存放测试文件（待配置）

## 文件命名约定

### 通用规则
| 类型 | 规则 | 示例 |
|------|------|------|
| 目录名 | kebab-case（短横线） | `qa-service-user/`, `doctor-api/` |
| Java 文件 | PascalCase（大驼峰） | `DoctorController.java` |
| TypeScript 文件 | kebab-case 或 PascalCase | `doctor-api.ts` 或 `DoctorApi.ts` |
| 配置文件 | kebab-case 或 snake_case | `application.properties`, `data.sql` |
| Markdown 文件 | kebab-case | `API_TEST.md`, `standard-project-structure.md` |

### 测试文件命名
| 类型 | 规则 | 示例 |
|------|------|------|
| Java 测试 | `XxxTests.java` | `QaServiceUserApplicationTests.java` |
| TypeScript 测试 | `xxx.spec.ts` 或 `xxx.test.ts` | `DoctorApi.spec.ts` |

## 代码组织原则

### 1. 单一职责原则（SRP）
- 每个类/函数只负责一个功能
- Controller 只处理 HTTP 请求和响应
- Service 只处理业务逻辑
- Repository 只处理数据访问

### 2. 开闭原则（OCP）
- 对扩展开放，对修改关闭
- 使用接口和抽象类定义扩展点

### 3. 依赖倒置原则（DIP）
- 高层模块不应依赖低层模块
- 两者都应依赖抽象

### 4. 接口隔离原则（ISP）
- 不应强迫客户端依赖它们不需要的接口
- 拆分大型接口为更小的专用接口

## 构建和部署结构

### Docker 配置
```
docker/
├── init-db.sql                # 数据库初始化脚本
└── (待添加: Dockerfile 用于后端服务)

docker-compose.yml             # 容器编排配置
```

### 环境配置
```
server/qa-service-user/src/main/resources/
├── application.properties          # 默认配置
├── application-dev.properties     # 开发环境配置（待添加）
├── application-prod.properties    # 生产环境配置（待添加）
└── application-test.properties    # 测试环境配置（待添加）
```

## 文档结构

### 项目文档组织
```
docs/
├── API_TEST.md                 # API 测试流程和用例
├── PATIENT_LOGIN_PRD.md       # 患者登录功能 PRD
├── PRD_TEMPLATE.md            # PRD 文档模板
├── prompts.md                 # AI 提示词使用记录
├── TEST_REPORT.md             # 测试执行报告
└── (待添加: architecture.md)  # 架构设计文档
```

### 代码文档规范
- **Java**: 使用 Javadoc 注释
- **TypeScript**: 使用 JSDoc 注释
- **复杂逻辑**: 添加行内注释说明
- **公共 API**: 必须添加文档注释

## 版本控制规范

### .gitignore 配置
```
# Java
*.class
*.jar
*.war
target/

# Node
node_modules/
dist/
.env

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
```

### 分支命名规范
| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 特性分支 | `feature/xxx` | `feature/patient-login` |
| 修复分支 | `fix/xxx` | `fix/doctor-api-error` |
| 文档分支 | `docs/xxx` | `docs/api-documentation` |
| 重构分支 | `refactor/xxx` | `refactor/user-service` |

## 自定义指南

虽然本文档提供了标准结构，但项目可能需要根据实际情况进行调整：

1. **框架要求**: 遵循框架的特定约定（如 Spring Boot、Vue 3）
2. **团队偏好**: 适应团队已建立的代码组织模式
3. **项目规模**: 对于小型项目，可以简化结构
4. **业务复杂度**: 根据业务领域需求调整模块划分

**重要**: 任何对标准结构的偏离都应在项目 README 或本文档中记录。

## 相关源码文件

| 文件 | 路径 | 说明 |
|------|------|------|
| QaServiceUserApplication.java | `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/QaServiceUserApplication.java` | 用户服务入口 |
| QaServiceQuestionApplication.java | `server/qa-service-question/src/main/java/com/leansofx/qaservicequestion/QaServiceQuestionApplication.java` | 问题服务入口 |
| App.vue | `web/qa-web/src/App.vue` | Vue 根组件 |
| main.ts | `web/qa-web/src/main.ts` | Vue 应用入口 |
| index.ts (router) | `web/qa-web/src/router/index.ts` | Vue Router 配置 |
| index.ts (store) | `web/qa-web/src/store/index.ts` | Pinia Store 配置 |
| application.properties | `server/qa-service-user/src/main/resources/application.properties` | 用户服务配置 |
| package.json | `web/qa-web/package.json` | 前端依赖配置 |
| docker-compose.yml | `docker-compose.yml` | Docker 编排配置 |

---

*此项目结构文档应在项目结构发生变化时更新。使用 `/asdm-context-update` 命令保持本文档的最新状态。*
