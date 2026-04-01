# API 测试文档

> 本文档列出了所有 API 端点及其测试命令、预期返回结果和测试场景

---

## 环境信息

- **后端服务地址**: http://localhost:8080
- **测试日期**: 2026-04-01
- **服务状态**: 运行中

---

## 一、医生相关 API

### 1.1 获取所有医生列表

**接口信息**
- **URL**: `/api/doctors`
- **方法**: `GET`
- **说明**: 获取系统中所有医生信息

**测试命令**
```bash
curl -X GET http://localhost:8080/api/doctors
```

**预期返回结果**
```json
{
  "code": 200,
  "data": [
    {
      "id": "doc001",
      "username": "dr-zhang-wei",
      "name": "张伟医生",
      "title": "主任医师",
      "department": "心内科",
      "avatar": "https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400",
      "experience": "15年临床经验",
      "isActive": true,
      "createdAt": null,
      "updatedAt": null,
      "specialties": ["高血压", "冠心病", "心律失常"]
    },
    // ... 其他医生数据
  ],
  "message": "success"
}
```

**测试场景**
- [x] 正常获取医生列表
- [x] 数据包含所有必要字段（id, name, title, department, isActive等）
- [x] 返回格式符合统一规范

---

### 1.2 获取在线医生列表

**接口信息**
- **URL**: `/api/doctors/active`
- **方法**: `GET`
- **说明**: 获取当前在线（isActive=true）的医生列表

**测试命令**
```bash
curl -X GET http://localhost:8080/api/doctors/active
```

**预期返回结果**
```json
{
  "code": 200,
  "data": [
    {
      "id": "doc001",
      "username": "dr-zhang-wei",
      "name": "张伟医生",
      "title": "主任医师",
      "department": "心内科",
      "avatar": "https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400",
      "experience": "15年临床经验",
      "isActive": true,
      "createdAt": null,
      "updatedAt": null,
      "specialties": ["高血压", "冠心病", "心律失常"]
    }
    // ... 仅返回 isActive=true 的医生
  ],
  "message": "success"
}
```

**测试场景**
- [x] 正常获取在线医生列表
- [x] 过滤掉 isActive=false 的医生
- [x] 数据格式正确

---

### 1.3 根据用户名获取医生信息

**接口信息**
- **URL**: `/api/doctors/{username}`
- **方法**: `GET`
- **说明**: 根据医生用户名获取详细信息
- **参数**: `username` - 医生用户名

**测试命令**
```bash
curl -X GET http://localhost:8080/api/doctors/dr-zhang-wei
```

**预期返回结果**
```json
{
  "code": 200,
  "data": {
    "id": "doc001",
    "username": "dr-zhang-wei",
    "name": "张伟医生",
    "title": "主任医师",
    "department": "心内科",
    "avatar": "https://images.pexels.com/photos/5215024/pexels-photo-5215024.jpeg?auto=compress&cs=tinysrgb&w=400",
    "experience": "15年临床经验",
    "isActive": true,
    "createdAt": null,
    "updatedAt": null,
    "specialties": ["高血压", "冠心病", "心律失常"]
  },
  "message": "success"
}
```

**测试场景**
- [x] 正常获取医生信息
- [x] 返回单条数据而非数组
- [x] 包含完整医生信息

---

## 二、患者认证 API

### 2.1 患者注册

**接口信息**
- **URL**: `/api/auth/patient/register`
- **方法**: `POST`
- **说明**: 新患者注册
- **Content-Type**: `application/json`

**请求参数**
```json
{
  "username": "testuser001",
  "password": "test123456",
  "name": "测试用户",
  "birthday": "1990-01-01",
  "phone": "13800138000",
  "gender": "male"
}
```

**测试命令**
```bash
curl -X POST http://localhost:8080/api/auth/patient/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser001",
    "password": "test123456",
    "name": "测试用户",
    "birthday": "1990-01-01",
    "phone": "13800138000",
    "gender": "male"
  }'
```

**预期返回结果**
```json
{
  "code": 200,
  "data": {
    "birthday": "1990-01-01",
    "createdAt": "2026-04-01T17:47:33.584866",
    "gender": "male",
    "phone": "13800138000",
    "name": "测试用户",
    "id": "82c7e4fef50d4e2f93272f34e0395cc6",
    "username": "testuser001"
  },
  "message": "注册成功"
}
```

**测试场景**
- [x] 正常注册成功
- [x] 返回创建的患者信息（不包含密码）
- [x] 生成唯一的 id
- [x] 自动设置 createdAt 时间戳

---

### 2.2 患者登录

**接口信息**
- **URL**: `/api/auth/patient/login`
- **方法**: `POST`
- **说明**: 患者使用用户名和密码登录
- **Content-Type**: `application/json`

**请求参数**
```json
{
  "username": "testuser001",
  "password": "test123456"
}
```

**测试命令**
```bash
# 正常登录
curl -X POST http://localhost:8080/api/auth/patient/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser001",
    "password": "test123456"
  }'

# 用户不存在
curl -X POST http://localhost:8080/api/auth/patient/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "wronguser",
    "password": "wrongpass"
  }'
```

**预期返回结果（成功）**
```json
{
  "code": 200,
  "data": {
    "birthday": "1990-01-01",
    "createdAt": "2026-04-01T17:47:33.584866",
    "gender": "male",
    "phone": "13800138000",
    "name": "测试用户",
    "id": "82c7e4fef50d4e2f93272f34e0395cc6",
    "username": "testuser001"
  },
  "message": "登录成功"
}
```

**预期返回结果（失败-用户不存在）**
```json
{
  "code": 1003,
  "message": "用户不存在,请先注册"
}
```

**测试场景**
- [x] 正常用户名密码登录成功
- [x] 返回完整用户信息
- [x] 用户不存在时返回错误码 1003
- [x] 密码错误时返回错误提示
- [x] 返回消息符合业务逻辑

---

### 2.3 用户名重复注册

**接口信息**
- **URL**: `/api/auth/patient/register`
- **方法**: `POST`
- **说明**: 使用已存在的用户名注册（应该失败）

**测试命令**
```bash
curl -X POST http://localhost:8080/api/auth/patient/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser001",
    "password": "test123456",
    "name": "重复用户",
    "birthday": "1990-01-01",
    "phone": "13800138001",
    "gender": "male"
  }'
```

**预期返回结果**
```json
{
  "code": 1001,
  "message": "该用户名已被注册,请更换"
}
```

**测试场景**
- [x] 重复用户名注册失败
- [x] 返回错误码 1001
- [x] 错误消息清晰明确
- [x] 不会创建重复用户记录

---

## 三、错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 1001 | 用户名已存在 |
| 1003 | 用户不存在 |

---

## 四、测试数据

### 4.1 医生测试数据

| 用户名 | 姓名 | 科室 | 状态 |
|--------|------|------|------|
| dr-zhang-wei | 张伟医生 | 心内科 | 在线 |
| dr-li-na | 李娜医生 | 儿科 | 在线 |
| dr-wang-qiang | 王强医生 | 骨科 | 在线 |
| dr-liu-min | 刘敏医生 | 妇产科 | 离线 |
| dr-chen-jie | 陈杰医生 | 消化内科 | 在线 |

### 4.2 患者测试数据

| 用户名 | 密码 | 姓名 | 手机号 |
|--------|------|------|--------|
| testuser001 | test123456 | 测试用户 | 13800138000 |

---

## 五、测试报告模板

### 测试执行记录

| 接口 | 测试场景 | 执行时间 | 结果 | 备注 |
|------|----------|----------|------|------|
| GET /api/doctors | 获取所有医生 | 2026-04-01 | ✅ 通过 | 返回5条数据 |
| GET /api/doctors/active | 获取在线医生 | 2026-04-01 | ✅ 通过 | 返回4条数据 |
| GET /api/doctors/{username} | 获取指定医生 | 2026-04-01 | ✅ 通过 | 返回张伟医生信息 |
| POST /api/auth/patient/register | 正常注册 | 2026-04-01 | ✅ 通过 | 注册成功 |
| POST /api/auth/patient/login | 正常登录 | 2026-04-01 | ✅ 通过 | 登录成功 |
| POST /api/auth/patient/login | 用户不存在 | 2026-04-01 | ✅ 通过 | 返回错误码1003 |
| POST /api/auth/patient/register | 重复注册 | 2026-04-01 | ✅ 通过 | 返回错误码1001 |

### 测试统计

- **总测试用例数**: 7
- **通过数**: 7
- **失败数**: 0
- **通过率**: 100%

---

## 六、注意事项

1. **密码安全性**: 当前开发环境使用明文存储密码，生产环境需要使用 BCrypt 加密
2. **认证机制**: 当前登录未实现 Token 认证，后续需要集成 JWT
3. **数据库**: 当前使用 H2 内存数据库，重启后数据会丢失
4. **CORS**: 已配置跨域支持，前端可以正常调用后端 API

---

*最后更新: 2026-04-01*
