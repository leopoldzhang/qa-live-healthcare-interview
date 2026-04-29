# Task PRD: 测试：单元测试和集成测试

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-009
**Created Date**: 2026-04-29
**Status**: TODO
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在为预约挂号功能创建全面的单元测试和集成测试，确保代码质量和功能正确性。

**任务目标**：
- 为后端 Service 类创建单元测试
- 为后端 Controller 创建集成测试
- 为前端组件创建单元测试（可选）
- 确保所有测试通过

**必要性**：
- 测试是保证代码质量的重要手段
- 自动化测试可以在代码变更时快速发现问题
- 测试是持续集成/持续部署（CI/CD）的基础

### 1.2 Task Objectives

- **Objective 1**: 为 `AppointmentService` 和 `ScheduleService` 创建单元测试
- **Objective 2**: 为 `AppointmentController` 和 `ScheduleController` 创建集成测试
- **Objective 3**: 为前端组件创建单元测试（可选）
- **Objective 4**: 确保所有测试通过，代码覆盖率达到要求

### 1.3 Related Feature Requirements

- 功能需求：所有功能需求
- 用户故事：所有用户故事

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 后端 Service 层单元测试
- **ID**: TASK-009-REQ-001
- **Description**: 为 `AppointmentService` 和 `ScheduleService` 创建单元测试
- **Testing Framework**: JUnit 5, Mockito
- **Test Cases**:
  - 测试创建预约成功场景
  - 测试创建预约失败场景（冲突、排班不存在等）
  - 测试取消预约成功场景
  - 测试取消预约失败场景（状态不正确等）
  - 测试查询预约记录
  - 测试创建排班
  - 测试查询可用时间段
  - 测试更新和删除排班

#### Requirement 2: 后端 Controller 层集成测试
- **ID**: TASK-009-REQ-002
- **Description**: 为 `AppointmentController` 和 `ScheduleController` 创建集成测试
- **Testing Framework**: Spring Boot Test, MockMvc
- **Test Cases**:
  - 测试所有 API 端点
  - 测试请求验证（缺少必需字段、字段格式错误等）
  - 测试响应格式和状态码
  - 测试错误处理

#### Requirement 3: 前端组件测试（可选）
- **ID**: TASK-009-REQ-003
- **Description**: 为前端 Vue 组件创建单元测试
- **Testing Framework**: Vue Test Utils, Jest (如果已配置)
- **Test Cases**:
  - 测试组件渲染
  - 测试用户交互（点击、输入等）
  - 测试 API 调用

### 2.2 Technical Requirements

- **Backend Testing Framework**: JUnit 5, Mockito, Spring Boot Test
- **Frontend Testing Framework**: Vue Test Utils, Jest (如果已配置)
- **Test Coverage**: 至少 80% 的代码覆盖率（后端）

### 2.3 Constraints and Limitations

- **Constraint 1**: 测试应该独立于外部系统（使用 Mock）
- **Constraint 2**: 测试应该可以快速运行（单元测试不应该访问数据库）
- **Constraint 3**: 集成测试应该使用内存数据库（如 H2）
- **Constraint 4**: 测试应该可以重复执行（不依赖执行顺序）

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **创建后端 Service 测试**: 使用 Mockito 模拟依赖
2. **创建后端 Controller 测试**: 使用 MockMvc 测试 API 端点
3. **创建前端组件测试**（可选）: 使用 Vue Test Utils
4. **运行测试**: 确保所有测试通过
5. **检查覆盖率**: 确保达到要求的代码覆盖率

### 3.2 Implementation Steps

1. **Step 1**: 创建 `AppointmentServiceTest.java`
   - 使用 `@ExtendWith(MockitoExtension.class)`
   - 使用 `@Mock` 模拟 `AppointmentRepository` 和 `DoctorScheduleRepository`
   - 使用 `@InjectMocks` 注入 `AppointmentService`
   - 编写测试方法，使用 `when(...).thenReturn(...)` 设置 Mock 行为
   - 使用 `assertEquals`, `assertThrows` 等断言

2. **Step 2**: 创建 `ScheduleServiceTest.java`
   - 同上，测试 `ScheduleService` 的方法

3. **Step 3**: 创建 `AppointmentControllerTest.java`
   - 使用 `@WebMvcTest(AppointmentController.class)`
   - 使用 `@MockBean` 模拟 `AppointmentService`
   - 使用 `MockMvc` 发送 HTTP 请求并验证响应
   - 测试所有端点，包括成功和失败场景

4. **Step 4**: 创建 `ScheduleControllerTest.java`
   - 同上，测试 `ScheduleController` 的端点

5. **Step 5**: 运行所有测试
   - Command: `cd server/qa-service-user && ./mvnw test`
   - Success criteria: 所有测试通过，退出码 0

**Validation Step**: 使用 Maven 运行测试验证：
  - Command: `cd server/qa-service-user && ./mvnw test`
  - Expected: 所有测试通过，退出码 0

### 3.3 Technical Considerations

- **考虑 1**: 使用 Mockito 的 `@Mock` 和 `@InjectMocks` 简化测试
- **考虑 2**: 使用 `@ExtendWith(MockitoExtension.class)` 启用 Mockito 支持
- **考虑 3**: 使用 `assertThrows` 测试异常场景
- **考虑 4**: 使用 `@DisplayName` 为测试方法提供可读的名称

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-coding-style.md`: 测试代码规范
- 现有的测试类：参考项目中已有的测试类（如果有）

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `AppointmentServiceTest.java` 成功创建，包含所有必要的测试用例
  - Test method: 检查文件是否存在，测试方法是否完整
  - **Validation tool**: `find . -name "AppointmentServiceTest.java"` 和代码审查

- **Criterion 2**: `ScheduleServiceTest.java` 成功创建，包含所有必要的测试用例
  - Test method: 检查文件是否存在，测试方法是否完整
  - **Validation tool**: `find . -name "ScheduleServiceTest.java"` 和代码审查

- **Criterion 3**: `AppointmentControllerTest.java` 成功创建，包含所有必要的测试用例
  - Test method: 检查文件是否存在，测试方法是否完整
  - **Validation tool**: `find . -name "AppointmentControllerTest.java"` 和代码审查

- **Criterion 4**: `ScheduleControllerTest.java` 成功创建，包含所有必要的测试用例
  - Test method: 检查文件是否存在，测试方法是否完整
  - **Validation tool**: `find . -name "ScheduleControllerTest.java"` 和代码审查

- **Criterion 5**: 所有测试通过
  - Test method: 运行 Maven 测试命令
  - **Validation tool**: `cd server/qa-service-user && ./mvnw test`，退出码 0

### 4.2 Edge Cases

- **Edge case 1**: 测试失败时，错误信息清晰
  - Expected behavior: 测试框架提供详细的失败信息

- **Edge case 2**: Mock 对象的行为未设置时
  - Expected behavior: 返回默认值（如 null, empty Optional）

### 4.3 Negative Tests

- **Negative test 1**: 测试代码有编译错误
  - Expected behavior: 编译失败，显示错误信息

- **Negative test 2**: 测试断言失败
  - Expected behavior: 测试失败，显示断言错误信息

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-004（后端：预约 API 接口实现）→ 需要 Service 和 Controller 实现
- **Depends on**: TASK-005（后端：排班 API 接口实现）→ 需要 Service 和 Controller 实现
- **Depends on**: TASK-006（前端：预约列表页面）→ 可选，前端测试
- **Depends on**: TASK-007（前端：医生排班页面）→ 可选，前端测试
- **Depends on**: TASK-008（前端：新增预约页面）→ 可选，前端测试

### 5.2 External Dependencies

- **Dependency 1**: JUnit 5（已在 pom.xml 中）
- **Dependency 2**: Mockito（已在 pom.xml 中）
- **Dependency 3**: Spring Boot Test（已在 pom.xml 中）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-004 和 TASK-005 完成，后端代码可用
- **Prerequisite 2**: 了解 JUnit 5 和 Mockito
- **Prerequisite 3**: 了解如何编写单元测试和集成测试

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 3 hours（人工开发工作量）
- **Complexity**: Medium
- **Risk**: Low

### 6.2 Effort Factors

- **Factor 1**: 编写测试需要时间，但可以提高代码质量
- **Factor 2**: 需要仔细设计测试用例，覆盖各种场景
- **Factor 3**: Mockito 和 Spring Boot Test 有学习曲线

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Test execution**:
  - Command: `cd server/qa-service-user && ./mvnw test`
  - Success criteria: 所有测试通过，退出码 0

- **Code coverage check** (可选):
  - Command: `cd server/qa-service-user && ./mvnw jacoco:report`
  - Success criteria: 代码覆盖率达到要求（如 80%）

### 7.2 Unit Testing

- **Test 1**: 测试 `AppointmentService` 的 `createAppointment` 方法
  - Approach: 使用 Mockito 模拟 Repository，测试成功和失败场景
  - Success criteria: 测试通过

- **Test 2**: 测试 `AppointmentService` 的 `cancelAppointment` 方法
  - Approach: 同上
  - Success criteria: 测试通过

- **Test 3**: 测试 `ScheduleService` 的所有方法
  - Approach: 同上
  - Success criteria: 测试通过

### 7.3 Integration Testing

- **Test 1**: 测试 `AppointmentController` 的所有端点
  - Approach: 使用 MockMvc 发送 HTTP 请求，验证响应
  - Success criteria: 测试通过

- **Test 2**: 测试 `ScheduleController` 的所有端点
  - Approach: 同上
  - Success criteria: 测试通过

### 7.4 Manual Testing

不适用（这是测试任务，主要通过自动化测试验证）

---

## 8. Implementation Notes

### 8.1 代码示例

**AppointmentServiceTest.java** (partial):
```java
@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    @DisplayName("创建预约成功")
    void createAppointmentSuccess() {
        // 准备测试数据
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId("patient1");
        request.setDoctorId("doctor1");
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setTimeSlot("09:00-09:30");

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId("schedule1");
        schedule.setMaxAppointments(1);
        schedule.setCurrentAppointments(0);

        // 设置 Mock 行为
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.of(schedule));
        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        AppointmentResponse response = appointmentService.createAppointment(request);

        // 验证结果
        assertNotNull(response);
        assertEquals("patient1", response.getPatientId());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("创建预约失败 - 排班不存在")
    void createAppointmentFailureScheduleNotFound() {
        // 准备测试数据
        AppointmentRequest request = new AppointmentRequest();
        request.setDoctorId("doctor1");
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setTimeSlot("09:00-09:30");

        // 设置 Mock 行为
        when(doctorScheduleRepository.findByDoctorIdAndScheduleDateAndTimeSlot(
                anyString(), any(LocalDate.class), anyString()))
                .thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.createAppointment(request);
        });
    }
}
```

**AppointmentControllerTest.java** (partial):
```java
@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    @DisplayName("POST /api/appointment - 创建预约成功")
    void createAppointmentSuccess() throws Exception {
        // 准备测试数据
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId("patient1");
        request.setDoctorId("doctor1");
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setTimeSlot("09:00-09:30");

        AppointmentResponse response = new AppointmentResponse();
        response.setAppointmentNo("APT123");

        // 设置 Mock 行为
        when(appointmentService.createAppointment(any(AppointmentRequest.class)))
                .thenReturn(response);

        // 执行测试
        mockMvc.perform(post("/api/appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentNo").value("APT123"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 8.2 注意事项

- **关于测试命名**: 使用 `@DisplayName` 提供可读的测试名称
- **关于断言**: 使用 JUnit 5 的 `assertXXX` 方法
- **关于 Mock**: 使用 Mockito 的 `when(...).thenReturn(...)` 设置 Mock 行为
- **关于验证**: 使用 Mockito 的 `verify(...)` 验证 Mock 对象的方法是否被调用

---

## 9. Risks and Mitigations

### Risk 1: 测试代码难以维护
- **Description**: 如果测试代码写得好，但难以维护，会在代码变更时带来问题
- **Impact**: Medium
- **Mitigation**: 编写清晰、简洁的测试代码，使用辅助方法减少重复

### Risk 2: 测试覆盖率不足
- **Description**: 如果测试覆盖率不足，可能会遗漏一些 bug
- **Impact**: Medium
- **Mitigation**: 使用代码覆盖率工具（如 JaCoCo）检查测试覆盖率，并补充测试用例

---

## 10. Deliverables

- **Deliverable 1**: `AppointmentServiceTest.java` 测试类
- **Deliverable 2**: `ScheduleServiceTest.java` 测试类
- **Deliverable 3**: `AppointmentControllerTest.java` 测试类
- **Deliverable 4**: `ScheduleControllerTest.java` 测试类
- **Deliverable 5**: 前端组件测试（可选）

**Mandatory Deliverable**: Validation Results
- **Test execution output**: 证据显示所有测试通过（Maven 测试日志，退出码 0）
- **Code coverage report** (可选): 证据显示代码覆盖率达到要求

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
