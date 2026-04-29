# Task PRD: 后端：预约 API 接口实现

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-004
**Created Date**: 2026-04-29
**Status**: DONE
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在实现预约挂号功能的后端 RESTful API 接口，包括创建预约、查询预约记录、取消预约等功能。

**任务目标**：
- 创建 `AppointmentController` 控制器类
- 实现预约相关的 RESTful API 端点
- 实现预约业务逻辑（冲突检测、状态管理等）
- 添加必要的输入验证和错误处理

**必要性**：
- 前端需要调用后端 API 完成预约操作
- API 是前后端交互的桥梁
- 业务逻辑需要在后端实现以保证数据一致性和安全性

### 1.2 Task Objectives

- **Objective 1**: 创建 `AppointmentController` 控制器，提供 RESTful API
- **Objective 2**: 实现预约创建、查询、取消等核心功能
- **Objective 3**: 实现预约冲突检测逻辑
- **Objective 4**: 添加输入验证和错误处理
- **Objective 5**: 确保 API 风格与现有系统一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-002（患者预约功能）
- 功能需求：REQ-003（预约记录管理）
- 功能需求：REQ-005（预约冲突检测）
- 用户故事：Story 1, Story 2

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建 AppointmentController
- **ID**: TASK-004-REQ-001
- **Description**: 创建控制器类，定义预约相关的 API 端点
- **Base URL**: `/api/appointment`
- **Endpoints**:
  - `POST /api/appointment` - 创建新预约
  - `GET /api/appointment/{appointmentNo}` - 获取预约详情
  - `GET /api/appointment/patient/{patientId}` - 获取患者的预约记录
  - `PUT /api/appointment/{appointmentNo}/cancel` - 取消预约
  - `PUT /api/appointment/{appointmentNo}/confirm` - 确认预约（医生）
  - `PUT /api/appointment/{appointmentNo}/complete` - 完成预约（医生）

#### Requirement 2: 实现创建预约功能
- **ID**: TASK-004-REQ-002
- **Description**: 实现 POST `/api/appointment` 端点
- **Request Body**:
  ```json
  {
    "patientId": "string",
    "doctorId": "string",
    "appointmentDate": "2026-05-01",
    "timeSlot": "09:00-09:30",
    "location": "string (optional)",
    "description": "string (optional)"
  }
  ```
- **Business Logic**:
  1. 验证输入参数
  2. 检查医生排班是否存在且可用
  3. 检查预约冲突（同一患者同一时间段不能预约多个医生）
  4. 检查医生时间段是否已满
  5. 创建预约记录（状态：PENDING）
  6. 更新医生排班的当前预约数
  7. 返回预约详情（包括 appointmentNo）

#### Requirement 3: 实现查询预约记录功能
- **ID**: TASK-004-REQ-003
- **Description**: 实现 GET 端点查询预约记录
- **Endpoints**:
  - `GET /api/appointment/{appointmentNo}` - 根据预约单号查询
  - `GET /api/appointment/patient/{patientId}?status=xxx` - 根据患者ID查询，可选过滤状态
- **Response**: 预约详情或预约列表

#### Requirement 4: 实现取消预约功能
- **ID**: TASK-004-REQ-004
- **Description**: 实现 PUT `/api/appointment/{appointmentNo}/cancel` 端点
- **Business Logic**:
  1. 根据 appointmentNo 查询预约记录
  2. 检查预约状态（只有 PENDING 和 CONFIRMED 可以取消）
  3. 更新预约状态为 CANCELLED
  4. 减少医生排班的当前预约数
  5. 返回更新后的预约详情

#### Requirement 5: 实现预约冲突检测
- **ID**: TASK-004-REQ-005
- **Description**: 在实现创建预约时，检测并防止预约冲突
- **Conflict Rules**:
  1. 同一患者不能在同一时间段预约多个医生
  2. 同一患者在同一医生的同一时间段只能预约一次
  3. 医生的时间段达到最大预约数后不再接受新的预约
- **Implementation**: 使用数据库事务和悲观锁或乐观锁保证一致性

### 2.2 Technical Requirements

- **Framework**: Spring Boot 3.5.7, Spring MVC
- **Dependency**: AppointmentRepository, DoctorScheduleRepository
- **Validation**: 使用 `@Valid` 和 Bean Validation 注解
- **Transaction**: 使用 `@Transactional` 保证操作的原子性
- **Exception Handling**: 使用 `@ControllerAdvice` 全局异常处理（或局部处理）

### 2.3 Constraints and Limitations

- **Constraint 1**: API 响应格式需要与现有系统一致（参考 `.asdm/contexts/api.md`）
- **Constraint 2**: 需要使用 DTO 进行输入和输出，不要直接暴露实体类
- **Constraint 3**: 需要考虑并发情况，使用数据库锁或分布式锁
- **Constraint 4**: 患者只能操作自己的预约记录（权限检查）

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **设计 DTO 类**: 创建请求和响应 DTO
2. **创建 Controller**: 定义端点和基本逻辑
3. **创建 Service**: 实现业务逻辑（预约、取消、冲突检测等）
4. **添加验证**: 使用 Bean Validation 注解
5. **添加事务**: 使用 `@Transactional` 保证数据一致性
6. **测试**: 编写单元测试验证功能

### 3.2 Implementation Steps

1. **Step 1**: 创建 DTO 类
   - `AppointmentRequest.java` - 创建预约请求
   - `AppointmentResponse.java` - 预约响应
   - 使用 Lombok `@Data` 简化代码

2. **Step 2**: 创建 `AppointmentService.java`
   - 实现 `createAppointment(AppointmentRequest request)` 方法
   - 实现 `cancelAppointment(String appointmentNo)` 方法
   - 实现 `getAppointmentByNo(String appointmentNo)` 方法
   - 实现 `getAppointmentsByPatientId(String patientId, String status)` 方法
   - 添加 `@Transactional` 注解

3. **Step 3**: 创建 `AppointmentController.java`
   - 注入 `AppointmentService`
   - 定义 RESTful 端点
   - 添加 `@Valid` 注解验证输入
   - 返回 `ResponseEntity` 包含适当的 HTTP 状态码

4. **Step 4**: 实现冲突检测逻辑
   - 在 `createAppointment` 方法中添加冲突检查
   - 使用 `AppointmentRepository` 查询冲突
   - 使用数据库事务和锁避免并发问题

5. **Step 5**: 测试编译和启动
   - Command: `cd server/qa-service-user && ./mvnw clean compile`
   - Command: `cd server/qa-service-user && ./mvnw spring-boot:run`
   - Success criteria: 编译成功，应用启动成功

**Validation Step**: 使用 Maven 编译和启动应用验证：
  - Command: `cd server/qa-service-user && ./mvnw clean compile && ./mvnw spring-boot:run`
  - Expected: 编译成功，应用启动成功，无错误

### 3.3 Technical Considerations

- **考虑 1**: 使用 DTO 而不是实体类作为请求/响应，避免暴露内部数据结构
- **考虑 2**: 使用 `@Transactional` 保证预约操作的原子性
- **考虑 3**: 冲突检测需要使用数据库锁（如 `PESSIMISTIC_LOCK` 或乐观锁）
- **考虑 4**: 考虑使用 `@ControllerAdvice` 进行全局异常处理，保持与现有系统一致

### 3.4 Reference to Project Context

- `.asdm/contexts/api.md`: 现有 API 风格和端点设计
- `.asdm/contexts/standard-coding-style.md`: Java 编码规范
- 现有的 Controller: 参考 `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AuthController.java`

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `AppointmentController` 成功创建，包含所有必需的端点
  - Test method: 检查文件是否存在，端点是否定义
  - **Validation tool**: `find . -name "AppointmentController.java"` 和代码审查

- **Criterion 2**: 创建预约 API 成功实现，包含冲突检测
  - Test method: 使用 Postman 或 curl 测试创建预约
  - **Validation tool**: 集成测试或手动测试，验证数据库记录

- **Criterion 3**: 查询预约记录 API 成功实现
  - Test method: 使用 Postman 或 curl 测试查询端点
  - **Validation tool**: 集成测试或手动测试

- **Criterion 4**: 取消预约 API 成功实现
  - Test method: 使用 Postman 或 curl 测试取消端点
  - **Validation tool**: 集成测试或手动测试，验证状态更新

- **Criterion 5**: 代码编译通过，无错误
  - Test method: 运行 Maven 编译命令
  - **Validation tool**: `cd server/qa-service-user && ./mvnw clean compile`，退出码 0

### 4.2 Edge Cases

- **Edge case 1**: 创建预约时，医生排班不存在
  - Expected behavior: 返回 400 Bad Request 或 404 Not Found

- **Edge case 2**: 取消预约时，预约状态不是 PENDING 或 CONFIRMED
  - Expected behavior: 返回 400 Bad Request，提示状态错误

- **Edge case 3**: 同一患者同一时间段预约两个医生
  - Expected behavior: 返回 409 Conflict，提示预约冲突

### 4.3 Negative Tests

- **Negative test 1**: 创建预约时，请求体缺少必需字段
  - Expected behavior: 返回 400 Bad Request，提示验证错误

- **Negative test 2**: 查询不存在的预约单号
  - Expected behavior: 返回 404 Not Found

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-002（后端：Appointment 实体类和 Repository）→ 需要实体类和 Repository
- **Blocks**: TASK-006（前端：预约列表页面）, TASK-008（前端：新增预约页面）→ 前端需要调用这些 API

### 5.2 External Dependencies

- **Dependency 1**: Spring Boot Web（已在 pom.xml 中）
- **Dependency 2**: AppointmentRepository（由 TASK-002 创建）
- **Dependency 3**: DoctorScheduleRepository（由 TASK-003 创建）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-002 完成，Appointment 实体类和 Repository 可用
- **Prerequisite 2**: TASK-003 完成（可选，用于冲突检测）
- **Prerequisite 3**: 了解 Spring MVC 和 RESTful API 设计

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 2 hours（人工开发工作量）
- **Complexity**: Medium
- **Risk**: Medium

### 6.2 Effort Factors

- **Factor 1**: 需要设计 DTO 和 Service 层，工作量中等
- **Factor 2**: 冲突检测逻辑较复杂，需要考虑并发
- **Factor 3**: 需要参考现有 Controller 的代码风格

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

- **Test 1**: 测试 AppointmentService 的 createAppointment 方法
  - Approach: 使用 Mockito 模拟 Repository
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=AppointmentServiceTest`
  - Success criteria: 测试通过

- **Test 2**: 测试冲突检测逻辑
  - Approach: 模拟冲突场景，验证抛出异常
  - Command: 同上
  - Success criteria: 冲突检测正常工作

### 7.3 Integration Testing

- **Test 1**: 测试创建预约 API 端点
  - Approach: 使用 MockMvc 或 TestRestTemplate 测试 Controller
  - Command: `cd server/qa-service-user && ./mvnw test -Dtest=AppointmentControllerTest`
  - Success criteria: API 端点返回预期响应

- **Test 2**: 测试取消预约 API 端点
  - Approach: 同上
  - Command: 同上
  - Success criteria: 状态正确更新

### 7.4 Manual Testing

- **Manual test 1**: 使用 Postman 或 curl 手动测试 API
  - Steps: 启动应用 → 使用 Postman 发送请求 → 验证响应和数据库
  - Success criteria: API 按预期工作

---

## 8. Implementation Notes

### 8.1 代码示例

**AppointmentController.java**:
```java
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{appointmentNo}")
    public ResponseEntity<AppointmentResponse> getAppointmentByNo(@PathVariable String appointmentNo) {
        AppointmentResponse response = appointmentService.getAppointmentByNo(appointmentNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(
            @PathVariable String patientId,
            @RequestParam(required = false) String status) {
        List<AppointmentResponse> responses = appointmentService.getAppointmentsByPatientId(patientId, status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{appointmentNo}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable String appointmentNo) {
        AppointmentResponse response = appointmentService.cancelAppointment(appointmentNo);
        return ResponseEntity.ok(response);
    }
}
```

**AppointmentService.java** (partial):
```java
@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    public AppointmentResponse createAppointment(AppointmentRequest request) {
        // 1. 验证医生排班是否存在
        // 2. 检查预约冲突
        // 3. 创建预约记录
        // 4. 更新医生排班当前预约数
        // 5. 返回预约详情
    }

    private void checkAppointmentConflict(AppointmentRequest request) {
        // 实现冲突检测逻辑
    }
}
```

### 8.2 注意事项

- **关于事务**: 预约操作需要事务，使用 `@Transactional`
- **关于并发**: 冲突检测需要使用数据库锁，避免超卖
- **关于权限**: 患者需要只能操作自己的预约，医生只能操作自己的预约

---

## 9. Risks and Mitigations

### Risk 1: 并发预约导致超卖
- **Description**: 多个患者同时预约同一时间段，可能导致超过最大预约数
- **Impact**: High
- **Mitigation**: 使用数据库悲观锁（`PESSIMISTIC_LOCK`）或乐观锁（`@Version`）

### Risk 2: API 响应格式与现有系统不一致
- **Description**: 新 API 的响应格式可能与现有 API 不同，导致前端需要特殊处理
- **Impact**: Medium
- **Mitigation**: 仔细参考现有 Controller 的响应格式，保持一致性

### Risk 3: 冲突检测逻辑复杂，容易出错
- **Description**: 预约冲突规则可能很复杂，实现不当会导致数据不一致
- **Impact**: High
- **Mitigation**: 详细测试各种冲突场景，使用事务和锁保证一致性

---

## 10. Deliverables

- **Deliverable 1**: `AppointmentController.java` 控制器类
- **Deliverable 2**: `AppointmentService.java` 服务类
- **Deliverable 3**: `AppointmentRequest.java` DTO 类
- **Deliverable 4**: `AppointmentResponse.java` DTO 类
- **Deliverable 5**: 单元测试类（可选，但推荐）

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（Maven 编译日志，退出码 0）
- **Unit test results**: 证据显示单元测试通过
- **API test results**: 证据显示 API 端点工作正常（使用 Postman 或 curl）

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
