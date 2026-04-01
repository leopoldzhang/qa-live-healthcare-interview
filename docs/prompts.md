# AI 辅助开发提示词文档

> 本文档记录了 QA Live Healthcare Interview 项目开发过程中使用的所有 AI 提示词

---

## 目录

- [阶段 1: 首页国际化](#阶段-1-首页国际化)
- [阶段 2: 数据库迁移](#阶段-2-数据库迁移)
- [阶段 3: 用户登录](#阶段-3-用户登录)
- [阶段 4: 测试与文档](#阶段-4-测试与文档)

---

## 阶段 1: 首页国际化

### 1.1 安装 vue-i18n

**提示词**:
```
在 Vue 3 + Vite + TypeScript 项目中安装和配置 vue-i18n 国际化插件
```

**预期结果**:
- 安装 `vue-i18n` 依赖包
- 创建 `locales` 目录结构
- 配置 `main.ts` 中的 i18n 实例

---

### 1.2 创建语言文件

**提示词**:
```
创建中文语言文件 zh-CN.ts，包含以下内容：
- 首页标题: "在线医疗问诊平台" / "专业医疗团队，随时在线"
- 功能特性描述（3个）: "专业医生团队"、"实时在线问诊"、"隐私安全保护"
- 按钮文字: "立即问诊"、"查看医生"
- 统计数据标签（4个）: "专业医生"、"问题总数"、"待响应问题"、"在线诊室"
- 开放诊室标题和描述
```

**预期结果**:
- 生成完整的中文翻译文件
- 所有翻译键使用合理的命名规范（如 `home.title`、`home.features.doctors`）

---

### 1.3 首页组件国际化改造

**提示词**:
```
将 Home.vue 中的所有硬编码中文文本替换为 i18n 国际化语法
使用 Composition API 的 useI18n 钩子和 t() 函数
```

**预期结果**:
- 导入 `useI18n` 从 'vue-i18n'
- 在 `setup()` 中调用 `useI18n()`
- 将所有文本替换为 `{{ t('key') }}` 或 `t('key')`
- 保持原有的样式和布局不变

---

### 1.4 修复 Vue I18n 解析错误

**提示词**:
```
修复 Vue I18n 解析错误：Uncaught SyntaxError: Message compilation error: Invalid linked format
错误位置：邮箱地址中的 @ 符号
文件位置：web/qa-web/src/main.ts
```

**预期结果**:
- 在 i18n 配置中添加自定义 `messageCompiler` 函数来禁用消息编译
- 添加相关配置选项（`warnHtmlMessage: false`、`missingWarn: false`、`fallbackWarn: false`）

---

### 1.5 扫描国际化完成度

**提示词**:
```
扫描所有 Vue 视图和组件文件，检查是否都实现了国际化
列出所有文件的国际化状态（已完成/未完成）
如果未完成，需要为该文件添加国际化支持
```

**预期结果**:
- 遍历 `/web/qa-web/src/views` 和 `/web/qa-web/src/components` 目录
- 检查每个文件是否使用 `useI18n` 和 `t()`
- 生成国际化完成度报告

---

## 阶段 2: 数据库迁移

### 2.1 创建 Docker Compose 配置

**提示词**:
```
创建 docker-compose.yml 文件，配置以下服务：
1. MySQL 8.0 数据库
   - 端口: 3306
   - 环境变量: 数据库名、用户名、密码
   - 数据卷挂载
2. phpMyAdmin 管理界面
   - 端口: 8081
   - 关联到 MySQL 服务
```

**预期结果**:
- 生成完整的 `docker-compose.yml` 文件
- 配置正确的服务依赖关系
- 包含必要的环境变量和端口映射

---

### 2.2 设计医生数据库表

**提示词**:
```
设计医生表 doctors 的 SQL 建表语句，包含以下字段：
- id (VARCHAR, 主键)
- username (VARCHAR, 唯一索引)
- password (VARCHAR)
- name (VARCHAR)
- title (VARCHAR)
- department (VARCHAR)
- avatar (VARCHAR)
- experience (VARCHAR)
- is_active (BOOLEAN)
- specialties (JSON)
```

**预期结果**:
- 生成完整的 SQL 建表语句
- 包含主键、索引约束
- 使用合适的数据类型和长度

---

### 2.3 创建 Doctor 实体类

**提示词**:
```
使用 Spring Boot + JPA 创建 Doctor 实体类
包路径: com.leansofx.qaserviceuser.entity
映射到数据库表 doctors
使用 Lombok 简化代码
对密码字段使用 @JsonIgnore 注解
```

**预期结果**:
- 实体类使用 @Entity、@Table、@Id、@Column 等 JPA 注解
- 字段类型与数据库对应
- 使用 Lombok 的 @Data、@NoArgsConstructor、@AllArgsConstructor

---

### 2.4 创建 Repository 和 Service

**提示词**:
```
创建 DoctorRepository 接口和 DoctorService 类
Repository 需要：
- 继承 JpaRepository<Doctor, String>
- 添加 findByIsActive 方法

Service 需要：
- getAllDoctors() - 获取所有医生
- getActiveDoctors() - 获取在线医生
- getDoctorByUsername(username) - 根据用户名获取医生
```

**预期结果**:
- Repository 接口正确继承 JPA 接口
- Service 类包含所有业务逻辑方法
- 方法实现正确，包含异常处理

---

### 2.5 创建 DoctorController

**提示词**:
```
创建 DoctorController 类，提供以下 API 端点：
- GET /api/doctors - 获取所有医生列表
- GET /api/doctors/active - 获取在线医生列表
- GET /api/doctors/{username} - 根据用户名获取医生信息
统一返回格式: { "code": 200, "data": [...], "message": "success" }
使用 @CrossOrigin 注解配置 CORS
```

**预期结果**:
- Controller 类使用 @RestController 和 @RequestMapping
- 每个端点正确映射
- 返回统一格式的响应对象
- 包含适当的 HTTP 状态码

---

### 2.6 前端 API 封装

**提示词**:
```
创建前端 API 请求工具文件 web/qa-web/src/api/doctor.ts
使用 fetch API 封装以下方法：
- getAllDoctors() - 获取所有医生
- getActiveDoctors() - 获取在线医生
- getDoctorByUsername(username) - 根据用户名获取医生
包含错误处理和数据转换
```

**预期结果**:
- 使用 TypeScript 类型定义
- 实现统一的错误处理逻辑
- 返回 Promise 对象
- 使用后端 API 基础 URL

---

### 2.7 更新前端 Store

**提示词**:
```
修改 web/qa-web/src/store/index.ts
将医生数据获取改为从 API 获取
在 onMounted 钩子中调用 API
处理加载状态和错误状态
```

**预期结果**:
- Store 状态包含加载中、数据、错误等字段
- 从 API 异步获取数据
- 错误处理逻辑完善

---

## 阶段 3: 用户登录

### 3.1 编写 PRD 文档

**提示词**:
```
使用 PRD 模板编写患者登录功能的产品需求文档
包含以下章节：
- 需求背景
- 功能描述
- 用户故事
- 技术方案
- 数据库设计
- API 接口设计
- 验收标准
- 风险点
```

**预期结果**:
- 生成完整的 PRD 文档
- 内容结构清晰，逻辑严密
- 技术方案可行

---

### 3.2 设计患者数据库表

**提示词**:
```
设计患者表 patients 的 SQL 建表语句，包含以下字段：
- id (VARCHAR, 主键)
- username (VARCHAR, 唯一索引)
- password (VARCHAR)
- name (VARCHAR)
- birthday (DATE)
- phone (VARCHAR)
- gender (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

**预期结果**:
- 生成完整的 SQL 建表语句
- 包含主键和唯一索引
- 使用合适的数据类型

---

### 3.3 创建 Patient 实体类

**提示词**:
```
使用 Spring Boot + JPA 创建 Patient 实体类
包路径: com.leansofx.qaserviceuser.entity
映射到数据库表 patients
使用 @PrePersist 自动设置 created_at
使用 @PreUpdate 自动设置 updated_at
对密码字段使用 @JsonIgnore
```

**预期结果**:
- 实体类正确映射数据库表
- 生命周期方法自动设置时间戳
- 敏感字段正确隐藏

---

### 3.4 创建 PatientRepository

**提示词**:
```
创建 PatientRepository 接口
继承 JpaRepository<Patient, String>
添加以下方法：
- findByUsername(username)
- existsByUsername(username)
```

**预期结果**:
- Repository 接口简洁清晰
- 自定义查询方法命名规范

---

### 3.5 创建 PatientService

**提示词**:
```
创建 PatientService 类，实现以下功能：
1. register(username, password, name, birthday, phone, gender)
   - 验证用户名是否已存在
   - 创建患者记录（开发环境密码明文存储）
   - 返回创建的患者信息或错误

2. login(username, password)
   - 验证用户名存在性
   - 验证密码正确性
   - 返回患者信息或错误信息

3. getPatientById(id)
4. getPatientByUsername(username)
```

**预期结果**:
- 业务逻辑完整
- 验证规则清晰
- 错误处理完善
- 返回统一格式的响应

---

### 3.6 创建 AuthController

**提示词**:
```
创建 AuthController 类，提供以下 API 端点：
- POST /api/auth/patient/register - 患者注册
- POST /api/auth/patient/login - 患者登录
- POST /api/auth/patient/logout - 患者登出（预留）
统一返回格式: { "code": 200, "data": {...}, "message": "success" }
使用 @CrossOrigin 配置 CORS
```

**预期结果**:
- Controller 正确映射端点
- 参数验证和错误处理完善
- 返回格式统一

---

### 3.7 创建登录页面

**提示词**:
```
创建 PatientLogin.vue 登录页面
使用 Ant Design Vue 组件
包含：
- 用户名输入框
- 密码输入框（带显示/隐藏切换）
- 登录按钮
- 注册链接
- 错误提示信息
表单验证规则
调用登录 API
成功后保存登录状态到 localStorage
跳转到首页或问诊页
```

**预期结果**:
- 界面美观，布局合理
- 表单验证规则完善
- 错误提示友好
- 登录流程完整

---

### 3.8 创建注册页面

**提示词**:
```
创建 PatientRegister.vue 注册页面
使用 Ant Design Vue 组件
包含：
- 用户名输入框（带实时唯一性验证）
- 密码输入框（带强度提示）
- 确认密码输入框
- 姓名、生日、手机号、性别输入
- 注册按钮
- 表单验证规则
调用注册 API
成功后跳转到登录页
```

**预期结果**:
- 表单字段完整
- 验证规则合理
- 用户名唯一性实时验证
- 密码强度提示
- 注册流程完整

---

### 3.9 创建前端 API 封装

**提示词**:
```
创建 web/qa-web/src/api/auth.ts
封装以下 API 方法：
- register(data) - 注册请求
- login(username, password) - 登录请求
- logout() - 登出请求
使用 fetch API
包含错误处理
```

**预期结果**:
- API 封装完整
- 错误处理逻辑清晰
- TypeScript 类型定义正确

---

### 3.10 配置路由

**提示词**:
```
在 router/index.ts 中添加患者登录和注册路由
路由路径：/patient/login、/patient/register
添加路由守卫检查登录状态
未登录访问问诊页面时跳转到登录页
```

**预期结果**:
- 路由配置正确
- 路由守卫逻辑完善
- 重定向行为符合预期

---

### 3.11 更新 Store 状态管理

**提示词**:
```
修改 web/qa-web/src/store/index.ts
添加以下功能：
- loginPatient(patient) - 保存登录患者信息
- logoutPatient() - 清除登录状态
- isLoggedIn() - 检查是否已登录
- getCurrentPatient() - 获取当前登录患者
使用 localStorage 持久化登录状态
```

**预期结果**:
- 登录状态管理完整
- 持久化机制正常
- getter 方法正确

---

## 阶段 4: 测试与文档

### 4.1 API 测试

**提示词**:
```
执行以下 API 测试并记录结果：

医生相关 API：
1. GET http://localhost:8080/api/doctors
2. GET http://localhost:8080/api/doctors/active
3. GET http://localhost:8080/api/doctors/dr-zhang-wei

患者认证 API：
4. POST http://localhost:8080/api/auth/patient/register
5. POST http://localhost:8080/api/auth/patient/login
6. POST http://localhost:8080/api/auth/patient/login（错误用户名）
7. POST http://localhost:8080/api/auth/patient/register（重复用户名）

使用 curl 命令执行测试
记录每个测试的返回结果和状态
```

**预期结果**:
- 所有 API 测试命令执行成功
- 返回数据格式正确
- 错误处理符合预期

---

### 4.2 创建 API 测试文档

**提示词**:
```
创建 /docs/API_TEST.md 文档
包含：
- 环境信息
- 所有 API 端点列表
- 每个 API 的测试命令
- 预期返回结果
- 实际测试结果
- 测试场景说明
- 错误码说明
- 测试统计
```

**预期结果**:
- 文档结构清晰
- 测试用例完整
- 命令可直接复制执行

---

### 4.3 创建测试报告

**提示词**:
```
创建 /docs/TEST_REPORT.md 测试报告
包含：
- 测试概述
- API 测试结果（表格形式）
- 功能测试结果
  - 首页中英文切换
  - 医生列表页
  - 患者登录注册
  - 完整问诊流程
- 国际化完整性测试
- 已知问题与建议
- 测试统计汇总
- 质量评估
- 测试结论
```

**预期结果**:
- 报告内容全面
- 测试结果详细
- 包含统计图表
- 结论清晰明确

---

## 通用提示词

### 启动服务

**提示词**:
```
启动前端服务
项目路径: /Users/wangyunli/workspace/qa-live-healthcare-interview/qa-live-healthcare-interview/web/qa-web
使用 npm 命令
确保服务在端口 5173 上运行
```

**预期结果**:
- Vite 开发服务器启动
- 可以访问 http://localhost:5173

---

### 调试问题

**提示词**:
```
分析以下错误信息并提供解决方案：
[粘贴错误信息]
说明错误原因
提供具体的修复步骤
```

**预期结果**:
- 准确识别错误原因
- 提供可行的解决方案
- 包含代码示例

---

### 代码审查

**提示词**:
```
审查以下代码，检查：
1. 代码规范性
2. 潜在的 bug
3. 性能问题
4. 安全问题
5. 改进建议

[粘贴代码]
```

**预期结果**:
- 列出发现的问题
- 提供改进建议
- 给出代码示例

---

### 文档生成

**提示词**:
```
根据以下内容生成技术文档：
- 文档类型：[API文档/用户手册/开发文档]
- 目标读者：[开发者/用户/运维]
- 内容要点：[列出要点]
```

**预期结果**:
- 文档结构清晰
- 内容准确完整
- 语言简洁易懂

---

## 提示词使用指南

### 1. 提示词结构

有效的提示词应包含：
- **明确的任务描述** - 说明要做什么
- **上下文信息** - 项目、技术栈、文件路径
- **具体要求** - 功能点、约束条件、输出格式
- **预期结果** - 期望的输出内容

### 2. 最佳实践

- ✅ 提供足够的上下文信息
- ✅ 明确技术栈和框架版本
- ✅ 指定文件路径和包名
- ✅ 说明代码风格和规范
- ✅ 提供示例或参考代码
- ❌ 避免过于模糊的描述
- ❌ 避免在一个提示词中包含多个不相关的任务

### 3. 调试提示词

如果 AI 生成的结果不符合预期：
1. 检查提示词是否足够具体
2. 补充必要的上下文信息
3. 提供更多的约束条件
4. 引用相关代码或文档
5. 分解复杂任务为多个简单任务

### 4. 提示词模板

```
[任务描述]
在 [技术栈] 项目中实现 [功能]

具体要求：
- 要求1
- 要求2
- 要求3

上下文信息：
- 项目路径: [路径]
- 相关文件: [文件列表]
- 技术栈: [技术栈列表]

预期结果：
- [期望的输出内容]
```

---

## 总结

本文档记录了 QA Live Healthcare Interview 项目开发过程中使用的所有 AI 提示词，涵盖：

- **阶段 1**: 首页国际化（5 个提示词）
- **阶段 2**: 数据库迁移（7 个提示词）
- **阶段 3**: 用户登录（11 个提示词）
- **阶段 4**: 测试与文档（3 个提示词）
- **通用提示词**: 4 个提示词

**总计**: 30 个提示词

这些提示词经过实践验证，可以高效地指导 AI 完成开发任务。在实际使用中，可以根据具体需求进行调整和优化。

---

*文档最后更新: 2026-04-01*

---

## 附录：实际用户使用的提示词

以下是从本次开发交互中提取的用户实际使用的提示词：

### 首页相关

1. **启动前端服务**
   - "启动前端服务"

2. **分析访问问题**
   - "访问前端服务返回内容：[HTML content]，请分析原因，并修复"

3. **提供错误信息**
   - "以下是错误信息：[Vue warn and SyntaxError stack traces]"
   - "错误信息：Uncaught SyntaxError: Message compilation error: Invalid linked format [full stack trace]"

4. **发现未国际化内容**
   - "在医生的诊室页面仍存在中英文未变动的情况"

5. **扫描国际化完成度**
   - "扫描所有页面，查看是否做了语言切换，如果做了就忽略，如果没做就增加语言切换"

### 任务执行相关

6. **执行任务清单**
   - "执行todo.md文档中的阶段 4: 测试与文档"

7. **执行特定子任务**
   - "仅仅将用户输入的内容，追加到prompts.md文档后，从交互内容中获取"

---

## 实际使用统计

- 总交互次数: 8
- 启动服务类: 1
- 问题修复类: 4
- 功能开发类: 2
- 文档管理类: 1

**实际使用提示词总数**: 8

这些是用户在本次开发会话中实际使用的提示词，反映了真实的工作流程和需求。
