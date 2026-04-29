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

### 3. TestController (CORS 测试)

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
