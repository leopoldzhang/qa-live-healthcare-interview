# Feature PRD: 预约挂号功能

**Feature ID**: FEAT-001-Appointment-Registration
**Created Date**: 2026-04-29
**Status**: PLANNED
**Language**: zh (中文)

---

## 1. Overview

### 1.1 Feature Summary

本功能旨在为医疗问诊平台添加**预约挂号**功能，允许患者在线预约医生的线下门诊。

**功能核心**：
- 患者可以浏览医生的可用时间段
- 患者可以选择合适的医生和时间进行预约
- 系统记录预约信息并生成预约单
- 患者可以查看和管理自己的预约记录

**必要性**：
- 当前系统仅支持在线问诊，缺少线下门诊预约功能
- 患者需要进行线下检查、治疗时需要预约挂号功能
- 提升医疗服务的完整性和用户体验

**受益用户**：
- **患者**：便捷地预约医生的线下门诊，节省现场排队时间
- **医生**：更好地管理自己的门诊时间，提高工作效率
- **医院/诊所**：优化资源配置，减少现场混乱

### 1.2 Objectives

- **Objective 1**: 实现患者在线预约医生线下门诊的功能
- **Objective 2**: 提供医生排班管理功能（管理员/医生可配置）
- **Objective 3**: 实现预约记录查询和管理功能
- **Objective 4**: 提供预约提醒功能（可选，后续迭代）

---

## 2. User Stories

### Story 1: 患者预约医生门诊
**As a** 患者用户  
**I want to** 浏览医生的可用门诊时间并预约  
**So that** 我可以在方便的时间去看医生，无需现场排队

**Acceptance Criteria**:
- [ ] 患者可以查看医生列表及每位医生的可用时间段
- [ ] 患者可以选择医生、日期和时间段进行预约
- [ ] 系统显示预约确认信息（医生、时间、地点等）
- [ ] 预约成功后，患者的预约记录中新增一条记录
- [ ] 同一时间段不能重复预约

### Story 2: 患者查看预约记录
**As a** 患者用户  
**I want to** 查看我的所有预约记录  
**So that** 我可以了解我的预约状态，准备就诊

**Acceptance Criteria**:
- [ ] 患者可以查看所有预约记录（待就诊、已完成、已取消）
- [ ] 预约记录显示医生姓名、预约时间、地点、状态等信息
- [ ] 患者可以取消未完成的预约
- [ ] 患者可以查看预约详情

### Story 3: 医生设置排班时间
**As a** 医生用户  
**I want to** 设置我的门诊排班时间  
**So that** 患者可以根据我的可用时间进行预约

**Acceptance Criteria**:
- [ ] 医生可以设置每周的门诊排班（星期、时间段）
- [ ] 医生可以设置门诊地点
- [ ] 医生可以查看自己的所有排班安排
- [ ] 医生可以临时调整或取消某个时间段的排班
- [ ] 排班设置后，患者可以看到可用的预约时间

### Story 4: 管理员管理医生和排班
**As a** 系统管理员  
**I want to** 管理医生信息和排班  
**So that** 确保系统数据的准确性和完整性

**Acceptance Criteria**:
- [ ] 管理员可以添加、编辑、停用医生账号
- [ ] 管理员可以统一管理医生的排班时间
- [ ] 管理员可以查看所有预约记录
- [ ] 管理员可以处理异常预约（如医生临时停诊）

---

## 3. Functional Requirements

### Requirement 1: 医生排班管理
- **ID**: REQ-001
- **Description**: 系统需要支持医生排班信息的设置和管理
- **Priority**: High
- **Related Stories**: Story 3, Story 4

**详细要求**：
- 医生可以设置每周重复的固定排班
- 支持临时排班调整（添加、修改、删除）
- 排班信息包括：医生ID、日期、时间段、地点、最大预约数
- 避免排班时间冲突

### Requirement 2: 患者预约功能
- **ID**: REQ-002
- **Description**: 患者需要能够选择医生和时间段进行预约
- **Priority**: High
- **Related Stories**: Story 1

**详细要求**：
- 患者浏览医生列表时可以看到医生的可用时间段数量
- 患者选择医生后，系统显示该医生未来7天的可用时间段
- 患者选择时间段后，填写预约信息（症状描述、是否需要携带检查报告等）
- 系统检查时间段是否已被预约满
- 预约成功后生成预约单号

### Requirement 3: 预约记录管理
- **ID**: REQ-003
- **Description**: 系统需要记录和展示所有预约信息
- **Priority**: High
- **Related Stories**: Story 1, Story 2

**详细要求**：
- 预约记录状态：PENDING（待确认）、CONFIRMED（已确认）、COMPLETED（已完成）、CANCELLED（已取消）、MISSED（未就诊）
- 患者可以取消 PENDING 和 CONFIRMED 状态的预约
- 医生可以确认或完成预约
- 支持按状态筛选预约记录

### Requirement 4: 可用时间段查询
- **ID**: REQ-004
- **Description**: 系统需要提供可用时间段的查询接口
- **Priority**: Medium
- **Related Stories**: Story 1, Story 3

**详细要求**：
- 根据医生ID和日期范围查询可用时间段
- 返回每个时间段的剩余预约名额
- 考虑已取消的预约，释放名额

### Requirement 5: 预约冲突检测
- **ID**: REQ-005
- **Description**: 系统需要防止重复预约和时间冲突
- **Priority**: High
- **Related Stories**: Story 1

**详细要求**：
- 同一患者不能在同一时间段预约多个医生
- 同一患者在同一医生的同一时间段只能预约一次
- 医生的时间段达到最大预约数后不再接受新的预约

---

## 4. Non-Functional Requirements

### 4.1 Performance
- 预约提交响应时间 < 500ms
- 可用时间段查询响应时间 < 300ms
- 支持至少 100 个并发预约请求

### 4.2 Security
- 患者只能查看和管理自己的预约记录
- 医生只能查看自己的排班和预约患者信息
- 预约数据需要加密存储（敏感信息）
- 需要防止恶意刷预约接口（限流）

### 4.3 Scalability
- 数据库设计需要支持水平扩展
- 排班数据可以考虑缓存（Redis）以提高查询性能

### 4.4 Reliability
- 预约操作需要事务保证，避免数据不一致
- 需要记录预约操作日志，便于问题追踪
- 考虑医生临时停诊的情况，需要通知已预约的患者

### 4.5 Usability
- 预约界面需要简洁明了，步骤清晰
- 提供预约成功/失败的明确提示
- 支持取消预约的二次确认

---

## 5. Technical Requirements

### 5.1 Architecture Considerations
- 预约功能主要涉及 `qa-service-user` 服务（用户、医生管理）
- 需要考虑是否创建新的微服务 `qa-service-appointment` 或集成到现有服务
- **建议**：初期集成到 `qa-service-user`，后续根据业务复杂度考虑拆分

### 5.2 Dependencies
- **内部依赖**：
  - 用户认证系统（已有）
  - 医生信息管理（已有）
- **外部依赖**：
  - 数据库（MySQL）
  - 可能的通知服务（短信/邮件，后续迭代）

### 5.3 Constraints
- 需要保持与现有系统的技术栈一致（Spring Boot, Vue 3, MySQL）
- 前端需要适配现有 UI 风格（Ant Design Vue）
- 数据库修改需要考虑现有数据的迁移

### 5.4 Data Models
需要新增以下数据模型：

**Appointment（预约）**：
- id: 预约ID（主键）
- patientId: 患者ID（外键）
- patientName: 患者姓名
- doctorId: 医生ID（外键）
- doctorName: 医生姓名
- appointmentDate: 预约日期
- timeSlot: 时间段（如：09:00-09:30）
- location: 就诊地点
- status: 预约状态（PENDING/CONFIRMED/COMPLETED/CANCELLED/MISSED）
- description: 症状描述（可选）
- appointmentNo: 预约单号（唯一）
- createTime: 创建时间
- updateTime: 更新时间

**DoctorSchedule（医生排班）**：
- id: 排班ID（主键）
- doctorId: 医生ID（外键）
- doctorName: 医生姓名
- scheduleDate: 排班日期
- timeSlot: 时间段
- location: 就诊地点
- maxAppointments: 最大预约数
- currentAppointments: 当前预约数
- status: 状态（AVAILABLE/UNAVAILABLE）
- createTime: 创建时间
- updateTime: 更新时间

---

## 6. Success Criteria

定义可衡量的成功标准：

- **SC-001**: 患者可以成功完成预约流程（选择医生 → 选择时间 → 确认预约）
- **SC-002**: 医生可以成功设置和管理自己的排班时间
- **SC-003**: 预约记录可以正确显示各种状态
- **SC-004**: 系统可以正确处理预约冲突（同一时间段不重复预约）
- **SC-005**: 前端页面在企业微信中显示正常（如适用）
- **SC-006**: 所有新增 API 接口通过单元测试

---

## 7. Task Breakdown Principles

### 7.1 Granularity
- 每个任务的开发工作量控制在 1-2 小时（人工）/ 5-10 分钟（AI）
- 任务应该独立且聚焦

### 7.2 Independence
- 尽量减少任务间的依赖
- 数据模型任务优先于 API 和前端任务

### 7.3 Testability
- 每个任务有明确的验收标准
- API 任务需要包含单元测试

### 7.4 Task Categories
任务将分为以下类别：
- **数据分析与设计**：数据模型设计、API 设计
- **代码实现**：后端 API、前端页面、数据库脚本
- **测试**：单元测试、集成测试

### 7.5 Task Count Limitation
- 本功能预计拆分为 **8-10 个任务**
- 如果超出 10 个任务，考虑拆分为多个子功能

---

## 8. Implementation Notes

### 8.1 数据库设计建议
- `appointments` 表需要索引：patient_id, doctor_id, appointment_date, status
- `doctor_schedules` 表需要索引：doctor_id, schedule_date
- 考虑使用数据库事务保证预约操作的原子性

### 8.2 API 设计建议
- 保持与现有 API 风格一致（参见 `.asdm/contexts/api.md`）
- 新增 API 前缀：`/api/appointment/` 和 `/api/schedule/`

### 8.3 前端实现建议
- 新增页面放在 `web/qa-web/src/views/appointment/` 目录
- 新增 API 封装在 `web/qa-web/src/api/appointment.ts`
- 保持与现有页面一致的 UI 风格

### 8.4 后续迭代功能
以下功能不在本次实现范围内，将在后续迭代中完成：
- 预约提醒通知（短信/微信）
- 医生停诊通知
- 预约统计分析
- 支付功能集成

---

## 9. Risks and Mitigations

### Risk 1: 数据库并发问题
- **Description**: 多个患者同时预约同一时间段可能导致超卖
- **Impact**: High
- **Mitigation**: 使用数据库乐观锁或分布式锁保证预约操作的原子性

### Risk 2: 排班数据复杂性
- **Description**: 医生的排班规则可能很复杂（每周重复、临时调整等）
- **Impact**: Medium
- **Mitigation**: 初期简化排班模型，仅支持按日期设置排班，后续迭代支持更复杂的规则

### Risk 3: 与现有系统集成的兼容性
- **Description**: 新增功能可能影响现有功能
- **Impact**: Medium
- **Mitigation**: 充分测试现有功能，确保新增功能不影响已有流程

### Risk 4: 时间 zone 处理
- **Description**: 不同地域的时间可能不同
- **Impact**: Low
- **Mitigation**: 统一使用服务器时区（Asia/Shanghai）

---

## 10. Appendix

### 10.1 References
- 项目 API 文档：`.asdm/contexts/api.md`
- 项目数据模型：`.asdm/contexts/data-models.md`
- 项目架构文档：`.asdm/contexts/architecture.md`
- Spring Boot 官方文档：https://spring.io/projects/spring-boot
- Vue 3 官方文档：https://vuejs.org/

### 10.2 Glossary
- **预约挂号**：患者预约医生线下门诊的服务
- **排班**：医生可用的门诊时间安排
- **时间段**：医生排班的具体时间区间（如 09:00-09:30）
- **预约单号**：系统生成的唯一预约标识
- **PENDING**：待确认状态，医生尚未确认预约
- **CONFIRMED**：已确认状态，预约成功
- **COMPLETED**：已完成状态，患者已就诊
- **CANCELLED**：已取消状态，预约被取消
- **MISSED**：未就诊状态，患者未按时就诊

---

## Task Count Validation

根据功能复杂度和任务分解原则，预计本功能将拆分为 **9 个任务**：

1. 数据模型设计与数据库脚本
2. 后端：Appointment 实体类和 Repository
3. 后端：DoctorSchedule 实体类和 Repository
4. 后端：预约 API 接口实现
5. 后端：排班 API 接口实现
6. 前端：预约列表页面
7. 前端：医生排班页面（医生端）
8. 前端：新增预约页面
9. 测试：单元测试和集成测试

**任务数量 = 9**，满足"10 个或更少任务"的要求 ✅

---

## Status Management

本功能的状态将使用以下值进行跟踪：
- **PLANNED**: 功能已规划但尚未开始（当前状态）
- **IN PROGRESS**: 功能正在实现中
- **COMPLETED**: 功能已成功实现
- **CANCELLED**: 功能已被取消

---

**Document Version**: 1.0  
**Last Updated**: 2026-04-29
