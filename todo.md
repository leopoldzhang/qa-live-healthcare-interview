# QA Live Healthcare Interview - 任务拆分清单

> 本文档基于 README.md 中的需求列表进行任务拆分，用于指导开发流程

---

## 阶段 0: 项目准备

### 0.1 Git 分支管理
- [ ] Fork 项目到个人 GitHub 账号
- [ ] 克隆项目到本地
- [ ] 创建特性分支（命名规范: `demo`）
- [ ] 确保当前工作在特性分支上

---

## 阶段 1: 需求一 - 首页中英文切换功能

### 1.1 环境准备
- [ ] 安装 vue-i18n 国际化插件
- [ ] 在 `web/qa-web/src/` 目录下创建 `locales` 文件夹

### 1.2 语言资源文件创建
- [ ] 创建中文语言文件 `web/qa-web/src/locales/zh-CN.ts`
  - 首页标题、副标题
  - 功能特性描述（专业医生团队、实时在线问诊、隐私安全保护）
  - 按钮文字（立即问诊、查看医生）
  - 统计数据标签（专业医生、问题总数、待响应问题、在线诊室）
  - 开放诊室标题和描述
- [ ] 创建英文语言文件 `web/qa-web/src/locales/en-US.ts`
  - 对应所有中文内容的英文翻译

### 1.3 国际化配置
- [ ] 在 `web/qa-web/src/main.ts` 中配置 vue-i18n
- [ ] 创建语言切换状态管理（可选，如需持久化）

### 1.4 UI 组件开发
- [ ] 在首页右上角添加语言切换下拉菜单
  - 使用 Ant Design Vue 的 Select 组件
  - 选项: "中文" 和 "English"
- [ ] 实现语言切换逻辑
  - 切换时动态更新 `i18n.global.locale`
  - 确保页面内容实时更新

### 1.5 首页内容国际化
- [ ] 替换 `Home.vue` 中的所有硬编码中文文本为 `{{ $t('key') }}` 格式
- [ ] 测试语言切换功能
  - 验证中文显示正常
  - 验证英文显示正常
  - 验证切换流畅无卡顿

### 1.6 功能验证
- [ ] 手动测试所有文本是否正确显示
- [ ] 检查样式是否因国际化变化而错乱

---

## 阶段 2: 需求二 - 医生数据迁移到 MySQL 数据库

### 2.1 数据库环境搭建
- [ ] 在项目根目录创建 `docker-compose.yml` 文件
  - 配置 MySQL 8.0 服务
  - 配置 phpMyAdmin 管理界面
  - 设置端口映射（MySQL: 3306, phpMyAdmin: 8081）
  - 配置环境变量（数据库密码、用户名等）
- [ ] 启动 Docker 容器
  ```bash
  docker-compose up -d
  ```
- [ ] 通过 phpMyAdmin 访问并验证数据库连接
  - 访问: http://localhost:8081
  - 登录验证
  - 创建数据库 `qa_healthcare`

### 2.2 数据库表设计
- [ ] 设计医生表 `doctors`
  - 字段: id, username, password, name, title, department, avatar, experience, is_active
  - 主键: id
  - 索引: username, is_active
- [ ] 编写 SQL 建表脚本
  [ ] 在 MySQL 中执行建表脚本

### 2.3 数据迁移
- [ ] 导入医生数据到 MySQL
  - 从 `web/qa-web/src/data/doctor-user-list.json` 提取数据
  - 编写 SQL INSERT 语句
  - 执行数据导入

### 2.4 后端服务开发
#### 2.4.1 依赖添加
- [ ] 在 `server/qa-service-user/pom.xml` 中添加依赖
  - `spring-boot-starter-data-jpa`
  - `mysql-connector-java` 或 `com.mysql:mysql-connector-j`
  - `lombok` (可选，简化实体类编写)

#### 2.4.2 配置文件更新
- [ ] 更新 `server/qa-service-user/src/main/resources/application.properties`
  - 配置 MySQL 数据源连接信息
  - 配置 JPA 相关设置（Hibernate 方言、DDL 策略、显示 SQL）
  - 配置 CORS（已存在，确认即可）

#### 2.4.3 实体类创建
- [ ] 创建 `Doctor` 实体类
  - 包路径: `com.leansofx.qaserviceuser.entity`
  - 使用 JPA 注解: `@Entity`, `@Table`, `@Id`, `@Column`
  - 字段映射与数据库表对应
  - 使用 `@JsonIgnore` 排除密码字段

#### 2.4.4 Repository 创建
- [ ] 创建 `DoctorRepository` 接口
  - 包路径: `com.leansofx.qaserviceuser.repository`
  - 继承 `JpaRepository<Doctor, String>`
  - 添加自定义查询方法: `findByIsActive(Boolean isActive)`

#### 2.4.5 Service 层开发
- [ ] 创建 `DoctorService` 类
  - 包路径: `com.leansofx.qaserviceuser.service`
  - 实现 `getAllDoctors()` 方法
  - 实现 `getActiveDoctors()` 方法
  - 实现 `getDoctorByUsername(String username)` 方法

#### 2.4.6 Controller 层开发
- [ ] 创建 `DoctorController` 类
  - 包路径: `com.leansofx.qaserviceuser.controller`
  - 端点 `GET /api/doctors` - 获取所有医生列表
  - 端点 `GET /api/doctors/active` - 获取在线医生列表
  - 端点 `GET /api/doctors/{username}` - 根据用户名获取医生信息
  - 统一返回格式: `{ "code": 200, "data": [...], "message": "success" }`

### 2.5 后端测试
- [ ] 启动后端服务
- [ ] 使用 curl 测试 API 端点
  ```bash
  curl http://localhost:8080/api/doctors
  curl http://localhost:8080/api/doctors/active
  curl http://localhost:8080/api/doctors/dr-zhang-wei
  ```

### 2.6 前端改造
#### 2.6.1 API 调用封装
- [ ] 创建 API 请求工具函数
  - 文件位置: `web/qa-web/src/api/doctor.ts`
  - 使用 `fetch` 或 `axios` 发送 HTTP 请求
  - 实现错误处理

#### 2.6.2 Store 状态改造
- [ ] 修改 `web/qa-web/src/store/index.ts`
  - 将医生数据获取改为从 API 获取
  - 修改 `state.doctors` 初始化方式
  - 修改 `getActiveDoctors()` 和 `getDoctorByUsername()` 方法

#### 2.6.3 页面数据加载
- [ ] 修改 `Doctors.vue`
  - 在 `onMounted` 钩子中调用 API 获取医生列表
  - 处理加载状态和错误状态
- [ ] 修改 `Home.vue`
  - 在 `onMounted` 钩子中调用 API 获取活跃医生列表
  - 更新统计数据获取逻辑（需新增 API）

### 2.7 前后端联调测试
- [ ] 启动前后端服务
- [ ] 验证医生列表页正常显示
- [ ] 验证首页开放诊室正常显示
- [ ] 验证统计数据正确显示
- [ ] 测试异常情况（网络错误、API 异常）

---

## 阶段 3: 需求三 - 问诊用户登录功能

### 3.1 PRD 文档准备
#### 3.1.1 PRD 模板创建
- [x] 创建 `/docs` 目录
- [x] 编写 PRD 模板文档 `/docs/PRD_TEMPLATE.md`
  - 包含: 需求背景、功能描述、用户故事、技术方案、数据库设计、API 接口、验收标准、风险点等

#### 3.1.2 用户登录 PRD 编写
- [x] 使用 PRD 模板编写用户登录 PRD `/docs/PATIENT_LOGIN_PRD.md`
  - 需求背景: 当前使用姓名+生日验证，需要支持用户名密码登录
  - 功能描述: 用户注册、登录、密码找回、登出
  - 用户故事: 作为问诊用户，我希望使用用户名密码登录，以便更安全地管理账户
  - 技术方案: 前端表单 + 后端 API + H2 存储
  - 数据库设计: patients 表新增 username, password, created_at 等字段
  - API 接口: 注册、登录、登出、密码重置等
  - 验收标准: 用户可以注册、登录、登出；密码加密存储；错误提示友好
  - 风险点: 密码安全性、并发注册、用户名冲突等

#### 3.1.3 PRD 文档评审
- [x] 审查 PRD 文档完整性
- [x] 确认功能拆分步骤是否合理

### 3.2 数据库设计
- [x] 设计患者表 `patients`
  - 字段: id, username, password, name, birthday, phone, gender, created_at, updated_at
  - 主键: id
  - 唯一索引: username
- [x] 在 H2 中创建 `patients` 表
- [x] 导入现有患者数据

### 3.3 后端开发
#### 3.3.1 实体类创建
- [x] 创建 `Patient` 实体类
  - 包路径: `com.leansofx.qaserviceuser.entity`
  - 使用 JPA 注解映射数据库表
  - 使用 `@JsonIgnore` 排除敏感字段

#### 3.3.2 Repository 创建
- [x] 创建 `PatientRepository` 接口
  - 继承 `JpaRepository<Patient, String>`
  - 添加 `findByUsername(String username)` 方法
  - 添加 `existsByUsername(String username)` 方法

#### 3.3.3 Service 层开发
- [x] 创建 `PatientService` 类
  - 注册功能: `register(username, password, name, birthday, phone, gender)`
    - 验证用户名是否已存在
    - 密码加密（使用 BCrypt，开发环境暂时使用明文）
    - 创建患者记录
  - 登录功能: `login(username, password)`
    - 验证用户名存在性
    - 验证密码正确性
    - 返回患者信息或错误信息
  - 查询功能: `getPatientById(id)`, `getPatientByUsername(username)`

#### 3.3.4 Controller 层开发
- [x] 创建 `AuthController` 类
  - 端点 `POST /api/auth/patient/register` - 患者注册
  - 端点 `POST /api/auth/patient/login` - 患者登录
  - 端点 `POST /api/auth/patient/logout` - 患者登出
  - 统一返回格式和错误处理

### 3.4 前端开发
#### 3.4.1 登录页面创建
- [x] 创建 `web/qa-web/src/views/PatientLogin.vue`
  - 用户名输入框
  - 密码输入框（带显示/隐藏切换）
  - 登录按钮
  - 注册链接
  - 错误提示信息

#### 3.4.2 注册页面创建
- [x] 创建 `web/qa-web/src/views/PatientRegister.vue`
  - 用户名输入框（带实时唯一性验证）
  - 密码输入框（带强度提示）
  - 确认密码输入框
  - 姓名、生日、手机号、性别输入
  - 注册按钮
  - 表单验证规则

#### 3.4.3 API 封装
- [x] 创建 `web/qa-web/src/api/auth.ts`
  - `register(data)` - 注册请求
  - `login(username, password)` - 登录请求
  - `logout()` - 登出请求

#### 3.4.4 路由配置
- [x] 在 `router/index.ts` 中添加路由
  - `/patient/login` - 登录页
  - `/patient/register` - 注册页
  - 添加路由守卫检查登录状态

#### 3.4.5 状态管理更新
- [x] 修改 `store/index.ts`
  - 添加 `loginPatient(patient)` 方法
  - 添加 `logoutPatient()` 方法
  - 持久化登录状态（使用 localStorage）

#### 3.4.6 首页/问诊页改造
- [x] 修改问诊流程
  - 未登录时跳转到登录页
  - 登录后进入问诊页
  - 显示当前登录用户信息
  - 首页添加登录/登出入口

### 3.5 功能测试
- [x] 测试注册流程
  - 正常注册
  - 用户名重复注册（应失败）
  - 密码不一致（应失败）
- [x] 测试登录流程
  - 正确用户名密码登录
  - 错误用户名登录（应失败）
  - 错误密码登录（应失败）
- [x] 测试登出流程
  - 登出后清除状态
- [x] 测试页面跳转

---

## 阶段 4: 测试与文档

### 4.1 API 测试文档编写
- [ ] 创建 `/docs/API_TEST.md`
  - 列出所有 API 端点
  - 为每个端点提供 curl 测试命令
  - 说明预期返回结果
  - 包含正常和异常场景测试
  - 提供测试报告模板

### 4.2 API 测试执行
- [ ] 测试医生相关 API
  ```bash
  curl -X GET http://localhost:8080/api/doctors
  curl -X GET http://localhost:8080/api/doctors/active
  curl -X GET http://localhost:8080/api/doctors/{username}
  ```
- [ ] 测试患者认证 API
  ```bash
  curl -X POST http://localhost:8080/api/auth/register -d "..."
  curl -X POST http://localhost:8080/api/auth/login -d "..."
  curl -X POST http://localhost:8080/api/auth/logout
  ```
- [ ] 记录测试结果
- [ ] 生成测试报告 `/docs/TEST_REPORT.md`

### 4.3 功能测试
- [ ] 手动测试首页中英文切换
- [ ] 手动测试医生列表页（数据库版）
- [ ] 手动测试患者登录注册功能
- [ ] 端到端测试完整问诊流程

---

## 阶段 5: 交付物准备

### 5.1 文档整理
- [ ] 确认 `/docs/PRD_TEMPLATE.md` 完整
- [ ] 确认 `/docs/PATIENT_LOGIN_PRD.md` 完整
- [ ] 确认 `/docs/API_TEST.md` 完整
- [ ] 确认 `/docs/TEST_REPORT.md` 完整

### 5.2 Prompt 文档
- [ ] 创建 `/docs/prompts.md`
  - 列出本次开发过程中使用的所有 AI 提示词
  - 按功能模块分类
  - 包含使用说明和预期结果

### 5.3 代码提交
- [ ] 提交所有代码到特性分支
- [ ] 确认提交信息规范（使用 .asdm 工具集）
- [ ] 推送分支到远程仓库

### 5.4 创建 Pull Request
- [ ] 在 GitHub 上创建 PR
- [ ] PR 标题清晰描述完成的功能
- [ ] PR 描述详细列出改动内容
- [ ] 关联需求编号

### 5.5 演示视频录制
- [ ] 录制功能演示视频
  - 展示首页中英文切换
  - 展示医生列表页（数据库版）
  - 展示患者登录注册功能
  - 展示完整的问诊流程
- [ ] 视频时长控制在合理范围（建议 5-10 分钟）

---

## 阶段 6: 交付与沟通

### 6.1 材料汇总
- [ ] 准备 PR 链接
- [ ] 准备演示视频
- [ ] 确认所有文档已就绪

### 6.2 提交面试
- [ ] 联系面试官
- [ ] 发送 PR 链接和演示视频

---

## 注意事项

1. **AI 辅助开发原则**
   - 所有代码必须通过 AI 辅助生成
   - 可以对 AI 生成的代码进行手工调整
   - 不得直接从零开始编码

2. **Git 提交规范**
   - 提交信息要清晰明确
   - 使用 `.asdm/toolsets/basic-tools/actions/asdm-git-commit.md` 规范

3. **时间管理**
   - 总时长控制在 1 天内完成
   - 合理分配各阶段时间

4. **质量保证**
   - 每个功能开发完成后立即测试
   - 确保代码可运行、可演示
   - 文档要完整准确

---

## 预估工作量

| 阶段 | 工作量 | 优先级 |
|------|--------|--------|
| 阶段 0: 项目准备 | 0.5h | P0 |
| 阶段 1: 需求一 - 首页国际化 | 1.5h | P1 |
| 阶段 2: 需求二 - 数据库迁移 | 3h | P1 |
| 阶段 3: 需求三 - 用户登录 | 2.5h | P1 |
| 阶段 4: 测试与文档 | 1.5h | P0 |
| 阶段 5: 交付物准备 | 1h | P0 |
| 阶段 6: 交付与沟通 | 0.5h | P0 |
| **总计** | **10.5h** | - |

---

## 参考资源

- README.md - 面试需求文档
- .asdm/toolsets/ - AI 辅助工具集
- web/qa-web/src/data/ - 前端数据文件
- server/qa-service-user/ - 用户服务后端

---

*最后更新: 2026-04-01*
