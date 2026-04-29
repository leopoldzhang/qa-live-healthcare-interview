# Task PRD: 后端：DoctorSchedule 实体类和 Repository

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-003
**Created Date**: 2026-04-29
**Status**: TODO
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在创建医生排班功能的 JPA 实体类和 Repository 接口，为后端排班 API 实现提供数据访问层。

**任务目标**：
- 创建 `DoctorSchedule` JPA 实体类
- 创建 `DoctorScheduleRepository` 接口
- 定义实体类与数据库的映射关系
- 提供排班数据的基本 CRUD 操作

**必要性**：
- 后端排班 API 需要操作 `doctor_schedules` 表
- JPA 实体类是 Spring Data JPA 的基础
- Repository 提供数据访问抽象，简化数据库操作

### 1.2 Task Objectives

- **Objective 1**: 创建符合 JPA 规范的 `DoctorSchedule` 实体类
- **Objective 2**: 创建 `DoctorScheduleRepository` 接口，继承 `JpaRepository`
- **Objective 3**: 定义必要的自定义查询方法（按医生ID、日期查询等）
- **Objective 4**: 确保实体类与数据库表结构一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-001（医生排班管理）
- 功能需求：REQ-004（可用时间段查询）
- 用户故事：Story 3, Story 4

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建 DoctorSchedule 实体类
- **ID**: TASK-003-REQ-001
- **Description**: 创建 `DoctorSchedule.java` 实体类，映射到 `doctor_schedules` 表
- **Fields**:
  - `id`: String, 主键
  - `doctorId`: String, 医生ID
  - `doctorName`: String, 医生姓名
  - `scheduleDate`: LocalDate, 排班日期
  - `timeSlot`: String, 时间段
  - `location`: String, 就诊地点
  - `maxAppointments`: Integer, 最大预约数
  - `currentAppointments`: Integer, 当前预约数
  - `status`: String 或 Enum, 状态（AVAILABLE/UNAVAILABLE）
  - `createTime`: LocalDateTime, 创建时间
  - `updateTime`: LocalDateTime, 更新时间
- **Annotations**:
  - `@Entity`, `@Table(name = "doctor_schedules")`
  - `@Id`, `@GeneratedValue`
  - `@Column` 用于字段映射

#### Requirement 2: 创建 DoctorScheduleRepository 接口
- **ID**: TASK-003-REQ-002
- **Description**: 创建 `DoctorScheduleRepository.java` 接口
- **Extends**: `JpaRepository<DoctorSchedule, String>`
- **Custom Query Methods**:
  - `List<DoctorSchedule> findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(String doctorId, LocalDate startDate, LocalDate endDate);`
  - `List<DoctorSchedule> findByDoctorIdAndScheduleDateAndStatus(String doctorId, LocalDate scheduleDate, String status);`
  - `Optional<DoctorSchedule> findByDoctorIdAndScheduleDateAndTimeSlot(String doctorId, LocalDate scheduleDate, String timeSlot);`
  - `List<DoctorSchedule> findByScheduleDateBetweenAndStatusOrderByScheduleDateAscTimeSlotAsc(LocalDate startDate, LocalDate endDate, String status);`

#### Requirement 3: 定义实体类生命周期回调
- **ID**: TASK-003-REQ-003
- **Description**: 使用 `@PrePersist` 和 `@PreUpdate` 自动填充时间戳
- **Implementation**:
  - `createTime` 在创建时自动设置为当前时间
  - `updateTime` 在创建和更新时自动设置为当前时间
  - `currentAppointments` 默认值为 0

### 2.2 Technical Requirements

- **Framework**: Spring Data JPA
- **Package**: `com.leansofx.qaserviceuser.entity` (或类似)
- **Dependencies**: `spring-boot-starter-data-jpa`
- **Lombok**: 使用 `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` 简化代码

### 2.3 Constraints and Limitations

- **Constraint 1**: 实体类需要与 TASK-001 中设计的数据库表结构一致
- **Constraint 2**: 字段类型和名称需要与数据库表匹配
- **Constraint 3**: 需要考虑序列化（如果返回 JSON）
- **Constraint 4**: `currentAppointments` 需要在业务逻辑中维护

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **分析数据库表结构**: 参考 TASK-001 中设计的 `doctor_schedules` 表
2. **创建实体类**: 使用 JPA 注解映射表和字段
3. **创建 Repository**: 继承 `JpaRepository`，添加自定义查询方法
4. **测试编译**: 确保代码编译通过
5. **验证映射**: 通过单元测试或启动应用验证映射正确性

### 3.2 Implementation Steps

1. **Step 1**: 创建 `DoctorSchedule.java` 实体类
   - 添加类级别注解：`@Entity`, `@Table`
   - 添加字段和字段级别注解：`@Id`, `@Column`, etc.
   - 添加生命周期回调方法：`@PrePersist`, `@PreUpdate`
   - 使用 Lombok 注解简化代码：`@Data`

2. **Step 2**: 创建 `DoctorScheduleRepository.java` 接口
   - 继承 `JpaRepository<DoctorSchedule, String>`
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
- **考虑 3**: `status` 字段可以使用枚举类型，但需要自定义序列化/反序列化
- **考虑 4**: 查询方法需要支持日期范围查询

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-coding-style.md`: Java 编码规范
- `.asdm/contexts/data-models.md`: 数据模型参考
- 现有的实体类：参考 `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/` 中的类

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `DoctorSchedule.java` 实体类成功创建，包含所有必需字段
  - Test method: 检查文件是否存在，字段是否完整
  - **Validation tool**: `find . -name "DoctorSchedule.java"` 和代码审查

- **Criterion 2**: `DoctorScheduleRepository.java` 接口成功创建，包含自定义查询方法
  - Test method: 检查文件是否存在，方法签名是否正确
  - **Validation tool**: `find . -name "DoctorScheduleRepository.java"` 和代码审查

- **Criterion 3**: 代码编译通过，无错误
  - Test method: 运行 Maven 编译命令
  - **Validation tool**: `cd server/qa-service-user && ./mvnw clean compile`，退出码 0

- **Criterion 4**: 实体类与数据库表结构一致
  - Test method: 比对实体类字段和数据库表字段
  - **Validation tool**: 代码审查和数据库表结构文档比对

### 4.2 Edge Cases

- **Edge case 1**: `currentAppointments` 字段的维护
  - Expected behavior: 在业务逻辑中更新，不使用数据库触发器

- **Edge case 2**: 日期范围查询的边界条件
  - Expected behavior: 使用 `BETWEEN` 或 `>=` 和 `<=` 正确处理边界

### 4.3 Negative Tests

- **Negative test 1**: 编译包含语法错误的代码
  - Expected behavior: 编译失败，显示错误信息

- **Negative test 2**: 实体类字段与数据库表字段不匹配
  - Expected behavior: 应用启动时抛出异常

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-001（数据模型设计与数据库脚本）→ 需要数据库表结构定义
- **Blocks**: TASK-005（后端：排班 API 接口实现）→ API 实现需要实体类和 Repository

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
- **Factor 4**: 排班查询方法稍复杂，需要支持日期范围

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Compilation check**:
  - Command: `cd server/qa-service-user && ./mvnw clean compile`
  - Success criteria: 编译成功，退出码 0

### 7.2 Unit Testing

- **Test 1**: 测试实体类字段映射
  - Approach: 使用 `@DataJpaTest` 测试数据层
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=DoctorScheduleRepositoryTest`
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

---

## 8. Implementation Notes

### 8.1 代码示例

**DoctorSchedule.java**:
```java
@Entity
@Table(name = "doctor_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @Column(name = "location")
    private String location;

    @Column(name = "max_appointments")
    private Integer maxAppointments = 1;

    @Column(name = "current_appointments")
    private Integer currentAppointments = 0;

    @Column(name = "status")
    private String status = "AVAILABLE";

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (currentAppointments == null) {
            currentAppointments = 0;
        }
        if (maxAppointments == null) {
            maxAppointments = 1;
        }
        if (status == null) {
            status = "AVAILABLE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
```

**DoctorScheduleRepository.java**:
```java
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, String> {
    List<DoctorSchedule> findByDoctorIdAndScheduleDateBetweenOrderByScheduleDateAscTimeSlotAsc(
        String doctorId, LocalDate startDate, LocalDate endDate);

    List<DoctorSchedule> findByDoctorIdAndScheduleDateAndStatus(
        String doctorId, LocalDate scheduleDate, String status);

    Optional<DoctorSchedule> findByDoctorIdAndScheduleDateAndTimeSlot(
        String doctorId, LocalDate scheduleDate, String timeSlot);

    List<DoctorSchedule> findByScheduleDateBetweenAndStatusOrderByScheduleDateAscTimeSlotAsc(
        LocalDate startDate, LocalDate endDate, String status);
}
```

### 8.2 注意事项

- **关于日期查询**: Spring Data JPA 支持 `Between` 关键字，用于日期范围查询
- **关于排序**: 使用 `OrderBy` 关键字指定排序字段和方向
- **关于可选字段**: `location` 等字段可能为空，需要正确处理

---

## 9. Risks and Mitigations

### Risk 1: 实体类与数据库表结构不匹配
- **Description**: 如果 TASK-001 的数据库表结构发生变化，实体类可能不匹配
- **Impact**: High
- **Mitigation**: 在 TASK-001 完成后立即执行此任务，保持沟通

### Risk 2: Repository 查询方法命名错误
- **Description**: Spring Data JPA 查询方法命名错误会导致应用启动失败
- **Impact**: Medium
- **Mitigation**: 仔细遵循 Spring Data JPA 方法命名约定，编译时检查

---

## 10. Deliverables

- **Deliverable 1**: `DoctorSchedule.java` 实体类文件
- **Deliverable 2**: `DoctorScheduleRepository.java` 接口文件
- **Deliverable 3**: 单元测试（可选，但推荐）

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（Maven 编译日志，退出码 0）
- **Code review**: 证据显示实体类字段完整、注解正确
- **Repository methods**: 证据显示必要的查询方法已定义

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
