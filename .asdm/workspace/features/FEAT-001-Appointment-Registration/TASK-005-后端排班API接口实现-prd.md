# Task PRD: 后端：排班 API 接口实现

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-005
**Created Date**: 2026-04-29
**Status**: DONE
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在实现医生排班管理的后端 RESTful API 接口，包括创建排班、查询排班、更新排班等功能。

**任务目标**：
- 创建 `ScheduleController` 控制器类
- 实现排班相关的 RESTful API 端点
- 实现排班业务逻辑（创建、更新、查询等）
- 添加必要的输入验证和错误处理

**必要性**：
- 医生需要设置自己的排班时间
- 前端需要调用后端 API 获取可用时间段
- 排班数据是预约功能的基础

### 1.2 Task Objectives

- **Objective 1**: 创建 `ScheduleController` 控制器，提供 RESTful API
- **Objective 2**: 实现排班创建、查询、更新、删除等核心功能
- **Objective 3**: 实现可用时间段查询功能（供患者预约时使用）
- **Objective 4**: 添加输入验证和错误处理
- **Objective 5**: 确保 API 风格与现有系统一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-001（医生排班管理）
- 功能需求：REQ-004（可用时间段查询）
- 用户故事：Story 3, Story 4

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建 ScheduleController
- **ID**: TASK-005-REQ-001
- **Description**: 创建控制器类，定义排班相关的 API 端点
- **Base URL**: `/api/schedule`
- **Endpoints**:
  - `POST /api/schedule` - 创建排班
  - `GET /api/schedule/{scheduleId}` - 获取排班详情
  - `GET /api/schedule/doctor/{doctorId}` - 获取医生的排班列表
  - `PUT /api/schedule/{scheduleId}` - 更新排班
  - `DELETE /api/schedule/{scheduleId}` - 删除排班
  - `GET /api/schedule/available` - 查询可用时间段（患者预约时使用）

#### Requirement 2: 实现创建排班功能
- **ID**: TASK-005-REQ-002
- **Description**: 实现 POST `/api/schedule` 端点
- **Request Body**:
  ```json
  {
    "doctorId": "string",
    "scheduleDate": "2026-05-01",
    "timeSlot": "09:00-09:30",
    "location": "string (optional)",
    "maxAppointments": 1
  }
  ```
- **Business Logic**:
  1. 验证输入参数
  2. 检查是否已存在相同医生和时间的排班
  3. 创建排班记录（状态：AVAILABLE）
  4. 返回排班详情

#### Requirement 3: 实现查询可用时间段功能
- **ID**: TASK-005-REQ-003
- **Description**: 实现 GET `/api/schedule/available` 端点
- **Query Parameters**:
  - `doctorId`: 医生ID（必需）
  - `startDate`: 开始日期（可选，默认今天）
  - `endDate`: 结束日期（可选，默认7天后）
- **Response**: 可用时间段列表，包含剩余预约名额
- **Business Logic**:
  1. 根据医生ID和日期范围查询排班
  2. 过滤状态为 AVAILABLE 的排班
  3. 计算每个时间段的剩余名额（`maxAppointments - currentAppointments`）
  4. 只返回还有剩余名额的排班

#### Requirement 4: 实现更新排班功能
- **ID**: TASK-005-REQ-004
- **Description**: 实现 PUT `/api/schedule/{scheduleId}` 端点
- **Business Logic**:
  1. 根据 scheduleId 查询排班记录
  2. 更新排班信息（时间、地点、最大预约数等）
  3. 如果已有关联的预约，需要考虑是否允许更新
  4. 返回更新后的排班详情

#### Requirement 5: 实现删除排班功能
- **ID**: TASK-005-REQ-005
- **Description**: 实现 DELETE `/api/schedule/{scheduleId}` 端点
- **Business Logic**:
  1. 根据 scheduleId 查询排班记录
  2. 检查是否已有关联的预约
  3. 如果有预约，不允许删除（或设置为 UNAVAILABLE）
  4. 删除排班记录（或更新状态）

### 2.2 Technical Requirements

- **Framework**: Spring Boot 3.5.7, Spring MVC
- **Dependency**: DoctorScheduleRepository
- **Validation**: 使用 `@Valid` 和 Bean Validation 注解
- **Transaction**: 使用 `@Transactional` 保证操作的原子性
- **Exception Handling**: 使用 `@ControllerAdvice` 全局异常处理

### 2.3 Constraints and Limitations

- **Constraint 1**: API 响应格式需要与现有系统一致（参考 `.asdm/contexts/api.md`）
- **Constraint 2**: 需要使用 DTO 进行输入和输出，不要直接暴露实体类
- **Constraint 3**: 医生只能操作自己的排班（权限检查）
- **Constraint 4**: 删除排班时需要检查是否已有关联的预约

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **设计 DTO 类**: 创建请求和响应 DTO
2. **创建 Controller**: 定义端点和基本逻辑
3. **创建 Service**: 实现业务逻辑（创建、查询、更新、删除）
4. **添加验证**: 使用 Bean Validation 注解
5. **添加事务**: 使用 `@Transactional` 保证数据一致性
6. **测试**: 编写单元测试验证功能

### 3.2 Implementation Steps

1. **Step 1**: 创建 DTO 类
   - `ScheduleRequest.java` - 创建/更新排班请求
   - `ScheduleResponse.java` - 排班响应
   - `AvailableScheduleResponse.java` - 可用时间段响应（包含剩余名额）
   - 使用 Lombok `@Data` 简化代码

2. **Step 2**: 创建 `ScheduleService.java`
   - 实现 `createSchedule(ScheduleRequest request)` 方法
   - 实现 `getScheduleById(String scheduleId)` 方法
   - 实现 `getSchedulesByDoctorId(String doctorId, LocalDate startDate, LocalDate endDate)` 方法
   - 实现 `getAvailableSchedules(String doctorId, LocalDate startDate, LocalDate endDate)` 方法
   - 实现 `updateSchedule(String scheduleId, ScheduleRequest request)` 方法
   - 实现 `deleteSchedule(String scheduleId)` 方法
   - 添加 `@Transactional` 注解

3. **Step 3**: 创建 `ScheduleController.java`
   - 注入 `ScheduleService`
   - 定义 RESTful 端点
   - 添加 `@Valid` 注解验证输入
   - 返回 `ResponseEntity` 包含适当的 HTTP 状态码

4. **Step 4**: 测试编译和启动
   - Command: `cd server/qa-service-user && ./mvnw clean compile`
   - Command: `cd server/qa-service-user && ./mvnw spring-boot:run`
   - Success criteria: 编译成功，应用启动成功

**Validation Step**: 使用 Maven 编译验证代码正确性：
  - Command: `cd server/qa-service-user && ./mvnw clean compile`
  - Expected: 编译成功，退出码 0

### 3.3 Technical Considerations

- **考虑 1**: 使用 DTO 而不是实体类作为请求/响应，避免暴露内部数据结构
- **考虑 2**: 使用 `@Transactional` 保证排班操作的原子性
- **考虑 3**: 可用时间段查询需要考虑已取消的预约，释放名额
- **考虑 4**: 考虑使用 `@ControllerAdvice` 进行全局异常处理，保持与现有系统一致

### 3.4 Reference to Project Context

- `.asdm/contexts/api.md`: 现有 API 风格和端点设计
- `.asdm/contexts/standard-coding-style.md`: Java 编码规范
- 现有的 Controller: 参考 `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AuthController.java`

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `ScheduleController` 成功创建，包含所有必需的端点
  - Test method: 检查文件是否存在，端点是否定义
  - **Validation tool**: `find . -name "ScheduleController.java"` 和代码审查

- **Criterion 2**: 创建排班 API 成功实现
  - Test method: 使用 Postman 或 curl 测试创建排班
  - **Validation tool**: 集成测试或手动测试，验证数据库记录

- **Criterion 3**: 查询可用时间段 API 成功实现
  - Test method: 使用 Postman 或 curl 测试查询端点
  - **Validation tool**: 集成测试或手动测试，验证返回可用时间段

- **Criterion 4**: 更新和删除排班 API 成功实现
  - Test method: 使用 Postman 或 curl 测试更新和删除端点
  - **Validation tool**: 集成测试或手动测试，验证数据库记录更新

- **Criterion 5**: 代码编译通过，无错误
  - Test method: 运行 Maven 编译命令
  - **Validation tool**: `cd server/qa-service-user && ./mvnw clean compile`，退出码 0

### 4.2 Edge Cases

- **Edge case 1**: 创建排班时，已存在相同医生和时间的排班
  - Expected behavior: 返回 409 Conflict，提示排班已存在

- **Edge case 2**: 删除排班时，已有关联的预约
  - Expected behavior: 返回 400 Bad Request，提示无法删除

- **Edge case 3**: 查询可用时间段时，没有可用排班
  - Expected behavior: 返回空列表

### 4.3 Negative Tests

- **Negative test 1**: 创建排班时，请求体缺少必需字段
  - Expected behavior: 返回 400 Bad Request，提示验证错误

- **Negative test 2**: 查询不存在的排班 ID
  - Expected behavior: 返回 404 Not Found

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-003（后端：DoctorSchedule 实体类和 Repository）→ 需要实体类和 Repository
- **Blocks**: TASK-007（前端：医生排班页面（医生端））→ 前端需要调用这些 API

### 5.2 External Dependencies

- **Dependency 1**: Spring Boot Web（已在 pom.xml 中）
- **Dependency 2**: DoctorScheduleRepository（由 TASK-003 创建）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-003 完成，DoctorSchedule 实体类和 Repository 可用
- **Prerequisite 2**: `qa-service-user` 项目可以成功编译
- **Prerequisite 3**: 了解 Spring MVC 和 RESTful API 设计

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 2 hours（人工开发工作量）
- **Complexity**: Medium
- **Risk**: Medium

### 6.2 Effort Factors

- **Factor 1**: 需要设计 DTO 和 Service 层，工作量中等
- **Factor 2**: 可用时间段查询逻辑较复杂，需要计算剩余名额
- **Factor 3**: 需要考虑权限检查（医生只能操作自己的排班）

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Compilation check**:
  - Command: `cd server/qa-service-user && ./mvnw clean compile`
  - Success criteria: 编译成功，退出码 0

- **Application startup check**:
  - Command: `cd server/qa-service-user && ./mvnw spring-boot:run`（短暂运行）
  - Success criteria: 应用成功启动，无错误

### 7.2 Unit Testing

- **Test 1**: 测试 ScheduleService 的 createSchedule 方法
  - Approach: 使用 Mockito 模拟 Repository
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=ScheduleServiceTest`
  - Success criteria: 测试通过

- **Test 2**: 测试可用时间段查询逻辑
  - Approach: 模拟排班数据，验证查询结果
  - Command: 同上
  - Success criteria: 查询逻辑正确

### 7.3 Integration Testing

- **Test 1**: 测试创建排班 API 端点
  - Approach: 使用 MockMvc 或 TestRestTemplate 测试 Controller
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=ScheduleControllerTest`
  - Success criteria: API 端点返回预期响应

- **Test 2**: 测试查询可用时间段 API 端点
  - Approach: 同上
  - Command: 同上
  - Success criteria: 返回可用的排班时间段

### 7.4 Manual Testing

- **Manual test 1**: 使用 Postman 或 curl 手动测试 API
  - Steps: 启动应用 → 使用 Postman 发送请求 → 验证响应和数据库
  - Success criteria: API 按预期工作

---

## 8. Implementation Notes

### 8.1 代码示例

**ScheduleController.java**:
```java
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        ScheduleResponse response = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable String scheduleId) {
        ScheduleResponse response = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ScheduleResponse> responses = scheduleService.getSchedulesByDoctorId(doctorId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableScheduleResponse>> getAvailableSchedules(
            @RequestParam String doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AvailableScheduleResponse> responses = scheduleService.getAvailableSchedules(doctorId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable String scheduleId,
            @Valid @RequestBody ScheduleRequest request) {
        ScheduleResponse response = scheduleService.updateSchedule(scheduleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }
}
```

**ScheduleService.java** (partial):
```java
@Service
@Transactional
public class ScheduleService {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    public ScheduleResponse createSchedule(ScheduleRequest request) {
        // 1. 检查是否已存在相同医生和时间的排班
        // 2. 创建排班记录
        // 3. 返回排班详情
    }

    public List<AvailableScheduleResponse> getAvailableSchedules(String doctorId, LocalDate startDate, LocalDate endDate) {
        // 1. 查询排班
        // 2. 过滤状态为 AVAILABLE 的排班
        // 3. 计算每个时间段的剩余名额
        // 4. 只返回还有剩余名额的排班
    }
}
```

### 8.2 注意事项

- **关于事务**: 排班操作需要事务，使用 `@Transactional`
- **关于权限**: 医生只能操作自己的排班，需要添加权限检查
- **关于可用时间段**: 需要考虑已取消的预约，释放名额

---

## 9. Risks and Mitigations

### Risk 1: 权限检查不完善，导致数据泄露
- **Description**: 如果没有正确的权限检查，医生可能查看或修改其他医生的排班
- **Impact**: High
- **Mitigation**: 在 Service 层添加权限检查，确保医生只能操作自己的排班

### Risk 2: 可用时间段查询逻辑复杂，容易出错
- **Description**: 计算剩余名额需要考虑多种因素（已预约、已取消等）
- **Impact**: Medium
- **Mitigation**: 详细测试各种场景，确保逻辑正确

### Risk 3: 删除排班时未检查关联的预约
- **Description**: 如果删除已有关联预约的排班，会导致数据不一致
- **Impact**: High
- **Mitigation**: 在删除前检查是否已有关联的预约，如果有则不允许删除

---

## 10. Deliverables

- **Deliverable 1**: `ScheduleController.java` 控制器类
- **Deliverable 2**: `ScheduleService.java` 服务类
- **Deliverable 3**: `ScheduleRequest.java` DTO 类
- **Deliverable 4**: `ScheduleResponse.java` DTO 类
- **Deliverable 5**: `AvailableScheduleResponse.java` DTO 类
- **Deliverable 6**: 单元测试类（可选，但推荐）

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（Maven 编译日志，退出码 0）
- **Unit test results**: 证据显示单元测试通过
- **API test results**: 证据显示 API 端点工作正常（使用 Postman 或 curl）

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
