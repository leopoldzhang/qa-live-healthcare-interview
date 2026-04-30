# Task PRD: 数据模型设计与数据库脚本

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-001
**Created Date**: 2026-04-29
**Status**: TODO
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在设计预约挂号功能所需的数据库模型，并创建相应的数据库初始化脚本。

**任务目标**：
- 设计 `appointments`（预约记录）表结构
- 设计 `doctor_schedules`（医生排班）表结构
- 创建数据库迁移脚本
- 确保与现有数据模型的兼容性

**必要性**：
- 预约挂号功能需要新的数据表来存储预约记录和医生排班信息
- 需要定义清晰的数据模型和表关系
- 数据库脚本是后续后端和前端开发的基础

### 1.2 Task Objectives

- **Objective 1**: 设计符合功能需求的数据库表结构
- **Objective 2**: 创建可执行的数据库初始化/迁移脚本
- **Objective 3**: 确保数据模型支持所有功能需求（预约、排班、查询等）
- **Objective 4**: 添加必要的索引以优化查询性能

### 1.3 Related Feature Requirements

- 功能需求：REQ-001（医生排班管理）
- 功能需求：REQ-002（患者预约功能）
- 功能需求：REQ-003（预约记录管理）
- 功能需求：REQ-004（可用时间段查询）
- 功能需求：REQ-005（预约冲突检测）
- 用户故事：Story 1, Story 2, Story 3, Story 4

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 设计 Appointment 表结构
- **ID**: TASK-001-REQ-001
- **Description**: 设计预约记录表，存储患者的预约信息
- **Fields**:
  - `id`: VARCHAR(50), 主键，预约ID
  - `patient_id`: VARCHAR(50), 患者ID（外键，关联 patients 表）
  - `patient_name`: VARCHAR(100), 患者姓名（冗余字段，减少关联查询）
  - `doctor_id`: VARCHAR(50), 医生ID（外键，关联 doctors 表）
  - `doctor_name`: VARCHAR(100), 医生姓名（冗余字段）
  - `appointment_date`: DATE, 预约日期
  - `time_slot`: VARCHAR(20), 时间段（如：09:00-09:30）
  - `location`: VARCHAR(200), 就诊地点
  - `status`: ENUM('PENDING','CONFIRMED','COMPLETED','CANCELLED','MISSED'), 预约状态
  - `description`: TEXT, 症状描述（可选）
  - `appointment_no`: VARCHAR(50), 预约单号（唯一）
  - `create_time`: TIMESTAMP, 创建时间
  - `update_time`: TIMESTAMP, 更新时间

#### Requirement 2: 设计 DoctorSchedule 表结构
- **ID**: TASK-001-REQ-002
- **Description**: 设计医生排班表，存储医生的可用时间段
- **Fields**:
  - `id`: VARCHAR(50), 主键，排班ID
  - `doctor_id`: VARCHAR(50), 医生ID（外键，关联 doctors 表）
  - `doctor_name`: VARCHAR(100), 医生姓名（冗余字段）
  - `schedule_date`: DATE, 排班日期
  - `time_slot`: VARCHAR(20), 时间段（如：09:00-09:30）
  - `location`: VARCHAR(200), 就诊地点
  - `max_appointments`: INT, 最大预约数（默认 1）
  - `current_appointments`: INT, 当前预约数（默认 0）
  - `status`: ENUM('AVAILABLE','UNAVAILABLE'), 状态
  - `create_time`: TIMESTAMP, 创建时间
  - `update_time`: TIMESTAMP, 更新时间

#### Requirement 3: 添加索引
- **ID**: TASK-001-REQ-003
- **Description**: 为常用查询字段添加索引以优化性能
- **Indexes**:
  - `appointments`: INDEX `idx_patient_id` ON appointments(patient_id)
  - `appointments`: INDEX `idx_doctor_id` ON appointments(doctor_id)
  - `appointments`: INDEX `idx_appointment_date` ON appointments(appointment_date)
  - `appointments`: INDEX `idx_status` ON appointments(status)
  - `doctor_schedules`: INDEX `idx_doctor_id` ON doctor_schedules(doctor_id)
  - `doctor_schedules`: INDEX `idx_schedule_date` ON doctor_schedules(schedule_date)
  - `doctor_schedules`: INDEX `idx_doctor_date` ON doctor_schedules(doctor_id, schedule_date)

#### Requirement 4: 创建数据库脚本
- **ID**: TASK-001-REQ-004
- **Description**: 创建可执行的 SQL 脚本用于创建表结构和索引
- **Script Content**:
  - CREATE TABLE 语句 for `appointments`
  - CREATE TABLE 语句 for `doctor_schedules`
  - CREATE INDEX 语句 for all indexes
  - 可选：初始测试数据

### 2.2 Technical Requirements

- **Database**: MySQL 8.0+
- **Character Set**: utf8mb4
- **Collation**: utf8mb4_unicode_ci
- **Storage Engine**: InnoDB（支持事务）
- **Naming Convention**: 蛇形命名法（snake_case）用于表名和字段名

### 2.3 Constraints and Limitations

- **Constraint 1**: 需要保持与现有数据库模式的兼容性
- **Constraint 2**: 脚本应该可重复执行（使用 IF NOT EXISTS）
- **Constraint 3**: 考虑数据迁移需求（如果将来需要修改表结构）
- **Constraint 4**: 时间字段使用合适的类型（DATE for date, VARCHAR for time_slot）

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **分析功能需求**: 仔细分析 Feature PRD 中的所有功能需求和数据模型部分
2. **设计表结构**: 根据需求设计两个主要表的字段、类型和约束
3. **定义关系**: 确定外键关系和索引策略
4. **编写 SQL 脚本**: 创建可执行的数据库脚本
5. **验证脚本**: 在开发环境中执行脚本并验证

### 3.2 Implementation Steps

1. **Step 1**: 分析现有数据库结构（参考 `docker/init-db.sql`）
2. **Step 2**: 设计 `appointments` 表结构（字段、类型、约束）
3. **Step 3**: 设计 `doctor_schedules` 表结构
4. **Step 4**: 确定索引策略
5. **Step 5**: 编写 CREATE TABLE SQL 语句
6. **Step 6**: 编写 CREATE INDEX SQL 语句
7. **Step 7**: 将脚本保存到合适的位置
8. **Step 8**: 验证脚本语法正确性

**Validation Step**: 使用 MySQL 客户端或 phpMyAdmin 验证 SQL 脚本的语法正确性：
  - 示例：`mysql -u root -p < script.sql`（在测试环境中）
  - 或使用 phpMyAdmin 导入功能验证

### 3.3 Technical Considerations

- **考虑 1**: 使用事务确保脚本执行的原子性（如果需要多条语句）
- **考虑 2**: 添加注释说明每个表和字段的用途
- **考虑 3**: 考虑将来可能的扩展（如添加字段）
- **考虑 4**: 确保字段长度足够（如 appointment_no 需要唯一性）

### 3.4 Reference to Project Context

- `.asdm/contexts/data-models.md`: 参考现有的数据模型设计
- `.asdm/contexts/standard-coding-style.md`: 保持命名一致性
- `docker/init-db.sql`: 参考现有的数据库脚本格式

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `appointments` 表成功创建，包含所有必需字段
  - Test method: 在 MySQL 中执行 `DESCRIBE appointments;` 并验证字段
  - **Validation tool**: MySQL DESCRIBE 命令或 phpMyAdmin 界面

- **Criterion 2**: `doctor_schedules` 表成功创建，包含所有必需字段
  - Test method: 在 MySQL 中执行 `DESCRIBE doctor_schedules;` 并验证字段
  - **Validation tool**: MySQL DESCRIBE 命令或 phpMyAdmin 界面

- **Criterion 3**: 所有必需的索引已创建
  - Test method: 在 MySQL 中执行 `SHOW INDEX FROM appointments;` 和 `SHOW INDEX FROM doctor_schedules;`
  - **Validation tool**: MySQL SHOW INDEX 命令

- **Criterion 4**: SQL 脚本可以在 MySQL 8.0+ 环境中成功执行
  - Test method: 在测试数据库中执行脚本，检查是否有错误
  - **Validation tool**: MySQL 客户端（退出码 0 表示成功）

- **Criterion 5**: 脚本使用 IF NOT EXISTS 确保可重复执行
  - Test method: 多次执行脚本，第二次应该没有错误
  - **Validation tool**: MySQL 客户端

### 4.2 Edge Cases

- **Edge case 1**: 如果表已经存在，脚本应该不报错
  - Expected behavior: 使用 IF NOT EXISTS，脚本继续执行

- **Edge case 2**: 字段类型和长度需要满足未来可能的扩展
  - Expected behavior: 使用合适的字段类型和足够的长度

### 4.3 Negative Tests

- **Negative test 1**: 脚本在 MySQL 5.7 或更早版本可能无法执行
  - Expected behavior: 明确文档说明需要 MySQL 8.0+

- **Negative test 2**: 如果外键关联的表不存在，脚本会报错
  - Expected behavior: 确保脚本执行顺序正确，或外键在表创建后添加

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: NONE（这是第一个任务）
- **Blocks**: TASK-002, TASK-003（后端实体类任务依赖于数据模型）

### 5.2 External Dependencies

- **Database**: MySQL 8.0+ 环境
- **Reference**: 现有数据库脚本 `docker/init-db.sql`

### 5.3 Prerequisites

- **Prerequisite 1**: 了解功能需求（已通过 Feature PRD 提供）
- **Prerequisite 2**: 访问 MySQL 测试环境（可选，用于验证脚本）
- **Prerequisite 3**: 了解项目现有的数据库结构

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 1 hour（人工开发工作量）
- **Complexity**: Low
- **Risk**: Low

### 6.2 Effort Factors

- **Factor 1**: 表结构设计相对简单，参考了 Feature PRD 中的数据模型部分
- **Factor 2**: SQL 语法简单，主要是 CREATE TABLE 和 CREATE INDEX
- **Factor 3**: 需要考虑索引策略，但需求明确

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Syntax validation**: 使用 MySQL 客户端验证 SQL 脚本语法
  - Command: `mysql -u [user] -p [database] < script.sql`
  - Success criteria: 退出码 0，无错误信息

- **Table existence validation**: 验证表是否创建成功
  - Command: `SHOW TABLES LIKE 'appointments';` 和 `SHOW TABLES LIKE 'doctor_schedules';`
  - Success criteria: 返回表名

### 7.2 Unit Testing

不适用（这是数据库脚本任务）

### 7.3 Integration Testing

- **Test 1**: 在开发环境的 MySQL 中执行脚本
  - Steps: 连接到开发数据库 → 执行脚本 → 验证表结构
  - Success criteria: 表成功创建，字段正确

- **Test 2**: 验证索引创建
  - Steps: 执行 `SHOW INDEX` 命令
  - Success criteria: 所有索引都已创建

### 7.4 Manual Testing

- **Manual test 1**: 使用 phpMyAdmin 界面查看表结构
  - Steps: 登录 phpMyAdmin → 选择数据库 → 查看表结构
  - Success criteria: 表结构显示正确

---

## 8. Implementation Notes

### 8.1 数据库脚本位置

建议将脚本保存在以下位置之一：
- `docker/init-db.sql`（追加到现有脚本）
- 或创建新文件 `docker/appointment-init-db.sql`

### 8.2 数据模型注意事项

- **冗余字段**: `patient_name` 和 `doctor_name` 是冗余字段，可以减少关联查询，但需要考虑数据一致性
- **时间戳**: `create_time` 和 `update_time` 可以使用数据库触发器或应用层自动填充
- **枚举类型**: 使用 ENUM 可以确保数据有效性，但需要注意数据库移植性

### 8.3 与现有系统的兼容性

- 现有系统有 `patients` 和 `doctors` 表，新表需要添加外键约束
- 需要考虑是否使用物理外键或逻辑外键（应用层保证）

---

## 9. Risks and Mitigations

### Risk 1: 数据库脚本与现有系统不兼容
- **Description**: 新增的表可能与现有系统的表结构或数据冲突
- **Impact**: High
- **Mitigation**: 在开发环境中充分测试，确保不会影响现有功能

### Risk 2: 索引设计不合理影响性能
- **Description**: 缺少必要的索引或索引设计不合理可能导致查询性能问题
- **Impact**: Medium
- **Mitigation**: 根据 Feature PRD 中的查询需求设计索引，后续可以根据实际性能调整

### Risk 3: 字段类型或长度不足
- **Description**: 选择的字段类型或长度可能不满足实际需求
- **Impact**: Medium
- **Mitigation**: 选择足够大的字段长度，使用合适的数据类型

---

## 10. Deliverables

- **Deliverable 1**: `appointments` 表的 CREATE TABLE SQL 语句
- **Deliverable 2**: `doctor_schedules` 表的 CREATE TABLE SQL 语句
- **Deliverable 3**: 所有必需索引的 CREATE INDEX SQL 语句
- **Deliverable 4**: 完整的、可执行的 SQL 脚本文件
- **Deliverable 5**: 数据模型设计文档（可以更新 `.asdm/contexts/data-models.md`）

**Mandatory Deliverable**: Validation Results
- **SQL execution output**: 证据显示脚本成功执行（无错误）
- **Table structure verification**: 证据显示表结构正确（DESCRIBE 输出）
- **Index verification**: 证据显示索引已创建（SHOW INDEX 输出）

---

## Status Management

本任务的状态将使用以下值进行跟踪：
- **TODO**: 任务已规划但未开始（当前状态）
- **IN PROGRESS**: 任务正在执行中
- **DONE**: 任务已成功完成
- **BLOCKED**: 任务被阻塞，等待外部依赖
- **CANCELLED**: 任务已取消

### Status Transitions
- `TODO` → `IN PROGRESS`: 当任务执行开始时
- `IN PROGRESS` → `DONE`: 当任务成功完成时
- `IN PROGRESS` → `BLOCKED`: 当任务遇到阻塞问题时
- `IN PROGRESS` → `TODO`: 当任务需要重新开始时
- `BLOCKED` → `IN PROGRESS`: 当阻塞问题解决时

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
