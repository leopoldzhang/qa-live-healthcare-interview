# Task PRD: 后端：Appointment 实体类和 Repository

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-002
**Created Date**: 2026-04-29
**Status**: DONE
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在创建预约功能的 JPA 实体类和 Repository 接口，为后端 API 实现提供数据访问层。

**任务目标**：
- 创建 `Appointment` JPA 实体类
- 创建 `AppointmentRepository` 接口
- 定义实体类与数据库的映射关系
- 提供基本的 CRUD 操作方法

**必要性**：
- 后端 API 需要操作 `appointments` 表
- JPA 实体类是 Spring Data JPA 的基础
- Repository 提供数据访问抽象，简化数据库操作

### 1.2 Task Objectives

- **Objective 1**: 创建符合 JPA 规范的 `Appointment` 实体类
- **Objective 2**: 创建 `AppointmentRepository` 接口，继承 `JpaRepository`
- **Objective 3**: 定义必要的自定义查询方法
- **Objective 4**: 确保实体类与数据库表结构一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-002（患者预约功能）
- 功能需求：REQ-003（预约记录管理）
- 功能需求：REQ-005（预约冲突检测）
- 用户故事：Story 1, Story 2

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建 Appointment 实体类
- **ID**: TASK-002-REQ-001
- **Description**: 创建 `Appointment.java` 实体类，映射到 `appointments` 表
- **Fields**:
  - `id`: String, 主键，使用 `@Id` 和 `@GeneratedValue`
  - `patientId`: String, 患者ID
  - `patientName`: String, 患者姓名
  - `doctorId`: String, 医生ID
  - `doctorName`: String, 医生姓名
  - `appointmentDate`: LocalDate, 预约日期
  - `timeSlot`: String, 时间段
  - `location`: String, 就诊地点
  - `status`: String 或 Enum, 预约状态
  - `description`: String, 症状描述
  - `appointmentNo`: String, 预约单号（唯一）
  - `createTime`: LocalDateTime, 创建时间
  - `updateTime`: LocalDateTime, 更新时间
- **Annotations**:
  - `@Entity`, `@Table(name = "appointments")`
  - `@Id`, `@GeneratedValue(strategy = GenerationType.UUID)` 或类似策略
  - `@Column` 用于字段映射

#### Requirement 2: 创建 AppointmentRepository 接口
- **ID**: TASK-002-REQ-002
- **Description**: 创建 `AppointmentRepository.java` 接口
- ** extends**: `JpaRepository<Appointment, String>`
- **Custom Query Methods**:
  - `List<Appointment> findByPatientIdOrderByAppointmentDateDesc(String patientId);`
  - `List<Appointment> findByDoctorIdAndAppointmentDateOrderByTimeSlot(String doctorId, LocalDate appointmentDate);`
  - `Optional<Appointment> findByAppointmentNo(String appointmentNo);`
  - `List<Appointment> findByStatus(String status);`
  - `Long countByDoctorIdAndAppointmentDateAndTimeSlotAndStatusNot(String doctorId, LocalDate appointmentDate, String timeSlot, String status);`

#### Requirement 3: 定义实体类生命周期回调
- **ID**: TASK-002-REQ-003
- **Description**: 使用 `@PrePersist` 和 `@PreUpdate` 自动填充时间戳
- **Implementation**:
  - `createTime` 在创建时自动设置为当前时间
  - `updateTime` 在创建和更新时自动设置为当前时间
  - `appointmentNo` 在创建时自动生成（可以使用 UUID 或特定格式）

### 2.2 Technical Requirements

- **Framework**: Spring Data JPA
- **Package**: `com.leansofx.qaserviceuser.entity` (或类似)
- **Dependencies**: `spring-boot-starter-data-jpa`
- **Lombok**: 使用 `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` 简化代码

### 2.3 Constraints and Limitations

- **Constraint 1**: 实体类需要与 TASK-001 中设计的数据库表结构一致
- **Constraint 2**: 字段类型和名称需要与数据库表匹配
- **Constraint 3**: 需要考虑序列化（如果返回 JSON）
- **Constraint 4**: 可能需要忽略某些字段的 JSON 序列化（如 `createTime`, `updateTime`）

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **分析数据库表结构**: 参考 TASK-001 中设计的 `appointments` 表
2. **创建实体类**: 使用 JPA 注解映射表和字段
3. **创建 Repository**: 继承 `JpaRepository`，添加自定义查询方法
4. **测试编译**: 确保代码编译通过
5. **验证映射**: 通过单元测试或启动应用验证映射正确性

### 3.2 Implementation Steps

1. **Step 1**: 创建 `Appointment.java` 实体类
   - 添加类级别注解：`@Entity`, `@Table`
   - 添加字段和字段级别注解：`@Id`, `@Column`, etc.
   - 添加生命周期回调方法：`@PrePersist`, `@PreUpdate`
   - 使用 Lombok 注解简化代码：`@Data`

2. **Step 2**: 创建 `AppointmentRepository.java` 接口
   - 继承 `JpaRepository<Appointment, String>`
   - 定义自定义查询方法（使用 Spring Data JPA 方法命名约定）

3. **Step 3**: 验证代码编译
   - Command: `cd server/qa-service-user && ./mvnw clean compile`
   - Success criteria: 编译成功，无错误

4. **Step 4**: 可选 - 创建简单的单元测试验证映射
   - 使用 `@DataJpaTest` 测试实体类映射

**Validation Step**: 使用 Maven 编译验证代码正确性：
  - Command: `cd server/qa-service-user && ./mvnw clean compile`
  - Expected: 编译成功，退出码 0

### 3.3 Technical Considerations

- **考虑 1**: 使用 Lombok 减少样板代码
- **考虑 2**: 考虑使用 `@JsonIgnore` 或 `@JsonFormat` 控制 JSON 序列化
- **考虑 3**: `appointmentNo` 的生成策略（UUID 或自定义格式）
- **考虑 4**: 状态码可以使用枚举类型，但需要自定义序列化/反序列化

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-coding-style.md`: Java 编码规范
- `.asdm/contexts/data-models.md`: 数据模型参考
- 现有的实体类：参考 `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/` 中的类

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `Appointment.java` 实体类成功创建，包含所有必需字段
  - Test method: 检查文件是否存在，字段是否完整
  - **Validation tool**: `find . -name "Appointment.java"` 和代码审查

- **Criterion 2**: `AppointmentRepository.java` 接口成功创建，包含自定义查询方法
  - Test method: 检查文件是否存在，方法签名是否正确
  - **Validation tool**: `find . -name "AppointmentRepository.java"` 和代码审查

- **Criterion 3**: 代码编译通过，无错误
  - Test method: 运行 Maven 编译命令
  - **Validation tool**: `cd server/qa-service-user && ./mvnw clean compile`，退出码 0

- **Criterion 4**: 实体类与数据库表结构一致
  - Test method: 比对实体类字段和数据库表字段
  - **Validation tool**: 代码审查和数据库表结构文档比对

### 4.2 Edge Cases

- **Edge case 1**: 字段为 null 的情况
  - Expected behavior: 使用 `@Column(nullable = false)` 或类似约束

- **Edge case 2**: 日期/时间格式序列化
  - Expected behavior: 使用 `@JsonFormat` 注解指定格式

### 4.3 Negative Tests

- **Negative test 1**: 编译包含语法错误的代码
  - Expected behavior: 编译失败，显示错误信息

- **Negative test 2**: 实体类字段与数据库表字段不匹配
  - Expected behavior: 应用启动时抛出异常（如 `Caused by: org.hibernate.AnnotationException`）

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-001（数据模型设计与数据库脚本）→ 需要数据库表结构定义
- **Blocks**: TASK-004（后端：预约 API 接口实现）→ API 实现需要实体类和 Repository

### 5.2 External Dependencies

- **Dependency 1**: Spring Data JPA（已在 `pom.xml` 中）
- **Dependency 2**: Lombok（已在 `pom.xml` 中）
- **Dependency 3**: 数据库表结构（由 TASK-001 创建）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-001 完成，明确数据库表结构
- **Prerequisite 2**: `qa-service-user` 项目可以成功编译
- **Prerequisite 3**: 了解 Spring Data JPA 和基本注解

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 1 hour（人工开发工作量）
- **Complexity**: Low
- **Risk**: Low

### 6.2 Effort Factors

- **Factor 1**: JPA 实体类创建是常规任务，有标准模式
- **Factor 2**: 可以参考项目中已有的实体类（如 `Patient.java`, `Doctor.java`）
- **Factor 3**: Spring Data JPA 的 Repository 接口非常简单

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Compilation check**:
  - Command: `cd server/qa-service-user && ./mvnw clean compile`
  - Success criteria: 编译成功，退出码 0

- **Code style check** (可选):
  - Command: 使用 Checkstyle 或类似工具
  - Success criteria: 无代码风格错误

### 7.2 Unit Testing

- **Test 1**: 测试实体类字段映射
  - Approach: 使用 `@DataJpaTest` 测试数据层
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=AppointmentRepositoryTest`
  - Success criteria: 测试通过

- **Test 2**: 测试 Repository 查询方法
  - Approach: 使用 H2 内存数据库测试 Repository 方法
  - Command: 同上
  - Success criteria: 查询方法返回预期结果

### 7.3 Integration Testing

- **Test 1**: 应用启动测试
  - Approach: 启动 Spring Boot 应用，验证实体类加载正常
  - Command: `cd server/qa-service-user && ./mvnw spring-boot:run`（短暂运行后停止）
  - Success criteria: 应用成功启动，无实体类映射错误

### 7.4 Manual Testing

不适用（这是后端代码任务，主要通过自动化测试验证）

---

## 8. Implementation Notes

### 8.1 代码示例

**Appointment.java**:
```java
@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "patient_name")
    private String patientName;

    // ... 其他字段

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (appointmentNo == null) {
            appointmentNo = "APT" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
```

**AppointmentRepository.java**:
```java
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(String patientId);
    List<Appointment> findByDoctorIdAndAppointmentDateOrderByTimeSlot(String doctorId, LocalDate appointmentDate);
    Optional<Appointment> findByAppointmentNo(String appointmentNo);
}
```

### 8.2 注意事项

- **关于主键生成**: 可以使用 UUID 或数据库自增，根据项目约定选择
- **关于日期类型**: `appointmentDate` 使用 `LocalDate`, `createTime` 和 `updateTime` 使用 `LocalDateTime`
- **关于状态码**: 可以考虑使用枚举类型，但需要自定义序列化

### 8.3 与现有代码的一致性

- 参考项目中已有的实体类（如 `Patient.java`）的代码风格和注解使用
- 保持包结构一致

---

## 9. Risks and Mitigations

### Risk 1: 实体类与数据库表结构不匹配
- **Description**: 如果 TASK-001 的数据库表结构发生变化，实体类可能不匹配
- **Impact**: High
- **Mitigation**: 在 TASK-001 完成后立即执行此任务，保持沟通

### Risk 2: 字段类型不匹配导致序列化/反序列化问题
- **Description**: Java 类型和 JSON 类型可能不匹配
- **Impact**: Medium
- **Mitigation**: 使用 `@JsonFormat` 注解明确指定格式

### Risk 3: Repository 查询方法命名错误
- **Description**: Spring Data JPA 查询方法命名错误会导致应用启动失败
- **Impact**: Medium
- **Mitigation**: 仔细遵循 Spring Data JPA 方法命名约定，编译时检查

---

## 10. Deliverables

- **Deliverable 1**: `Appointment.java` 实体类文件
- **Deliverable 2**: `AppointmentRepository.java` 接口文件
- **Deliverable 3**: 单元测试（可选，但推荐）

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（Maven 编译日志，退出码 0）
- **Code review**: 证据显示实体类字段完整、注解正确
- **Repository methods**: 证据显示必要的查询方法已定义

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
