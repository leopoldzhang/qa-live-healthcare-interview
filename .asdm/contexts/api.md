# API 接口文档

## 概述
本文档提供在线问诊系统的完整 API 文档，包括端点定义、请求/响应格式、使用示例和测试指南。

## API 元数据

### 基础 URL

| 环境 | URL | 说明 |
|------|-----|------|
| **开发环境** | `http://localhost:8080` (用户服务)<br>`http://localhost:8081` (问诊服务) | 本地开发服务器 |
| **生产环境** | 待配置 | 生产环境 |

### 服务端口

| 服务名 | 端口 | 说明 |
|--------|------|------|
| qa-service-user | 8080 | 用户管理服务（患者、医生） |
| qa-service-question | 8081 | 问诊服务（预留） |

### API 版本
- **当前版本**: v1.0.0
- **版本策略**: 无版本前缀（简化设计）
- **支持版本**: v1.0.x

### 跨域配置 (CORS)
- **qa-service-user**: 允许 `http://localhost:5173`, `http://localhost:3000`
- **qa-service-question**: 允许所有来源 (`*`)

### 通用请求头

| Header | 必需 | 说明 | 示例 |
|--------|------|------|------|
| `Content-Type` | 是 | 请求内容类型 | `application/json` |
| `Accept` | 是 | 期望响应类型 | `application/json` |

### 通用响应格式

**成功响应**:
```json
{
  "code": 200,
  "data": {},
  "message": "success"
}
```

**错误响应**:
```json
{
  "code": 1001,
  "data": null,
  "message": "错误描述"
}
```

### 错误码定义

| 错误码 | HTTP 状态 | 说明 |
|--------|-----------|------|
| 200 | 200 | 请求成功 |
| 1001 | 400 | 用户名已存在 |
| 1002 | 400 | 用户名或密码错误 |
| 1003 | 400 | 用户不存在 |
| 1004 | 400 | 参数验证失败（必填字段为空） |
| 1005 | 400 | 系统繁忙，请稍后重试 |
| 500 | 500 | 服务器内部错误 |

---

## API 定义

**说明**: 本节按服务分组组织 API 端点。每个服务包含多个 Controller，负责相关的业务逻辑。

---

## 服务一：qa-service-user (端口 8080)

### 1. AuthController (患者认证)

**源码**: `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AuthController.java`

**描述**: 处理患者用户的注册、登录、登出等认证操作。

**基础路径**: `/api/auth/patient`

| 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|
| `/register` | POST | 患者注册 | 200, 400 |
| `/login` | POST | 患者登录 | 200, 400 |
| `/logout` | POST | 患者登出 | 200 |
| `/check-username` | GET | 检查用户名是否存在 | 200 |
| `/` | GET | 获取所有患者列表 | 200, 500 |
| `/{id}` | GET | 根据 ID 获取患者信息 | 200, 400 |

#### POST /api/auth/patient/register

**描述**: 注册新患者账号。

**请求参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `username` | string | 是 | 用户名（唯一） | `"patient001"` |
| `password` | string | 是 | 密码 | `"password123"` |
| `name` | string | 是 | 真实姓名 | `"张三"` |
| `birthday` | string | 否 | 出生日期 | `"1990-01-01"` |
| `phone` | string | 否 | 手机号 | `"13800138000"` |
| `gender` | string | 否 | 性别 | `"男"` 或 `"女"` |

**请求示例**:
```json
{
  "username": "patient001",
  "password": "password123",
  "name": "张三",
  "birthday": "1990-01-01",
  "phone": "13800138000",
  "gender": "男"
}
```

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "username": "patient001",
    "name": "张三",
    "birthday": "1990-01-01",
    "phone": "13800138000",
    "gender": "男",
    "createdAt": "2024-01-15T10:30:00"
  },
  "message": "注册成功"
}
```

**错误响应**:
- **1001** - 用户名已被注册
- **1004** - 用户名、密码或姓名为空
- **1005** - 系统繁忙

---

#### POST /api/auth/patient/login

**描述**: 患者登录，验证用户名和密码。

**请求参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `username` | string | 是 | 用户名 | `"patient001"` |
| `password` | string | 是 | 密码 | `"password123"` |

**请求示例**:
```json
{
  "username": "patient001",
  "password": "password123"
}
```

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "username": "patient001",
    "name": "张三",
    "birthday": "1990-01-01",
    "phone": "13800138000",
    "gender": "男",
    "createdAt": "2024-01-15T10:30:00"
  },
  "message": "登录成功"
}
```

**错误响应**:
- **1002** - 用户名或密码错误
- **1003** - 用户不存在，请先注册
- **1004** - 用户名或密码为空

---

#### POST /api/auth/patient/logout

**描述**: 患者登出（前端清除本地存储即可）。

**请求参数**: 无

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": null,
  "message": "登出成功"
}
```

---

#### GET /api/auth/patient/check-username

**描述**: 检查用户名是否已被注册。

**查询参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `username` | string | 是 | 待检查的用户名 | `"patient001"` |

**请求示例**:
```
GET /api/auth/patient/check-username?username=patient001
```

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "exists": false
  },
  "message": "success"
}
```

---

#### GET /api/auth/patient

**描述**: 获取所有患者列表（管理员功能）。

**请求参数**: 无

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "username": "patient001",
      "name": "张三",
      "birthday": "1990-01-01",
      "phone": "13800138000",
      "gender": "男",
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "message": "success"
}
```

---

#### GET /api/auth/patient/{id}

**描述**: 根据 ID 获取患者详细信息。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `id` | string | 是 | 患者 ID | `"1"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "username": "patient001",
    "name": "张三",
    "birthday": "1990-01-01",
    "phone": "13800138000",
    "gender": "男",
    "createdAt": "2024-01-15T10:30:00"
  },
  "message": "success"
}
```

**错误响应**:
- **1003** - 患者不存在

---

### 2. DoctorController (医生管理)

**源码**: `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/DoctorController.java`

**描述**: 管理医生信息的查询操作。

**基础路径**: `/api/doctors`

| 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|
| `/` | GET | 获取所有医生列表 | 200, 500 |
| `/active` | GET | 获取活跃医生列表 | 200, 500 |
| `/{username}` | GET | 根据用户名获取医生信息 | 200, 404, 500 |

#### GET /api/doctors

**描述**: 获取所有医生列表。

**请求参数**: 无

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "username": "doctor001",
      "password": "$2a$10$...",
      "name": "李医生",
      "title": "主任医师",
      "department": "内科",
      "avatar": "",
      "experience": "10年",
      "isActive": true,
      "specialties": ["感冒", "发烧", "咳嗽"]
    }
  ],
  "message": "success"
}
```

---

#### GET /api/doctors/active

**描述**: 获取所有活跃医生列表（可接诊）。

**请求参数**: 无

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "username": "doctor001",
      "name": "李医生",
      "title": "主任医师",
      "department": "内科",
      "avatar": "",
      "experience": "10年",
      "isActive": true,
      "specialties": ["感冒", "发烧", "咳嗽"]
    }
  ],
  "message": "success"
}
```

---

#### GET /api/doctors/{username}

**描述**: 根据用户名获取医生详细信息。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `username` | string | 是 | 医生用户名 | `"doctor001"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "username": "doctor001",
    "name": "李医生",
    "title": "主任医师",
    "department": "内科",
    "avatar": "",
    "experience": "10年",
    "isActive": true,
    "specialties": ["感冒", "发烧", "咳嗽"]
  },
  "message": "success"
}
```

**错误响应**:
- **404** - 医生不存在

---

### 3. AppointmentController (预约管理)

**源码**: `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AppointmentController.java`

**描述**: 处理患者预约医生的相关操作。

**基础路径**: `/api/appointment`

| 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|
| `/` | POST | 创建预约 | 201, 400 |
| `/{appointmentNo}` | GET | 根据预约单号查询预约 | 200, 400 |
| `/patient/{patientId}` | GET | 根据患者ID查询预约记录 | 200, 400 |
| `/{appointmentNo}/cancel` | PUT | 取消预约 | 200, 400 |
| `/{appointmentNo}/confirm` | PUT | 确认预约（医生） | 200, 400 |
| `/{appointmentNo}/complete` | PUT | 完成预约（医生） | 200, 400 |

#### POST /api/appointment

**描述**: 创建预约。

**请求参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `patientId` | string | 是 | 患者ID | `"patient001"` |
| `doctorId` | string | 是 | 医生ID | `"doc001"` |
| `appointmentDate` | string | 是 | 预约日期 | `"2026-04-30"` |
| `timeSlot` | string | 是 | 时间段 | `"09:00-09:30"` |
| `location` | string | 否 | 地点 | `"门诊楼3楼"` |
| `description` | string | 否 | 描述 | `"初诊"` |

**请求示例**:
```json
{
  "patientId": "patient001",
  "doctorId": "doc001",
  "appointmentDate": "2026-04-30",
  "timeSlot": "09:00-09:30",
  "location": "门诊楼3楼",
  "description": "初诊"
}
```

**成功响应 (201)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "appointmentNo": "APT1234567890",
    "patientId": "patient001",
    "patientName": "张三",
    "doctorId": "doc001",
    "doctorName": "张伟医生",
    "appointmentDate": "2026-04-30",
    "timeSlot": "09:00-09:30",
    "location": "门诊楼3楼",
    "status": "PENDING",
    "description": "初诊",
    "createTime": "2026-04-29T10:30:00"
  },
  "message": "预约成功"
}
```

**错误响应**:
- **1003** - 患者不存在或医生不存在
- **1004** - 预约日期/时间段为空，或医生排班不可用，或医生该时间段已约满，或重复预约
- **1005** - 系统繁忙

---

#### GET /api/appointment/{appointmentNo}

**描述**: 根据预约单号查询预约详情。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `appointmentNo` | string | 是 | 预约单号 | `"APT1234567890"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "appointmentNo": "APT1234567890",
    "patientId": "patient001",
    "patientName": "张三",
    "doctorId": "doc001",
    "doctorName": "张伟医生",
    "appointmentDate": "2026-04-30",
    "timeSlot": "09:00-09:30",
    "location": "门诊楼3楼",
    "status": "PENDING",
    "description": "初诊",
    "createTime": "2026-04-29T10:30:00",
    "updateTime": "2026-04-29T10:30:00"
  },
  "message": "success"
}
```

**错误响应**:
- **1003** - 预约不存在

---

#### GET /api/appointment/patient/{patientId}

**描述**: 根据患者ID查询预约记录（可筛选状态）。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `patientId` | string | 是 | 患者ID | `"patient001"` |

**查询参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `status` | string | 否 | 预约状态筛选 | `"PENDING"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "appointmentNo": "APT1234567890",
      "patientId": "patient001",
      "patientName": "张三",
      "doctorId": "doc001",
      "doctorName": "张伟医生",
      "appointmentDate": "2026-04-30",
      "timeSlot": "09:00-09:30",
      "location": "门诊楼3楼",
      "status": "PENDING",
      "description": "初诊",
      "createTime": "2026-04-29T10:30:00"
    }
  ],
  "message": "success"
}
```

---

#### PUT /api/appointment/{appointmentNo}/cancel

**描述**: 取消预约。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `appointmentNo` | string | 是 | 预约单号 | `"APT1234567890"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "appointmentNo": "APT1234567890",
    "status": "CANCELLED",
    "updateTime": "2026-04-29T11:00:00"
  },
  "message": "预约已取消"
}
```

**错误响应**:
- **1003** - 预约不存在
- **1004** - 无法取消预约（状态不允许）

---

#### PUT /api/appointment/{appointmentNo}/confirm

**描述**: 确认预约（医生操作）。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `appointmentNo` | string | 是 | 预约单号 | `"APT1234567890"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "appointmentNo": "APT1234567890",
    "status": "CONFIRMED",
    "updateTime": "2026-04-29T11:30:00"
  },
  "message": "预约已确认"
}
```

**错误响应**:
- **1003** - 预约不存在
- **1004** - 无法确认预约（状态不允许）

---

#### PUT /api/appointment/{appointmentNo}/complete

**描述**: 完成预约（医生操作）。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `appointmentNo` | string | 是 | 预约单号 | `"APT1234567890"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "appointmentNo": "APT1234567890",
    "status": "COMPLETED",
    "updateTime": "2026-04-30T09:30:00"
  },
  "message": "预约已完成"
}
```

**错误响应**:
- **1003** - 预约不存在
- **1004** - 无法完成预约（状态不允许）

---

### 4. ScheduleController (排班管理)

**源码**: `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/ScheduleController.java`

**描述**: 处理医生排班的相关操作。

**基础路径**: `/api/schedule`

| 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|
| `/doctor/{doctorId}` | GET | 获取医生的排班列表 | 200, 400 |
| `/available` | GET | 获取可用排班时间段 | 200, 400 |
| `/` | POST | 创建排班 | 201, 400 |
| `/{scheduleId}` | PUT | 更新排班 | 200, 400 |
| `/{scheduleId}` | DELETE | 删除排班 | 200, 400 |

#### GET /api/schedule/doctor/{doctorId}

**描述**: 获取指定医生的排班列表。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `doctorId` | string | 是 | 医生ID | `"doc001"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "doctorId": "doc001",
      "doctorName": "张伟医生",
      "scheduleDate": "2026-04-30",
      "timeSlot": "09:00-09:30",
      "location": "门诊楼3楼",
      "maxAppointments": 1,
      "bookedCount": 0,
      "available": true
    }
  ],
  "message": "success"
}
```

---

#### GET /api/schedule/available

**描述**: 获取可用排班时间段。

**查询参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `doctorId` | string | 否 | 医生ID | `"doc001"` |
| `startDate` | string | 否 | 开始日期 | `"2026-04-30"` |
| `endDate` | string | 否 | 结束日期 | `"2026-05-07"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": [
    {
      "id": "1",
      "doctorId": "doc001",
      "doctorName": "张伟医生",
      "scheduleDate": "2026-04-30",
      "timeSlot": "09:00-09:30",
      "location": "门诊楼3楼",
      "maxAppointments": 1,
      "bookedCount": 0,
      "available": true
    }
  ],
  "message": "success"
}
```

---

#### POST /api/schedule

**描述**: 创建排班（医生操作）。

**请求参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `doctorId` | string | 是 | 医生ID | `"doc001"` |
| `doctorName` | string | 是 | 医生姓名 | `"张伟医生"` |
| `scheduleDate` | string | 是 | 排班日期 | `"2026-04-30"` |
| `timeSlot` | string | 是 | 时间段 | `"09:00-09:30"` |
| `location` | string | 否 | 地点 | `"门诊楼3楼"` |
| `maxAppointments` | int | 否 | 最大预约数 | `1` |

**成功响应 (201)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "doctorId": "doc001",
    "doctorName": "张伟医生",
    "scheduleDate": "2026-04-30",
    "timeSlot": "09:00-09:30",
    "location": "门诊楼3楼",
    "maxAppointments": 1,
    "bookedCount": 0,
    "available": true
  },
  "message": "排班创建成功"
}
```

---

#### PUT /api/schedule/{scheduleId}

**描述**: 更新排班信息。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `scheduleId` | string | 是 | 排班ID | `"1"` |

**请求参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `scheduleDate` | string | 否 | 排班日期 | `"2026-05-01"` |
| `timeSlot` | string | 否 | 时间段 | `"10:00-10:30"` |
| `location` | string | 否 | 地点 | `"门诊楼4楼"` |
| `maxAppointments` | int | 否 | 最大预约数 | `2` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": {
    "id": "1",
    "scheduleDate": "2026-05-01",
    "timeSlot": "10:00-10:30",
    "location": "门诊楼4楼",
    "maxAppointments": 2,
    "updateTime": "2026-04-29T12:00:00"
  },
  "message": "排班更新成功"
}
```

---

#### DELETE /api/schedule/{scheduleId}

**描述**: 删除排班。

**路径参数**:

| 参数 | 类型 | 必需 | 说明 | 示例 |
|------|------|------|------|------|
| `scheduleId` | string | 是 | 排班ID | `"1"` |

**成功响应 (200)**:
```json
{
  "code": 200,
  "data": null,
  "message": "排班删除成功"
}
```

---

### 5. TestController (CORS 测试)

**源码**: `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/TestController.java`

**描述**: 用于测试 CORS 配置是否正常工作。

**基础路径**: `/api/test`

| 端点 | 方法 | 说明 | 状态 |
|------|------|------|------|
| `/cors` | GET | 测试 CORS GET 请求 | 200 |
| `/cors` | POST | 测试 CORS POST 请求 | 200 |
| `/cors` | OPTIONS | 处理预检请求 | 200 |

#### GET /api/test/cors

**描述**: 测试 CORS GET 请求。

**成功响应 (200)**:
```json
{
  "message": "CORS configuration is working!",
  "timestamp": 1700000000000,
  "service": "qa-service-user"
}
```

---

#### POST /api/test/cors

**描述**: 测试 CORS POST 请求。

**请求参数**: 任意 JSON 数据（可选）

**成功响应 (200)**:
```json
{
  "message": "POST request with CORS is working!",
  "receivedData": {},
  "timestamp": 1700000000000,
  "service": "qa-service-user"
}
```

---

## 服务二：qa-service-question (端口 8081)

**状态**: 🚧 开发中

此服务预留用于问诊核心功能（问答、问诊记录等），当前版本尚未实现 Controller。

**计划功能**:
- 创建问诊
- 提交问题
- 医生回复
- 问诊历史查询

---

## 前端 API 封装

### auth.ts

**源码**: `web/qa-web/src/api/auth.ts`

**封装的 API**:
- `register(data)` - 患者注册
- `login(data)` - 患者登录
- `logout()` - 患者登出
- `checkUsername(username)` - 检查用户名
- `getAllPatients()` - 获取所有患者
- `getPatientById(id)` - 根据 ID 获取患者

**类型定义**:
```typescript
interface Patient {
  id: string;
  username: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
  createdAt?: string;
}

interface RegisterRequest {
  username: string;
  password: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
}

interface LoginRequest {
  username: string;
  password: string;
}

interface ApiResponse<T> {
  code: number;
  data: T;
  message: string;
}
```

---

### doctor.ts

**源码**: `web/qa-web/src/api/doctor.ts`

**封装的 API**:
- `getAllDoctors()` - 获取所有医生
- `getActiveDoctors()` - 获取活跃医生
- `getDoctorByUsername(username)` - 根据用户名获取医生

**类型定义**:
```typescript
interface Doctor {
  id: string;
  username: string;
  name: string;
  title: string;
  department: string;
  avatar: string;
  experience: string;
  isActive: boolean;
  specialties: string[];
}
```

---

### appointment.ts

**源码**: `web/qa-web/src/api/appointment.ts`

**封装的 API (预约)**:
- `getAppointmentList(params)` - 获取预约列表
- `createAppointment(data)` - 创建预约
- `cancelAppointment(appointmentNo)` - 取消预约
- `getAppointmentDetail(appointmentNo)` - 获取预约详情

**封装的 API (排班)**:
- `getSchedulesByDoctorId(doctorId)` - 获取医生的排班列表
- `getAvailableSchedules(params)` - 获取可用排班时间段
- `createSchedule(data)` - 创建排班
- `updateSchedule(scheduleId, data)` - 更新排班
- `deleteSchedule(scheduleId)` - 删除排班
- `getScheduleById(scheduleId)` - 获取排班详情

**类型定义**:
```typescript
interface Appointment {
  id: string;
  appointmentNo: string;
  patientId: string;
  patientName: string;
  doctorId: string;
  doctorName: string;
  appointmentDate: string;
  timeSlot: string;
  location?: string;
  status: 'PENDING' | 'CONFIRMED' | 'COMPLETED' | 'CANCELLED';
  description?: string;
  createTime: string;
  updateTime: string;
}

interface Schedule {
  id: string;
  doctorId: string;
  doctorName: string;
  scheduleDate: string;
  timeSlot: string;
  location?: string;
  maxAppointments: number;
  bookedCount: number;
  available: boolean;
}
```

---

## API 测试

### cURL 测试命令

#### 患者注册
```bash
curl -X POST http://localhost:8080/api/auth/patient/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "patient001",
    "password": "password123",
    "name": "张三",
    "birthday": "1990-01-01",
    "phone": "13800138000",
    "gender": "男"
  }'
```

#### 患者登录
```bash
curl -X POST http://localhost:8080/api/auth/patient/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "patient001",
    "password": "password123"
  }'
```

#### 检查用户名
```bash
curl -X GET "http://localhost:8080/api/auth/patient/check-username?username=patient001"
```

#### 获取所有医生
```bash
curl -X GET http://localhost:8080/api/doctors
```

#### 获取活跃医生
```bash
curl -X GET http://localhost:8080/api/doctors/active
```

#### 根据用户名获取医生
```bash
curl -X GET http://localhost:8080/api/doctors/doctor001
```

#### 测试 CORS
```bash
curl -X GET http://localhost:8080/api/test/cors
```

---

## Postman 测试集合

可以将以下 JSON 导入 Postman 进行 API 测试：

```json
{
  "info": {
    "name": "在线问诊系统 API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "患者注册",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/auth/patient/register",
        "header": [
          {"key": "Content-Type", "value": "application/json"}
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"username\":\"patient001\",\"password\":\"password123\",\"name\":\"张三\",\"birthday\":\"1990-01-01\",\"phone\":\"13800138000\",\"gender\":\"男\"}"
        }
      }
    },
    {
      "name": "患者登录",
      "request": {
        "method": "POST",
        "url": "http://localhost:8080/api/auth/patient/login",
        "header": [
          {"key": "Content-Type", "value": "application/json"}
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"username\":\"patient001\",\"password\":\"password123\"}"
        }
      }
    },
    {
      "name": "获取医生列表",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/api/doctors"
      }
    }
  ]
}
```

---

## API 变更日志

| 版本 | 日期 | 变更内容 |
|------|------|----------|
| **v1.0.0** | 2024-01-01 | 初始版本发布 |
| | | - 患者注册/登录功能 |
| | | - 医生信息查询功能 |
| | | - CORS 测试接口 |

---

## 相关源码文件

- `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/AuthController.java`
- `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/DoctorController.java`
- `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/controller/TestController.java`
- `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/Patient.java`
- `server/qa-service-user/src/main/java/com/leansofx/qaserviceuser/entity/Doctor.java`
- `web/qa-web/src/api/auth.ts`
- `web/qa-web/src/api/doctor.ts`

---

*本文档按服务分组组织 API 端点。每当端点变更时，请使用 `/asdm-context-update` 更新此文档。*
