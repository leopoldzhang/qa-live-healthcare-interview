# Task PRD: 前端：预约列表页面

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-006
**Created Date**: 2026-04-29
**Status**: DONE
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在创建患者查看自己预约记录的列表页面，包括预约状态的筛选、查看详情、取消预约等功能。

**任务目标**：
- 创建 `AppointmentList.vue` 组件
- 实现预约记录查询和展示
- 实现按状态筛选功能
- 实现取消预约功能

**必要性**：
- 患者需要查看自己的预约记录
- 患者需要管理自己的预约（取消、查看详情）
- 这是预约功能的重要组成部分

### 1.2 Task Objectives

- **Objective 1**: 创建预约列表页面 Vue 组件
- **Objective 2**: 实现调用后端 API 获取预约记录
- **Objective 3**: 实现按状态筛选预约记录
- **Objective 4**: 实现取消预约功能
- **Objective 5**: 确保页面样式与现有系统一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-003（预约记录管理）
- 用户故事：Story 2

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建预约列表页面组件
- **ID**: TASK-006-REQ-001
- **Description**: 创建 `AppointmentList.vue` 组件
- **Location**: `web/qa-web/src/views/appointment/AppointmentList.vue`
- **Features**:
  - 显示预约记录列表
  - 支持按状态筛选
  - 支持查看预约详情
  - 支持取消预约

#### Requirement 2: 实现预约记录查询
- **ID**: TASK-006-REQ-002
- **Description**: 调用后端 API 获取预约记录
- **API Endpoint**: `GET /api/appointment/patient/{patientId}?status=xxx`
- **Implementation**:
  - 在组件创建时（`onMounted`）调用 API
  - 使用 `patientId` 从用户登录信息或路由参数获取
  - 将预约记录存储在响应式变量中

#### Requirement 3: 实现按状态筛选
- **ID**: TASK-006-REQ-003
- **Description**: 提供状态筛选功能
- **Status Options**: ALL, PENDING, CONFIRMED, COMPLETED, CANCELLED, MISSED
- **Implementation**:
  - 使用下拉菜单或标签页切换状态
  - 状态改变时重新调用 API（带 status 参数）
  - 默认显示所有状态

#### Requirement 4: 实现取消预约功能
- **ID**: TASK-006-REQ-004
- **Description**: 允许患者取消预约
- **API Endpoint**: `PUT /api/appointment/{appointmentNo}/cancel`
- **Implementation**:
  - 只为 PENDING 和 CONFIRMED 状态的预约显示取消按钮
  - 点击取消按钮时弹出确认对话框
  - 确认后调用取消 API
  - 成功后刷新列表

### 2.2 Technical Requirements

- **Framework**: Vue 3.5.10 (Composition API)
- **UI Library**: Ant Design Vue 4.2.6
- **HTTP Client**: Axios (通过封装的 API 模块)
- **State Management**: Pinia (如果需要用户状态)
- **Routing**: Vue Router 4

### 2.3 Constraints and Limitations

- **Constraint 1**: 页面样式需要与现有系统一致（参考 `Doctors.vue`, `Consultation.vue` 等）
- **Constraint 2**: 需要使用 Ant Design Vue 组件，不要使用原生 HTML 元素
- **Constraint 3**: 需要正确处理加载状态和错误状态
- **Constraint 4**: 需要考虑移动端适配（如果现有系统支持）

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **创建组件文件**: 在 `web/qa-web/src/views/appointment/` 目录创建 `AppointmentList.vue`
2. **实现模板**: 使用 Ant Design Vue 组件构建 UI
3. **实现脚本**: 使用 Composition API 实现业务逻辑
4. **实现样式**: 使用 CSS 或 SCSS，保持与现有页面一致
5. **配置路由**: 在 `router/index.ts` 中添加路由
6. **测试**: 手动测试页面功能

### 3.2 Implementation Steps

1. **Step 1**: 创建 `web/qa-web/src/views/appointment/AppointmentList.vue` 文件
   - 添加 `<template>` 部分：使用 `a-table` 展示预约列表
   - 添加筛选控件：使用 `a-tabs` 或 `a-select`
   - 添加操作按钮：查看详情、取消预约

2. **Step 2**: 实现 `<script setup>` 部分
   - 导入必要的依赖：`vue`, `vue-router`, `ant-design-vue`, axios
   - 定义响应式变量：`appointments`, `loading`, `selectedStatus`
   - 实现 `fetchAppointments()` 方法：调用后端 API
   - 实现 `cancelAppointment(appointmentNo)` 方法：调用取消 API
   - 实现 `viewAppointmentDetail(appointmentNo)` 方法：显示详情
   - 使用 `onMounted()` 在组件创建时获取预约列表

3. **Step 3**: 添加样式 `<style scoped>`
   - 保持与现有页面一致的样式
   - 参考 `Doctors.vue` 或 `Consultation.vue` 的样式

4. **Step 4**: 配置路由
   - 在 `web/qa-web/src/router/index.ts` 中添加路由
   - Path: `/appointment/list`
   - Component: `AppointmentList.vue`
   - Meta: `{ title: '我的预约' }`

5. **Step 5**: 创建 API 封装
   - 创建 `web/qa-web/src/api/appointment.ts`
   - 封装预约相关的 API 调用

**Validation Step**: 使用 Vite 开发服务器验证页面：
  - Command: `cd web/qa-web && npm run dev`
  - Expected: 页面成功加载，无编译错误，功能正常工作

### 3.3 Technical Considerations

- **考虑 1**: 使用 Composition API (`<script setup>`) 而不是 Options API
- **考虑 2**: 使用 Ant Design Vue 的 `a-table` 组件展示列表，支持分页、排序
- **考虑 3**: 使用 `a-modal` 或 `a-drawer` 展示预约详情
- **考虑 4**: 使用 `a-popconfirm` 实现取消预约的二次确认

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-project-structure.md`: 前端项目结构
- `.asdm/contexts/api.md`: API 端点设计
- 现有的视图组件：参考 `web/qa-web/src/views/Doctors.vue`

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `AppointmentList.vue` 组件成功创建
  - Test method: 检查文件是否存在
  - **Validation tool**: `find . -name "AppointmentList.vue"`

- **Criterion 2**: 页面成功调用后端 API 获取预约记录
  - Test method: 启动前端和后端，访问页面，查看网络请求
  - **Validation tool**: 浏览器开发者工具（Network 选项卡）

- **Criterion 3**: 按状态筛选功能正常工作
  - Test method: 切换状态筛选，查看列表更新
  - **Validation tool**: 手动测试

- **Criterion 4**: 取消预约功能正常工作
  - Test method: 点击取消按钮，确认，查看列表更新
  - **Validation tool**: 手动测试，查看后端数据库

- **Criterion 5**: 页面样式与现有系统一致
  - Test method: 视觉比对
  - **Validation tool**: 手动审查

### 4.2 Edge Cases

- **Edge case 1**: 预约记录为空
  - Expected behavior: 显示空状态提示（使用 `a-empty` 组件）

- **Edge case 2**: API 请求失败
  - Expected behavior: 显示错误提示（使用 `a-message` 或 `a-notification`）

- **Edge case 3**: 用户快速多次点击取消按钮
  - Expected behavior: 禁用按钮或添加加载状态，防止重复提交

### 4.3 Negative Tests

- **Negative test 1**: 后端 API 返回 500 错误
  - Expected behavior: 显示错误提示，不崩溃

- **Negative test 2**: 用户未登录（如果系统需要登录）
  - Expected behavior: 重定向到登录页面或显示未登录提示

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-004（后端：预约 API 接口实现）→ 需要后端 API 可用
- **Blocks**: TASK-008（前端：新增预约页面）→ 可能需要从列表页面跳转

### 5.2 External Dependencies

- **Dependency 1**: Ant Design Vue（已在项目中）
- **Dependency 2**: Axios（已在项目中）
- **Dependency 3**: 后端 API（由 TASK-004 提供）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-004 完成，后端预约 API 可用
- **Prerequisite 2**: 了解 Vue 3 Composition API
- **Prerequisite 3**: 了解 Ant Design Vue 组件

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 2 hours（人工开发工作量）
- **Complexity**: Medium
- **Risk**: Low

### 6.2 Effort Factors

- **Factor 1**: Vue 组件开发是常规任务，有标准模式
- **Factor 2**: 需要参考现有组件的实现风格
- **Factor 3**: 需要处理加载状态、错误状态等边界情况

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Build validation**:
  - Command: `cd web/qa-web && npm run build`
  - Success criteria: 构建成功，无错误

- **Linting** (如果已配置):
  - Command: `cd web/qa-web && npm run lint`
  - Success criteria: 无 lint 错误

### 7.2 Unit Testing

不适用（前端 UI 组件，主要通过手动测试或 E2E 测试）

### 7.3 Integration Testing

- **Test 1**: 测试页面加载和数据获取
  - Steps: 启动前端和后端 → 访问预约列表页面 → 查看数据是否加载
  - Success criteria: 数据显示正确

- **Test 2**: 测试取消预约功能
  - Steps: 创建测试预约 → 点击取消 → 确认 → 查看状态更新
  - Success criteria: 预约状态更新为 CANCELLED

### 7.4 Manual Testing

- **Manual test 1**: 测试所有功能
  - Steps: 访问页面 → 测试筛选 → 测试查看详情 → 测试取消预约
  - Success criteria: 所有功能按预期工作

- **Manual test 2**: 测试样式和响应式布局
  - Steps: 在不同屏幕尺寸下查看页面
  - Success criteria: 页面显示正常，无样式问题

---

## 8. Implementation Notes

### 8.1 代码示例

**AppointmentList.vue** (partial):
```vue
<template>
  <div class="appointment-list">
    <h2>我的预约</h2>

    <a-tabs v-model:activeKey="selectedStatus" @change="handleStatusChange">
      <a-tab-pane key="ALL" tab="全部" />
      <a-tab-pane key="PENDING" tab="待确认" />
      <a-tab-pane key="CONFIRMED" tab="已确认" />
      <a-tab-pane key="COMPLETED" tab="已完成" />
      <a-tab-pane key="CANCELLED" tab="已取消" />
    </a-tabs>

    <a-table
      :columns="columns"
      :data-source="appointments"
      :loading="loading"
      row-key="appointmentNo"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="getStatusColor(record.status)">
            {{ getStatusText(record.status) }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-button type="link" @click="viewDetail(record)">详情</a-button>
          <a-popconfirm
            v-if="record.status === 'PENDING' || record.status === 'CONFIRMED'"
            title="确定取消预约吗？"
            @confirm="cancelAppointment(record.appointmentNo)"
          >
            <a-button type="link" danger>取消</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { getAppointmentsByPatientId, cancelAppointment as cancelAppointmentApi } from '@/api/appointment';

const appointments = ref([]);
const loading = ref(false);
const selectedStatus = ref('ALL');

const columns = [
  { title: '预约单号', dataIndex: 'appointmentNo', key: 'appointmentNo' },
  { title: '医生姓名', dataIndex: 'doctorName', key: 'doctorName' },
  { title: '预约日期', dataIndex: 'appointmentDate', key: 'appointmentDate' },
  { title: '时间段', dataIndex: 'timeSlot', key: 'timeSlot' },
  { title: '地点', dataIndex: 'location', key: 'location' },
  { title: '状态', key: 'status' },
  { title: '操作', key: 'action' },
];

const fetchAppointments = async () => {
  loading.value = true;
  try {
    const status = selectedStatus.value === 'ALL' ? null : selectedStatus.value;
    const res = await getAppointmentsByPatientId(patientId.value, status);
    appointments.value = res.data;
  } catch (error) {
    message.error('获取预约记录失败');
  } finally {
    loading.value = false;
  }
};

const cancelAppointment = async (appointmentNo) => {
  try {
    await cancelAppointmentApi(appointmentNo);
    message.success('预约已取消');
    fetchAppointments(); // 刷新列表
  } catch (error) {
    message.error('取消预约失败');
  }
};

const viewDetail = (record) => {
  // 显示详情，可以使用 modal 或 drawer
};

const getStatusColor = (status) => {
  const colorMap = {
    'PENDING': 'orange',
    'CONFIRMED': 'blue',
    'COMPLETED': 'green',
    'CANCELLED': 'gray',
    'MISSED': 'red',
  };
  return colorMap[status] || 'default';
};

const getStatusText = (status) => {
  const textMap = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消',
    'MISSED': '未就诊',
  };
  return textMap[status] || status;
};

onMounted(() => {
  fetchAppointments();
});
</script>
```

### 8.2 注意事项

- **关于用户 ID**: 需要从登录信息或路由参数获取 `patientId`
- **关于状态显示**: 可以使用标签（`a-tag`）不同颜色显示不同状态
- **关于时间格式**: 使用 `dayjs` 或 `moment.js` 格式化日期

---

## 9. Risks and Mitigations

### Risk 1: 后端 API 尚未完成
- **Description**: 如果 TASK-004 尚未完成，前端无法测试
- **Impact**: Medium
- **Mitigation**: 使用 Mock 数据先开发前端，或等待后端完成

### Risk 2: 页面样式与现有系统不一致
- **Description**: 新页面可能看起来与现有页面不同
- **Impact**: Low
- **Mitigation**: 仔细参考现有页面的样式，保持一致性

---

## 10. Deliverables

- **Deliverable 1**: `AppointmentList.vue` 组件文件
- **Deliverable 2**: `appointment.ts` API 封装文件
- **Deliverable 3**: 路由配置更新

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（npm run build，退出码 0）
- **Manual test results**: 证据显示页面功能正常工作（截图或测试记录）

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
