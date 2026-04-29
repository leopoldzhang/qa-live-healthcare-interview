# Task PRD: 前端：医生排班页面（医生端）

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-007
**Created Date**: 2026-04-29
**Status**: TODO
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在创建医生管理自己排班时间的页面，包括查看排班、添加排班、编辑排班、删除排班等功能。

**任务目标**：
- 创建 `DoctorSchedule.vue` 组件
- 实现排班查询和展示
- 实现添加/编辑/删除排班功能
- 实现排班日历视图（可选）

**必要性**：
- 医生需要设置自己的可用时间段
- 排班数据是患者预约的基础
- 医生需要能够管理自己的排班

### 1.2 Task Objectives

- **Objective 1**: 创建医生排班管理页面 Vue 组件
- **Objective 2**: 实现调用后端 API 获取排班记录
- **Objective 3**: 实现添加和编辑排班功能
- **Objective 4**: 实现删除排班功能
- **Objective 5**: 确保页面样式与现有系统一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-001（医生排班管理）
- 用户故事：Story 3

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建医生排班页面组件
- **ID**: TASK-007-REQ-001
- **Description**: 创建 `DoctorSchedule.vue` 组件
- **Location**: `web/qa-web/src/views/appointment/DoctorSchedule.vue`
- **Features**:
  - 显示排班列表（按日期排序）
  - 支持按日期范围筛选
  - 支持添加新排班
  - 支持编辑现有排班
  - 支持删除排班

#### Requirement 2: 实现排班查询
- **ID**: TASK-007-REQ-002
- **Description**: 调用后端 API 获取医生的排班记录
- **API Endpoint**: `GET /api/schedule/doctor/{doctorId}`
- **Implementation**:
  - 在组件创建时调用 API
  - 使用 `doctorId` 从用户登录信息获取
  - 将排班记录存储在响应式变量中

#### Requirement 3: 实现添加排班功能
- **ID**: TASK-007-REQ-003
- **Description**: 允许医生添加新的排班时间段
- **API Endpoint**: `POST /api/schedule`
- **Implementation**:
  - 点击"添加排班"按钮，显示模态框（`a-modal`）
  - 填写排班信息（日期、时间段、地点、最大预约数）
  - 提交时调用 API
  - 成功后刷新列表

#### Requirement 4: 实现编辑排班功能
- **ID**: TASK-007-REQ-004
- **Description**: 允许医生编辑现有的排班
- **API Endpoint**: `PUT /api/schedule/{scheduleId}`
- **Implementation**:
  - 点击编辑按钮，显示编辑模态框
  - 预填充现有排班信息
  - 提交时调用更新 API
  - 成功后刷新列表

#### Requirement 5: 实现删除排班功能
- **ID**: TASK-007-REQ-005
- **Description**: 允许医生删除排班
- **API Endpoint**: `DELETE /api/schedule/{scheduleId}`
- **Implementation**:
  - 点击删除按钮，弹出确认对话框（`a-popconfirm`）
  - 确认后调用删除 API
  - 成功后刷新列表

### 2.2 Technical Requirements

- **Framework**: Vue 3.5.10 (Composition API)
- **UI Library**: Ant Design Vue 4.2.6
- **HTTP Client**: Axios (通过封装的 API 模块)
- **Routing**: Vue Router 4

### 2.3 Constraints and Limitations

- **Constraint 1**: 页面样式需要与现有系统一致
- **Constraint 2**: 需要使用 Ant Design Vue 组件
- **Constraint 3**: 医生只能操作自己的排班（权限检查）
- **Constraint 4**: 需要考虑移动端适配

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **创建组件文件**: 在 `web/qa-web/src/views/appointment/` 目录创建 `DoctorSchedule.vue`
2. **实现模板**: 使用 Ant Design Vue 组件构建 UI
3. **实现脚本**: 使用 Composition API 实现业务逻辑
4. **实现样式**: 使用 CSS，保持与现有页面一致
5. **配置路由**: 在 `router/index.ts` 中添加路由
6. **测试**: 手动测试页面功能

### 3.2 Implementation Steps

1. **Step 1**: 创建 `web/qa-web/src/views/appointment/DoctorSchedule.vue` 文件
   - 添加 `<template>` 部分：使用 `a-table` 展示排班列表
   - 添加操作按钮：添加、编辑、删除
   - 添加模态框：用于添加/编辑排班

2. **Step 2**: 实现 `<script setup>` 部分
   - 导入必要的依赖：`vue`, `vue-router`, `ant-design-vue`, axios
   - 定义响应式变量：`schedules`, `loading`, `modalVisible`, `formData`
   - 实现 `fetchSchedules()` 方法：调用后端 API
   - 实现 `addSchedule()` 方法：显示添加模态框
   - 实现 `editSchedule(record)` 方法：显示编辑模态框
   - 实现 `handleSubmit()` 方法：提交添加/编辑表单
   - 实现 `deleteSchedule(scheduleId)` 方法：删除排班
   - 使用 `onMounted()` 在组件创建时获取排班列表

3. **Step 3**: 添加样式 `<style scoped>`
   - 保持与现有页面一致的样式

4. **Step 4**: 配置路由
   - 在 `web/qa-web/src/router/index.ts` 中添加路由
   - Path: `/appointment/schedule`
   - Component: `DoctorSchedule.vue`
   - Meta: `{ title: '我的排班' }`

**Validation Step**: 使用 Vite 开发服务器验证页面：
  - Command: `cd web/qa-web && npm run dev`
  - Expected: 页面成功加载，无编译错误，功能正常工作

### 3.3 Technical Considerations

- **考虑 1**: 使用 Composition API (`<script setup>`)
- **考虑 2**: 使用 `a-modal` 组件实现添加/编辑排班的表单
- **考虑 3**: 使用 `a-form` 和 `a-form-item` 构建表单
- **考虑 4**: 使用 `a-popconfirm` 实现删除确认

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-project-structure.md`: 前端项目结构
- `.asdm/contexts/api.md`: API 端点设计
- 现有的视图组件：参考 `web/qa-web/src/views/Doctors.vue`

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `DoctorSchedule.vue` 组件成功创建
  - Test method: 检查文件是否存在
  - **Validation tool**: `find . -name "DoctorSchedule.vue"`

- **Criterion 2**: 页面成功调用后端 API 获取排班记录
  - Test method: 启动前端和后端，访问页面，查看网络请求
  - **Validation tool**: 浏览器开发者工具（Network 选项卡）

- **Criterion 3**: 添加排班功能正常工作
  - Test method: 点击添加按钮，填写表单，提交
  - **Validation tool**: 手动测试，查看后端数据库

- **Criterion 4**: 编辑和删除排班功能正常工作
  - Test method: 点击编辑/删除按钮，确认操作
  - **Validation tool**: 手动测试，查看后端数据库

- **Criterion 5**: 页面样式与现有系统一致
  - Test method: 视觉比对
  - **Validation tool**: 手动审查

### 4.2 Edge Cases

- **Edge case 1**: 排班记录为空
  - Expected behavior: 显示空状态提示

- **Edge case 2**: API 请求失败
  - Expected behavior: 显示错误提示

- **Edge case 3**: 添加排班时，时间冲突
  - Expected behavior: 后端返回错误，前端显示提示

### 4.3 Negative Tests

- **Negative test 1**: 后端 API 返回 500 错误
  - Expected behavior: 显示错误提示，不崩溃

- **Negative test 2**: 表单验证失败
  - Expected behavior: 显示验证错误提示

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-005（后端：排班 API 接口实现）→ 需要后端 API 可用
- **Blocks**: TASK-009（测试：单元测试和集成测试）→ 需要完成后的代码进行测试

### 5.2 External Dependencies

- **Dependency 1**: Ant Design Vue（已在项目中）
- **Dependency 2**: Axios（已在项目中）
- **Dependency 3**: 后端 API（由 TASK-005 提供）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-005 完成，后端排班 API 可用
- **Prerequisite 2**: 了解 Vue 3 Composition API
- **Prerequisite 3**: 了解 Ant Design Vue 组件

---

## 6. Estimated Effort

### 6.1 Effort Estimate

- **Estimated effort**: 2 hours（人工开发工作量）
- **Complexity**: Medium
- **Risk**: Low

### 6.2 Effort Factors

- **Factor 1**: Vue 组件开发是常规任务
- **Factor 2**: 需要处理表单验证、模态框等交互
- **Factor 3**: 需要参考现有组件的实现风格

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Build validation**:
  - Command: `cd web/qa-web && npm run build`
  - Success criteria: 构建成功，无错误

### 7.2 Integration Testing

- **Test 1**: 测试页面加载和数据获取
  - Steps: 启动前端和后端 → 访问排班页面 → 查看数据是否加载
  - Success criteria: 数据显示正确

- **Test 2**: 测试添加排班功能
  - Steps: 点击添加 → 填写表单 → 提交 → 查看列表更新
  - Success criteria: 排班成功添加

### 7.3 Manual Testing

- **Manual test 1**: 测试所有功能
  - Steps: 访问页面 → 测试添加 → 测试编辑 → 测试删除
  - Success criteria: 所有功能按预期工作

---

## 8. Implementation Notes

### 8.1 代码示例

**DoctorSchedule.vue** (partial):
```vue
<template>
  <div class="doctor-schedule">
    <h2>我的排班</h2>

    <a-button type="primary" @click="showAddModal">添加排班</a-button>

    <a-table
      :columns="columns"
      :data-source="schedules"
      :loading="loading"
      row-key="id"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-button type="link" @click="showEditModal(record)">编辑</a-button>
          <a-popconfirm
            title="确定删除该排班吗？"
            @confirm="deleteSchedule(record.id)"
          >
            <a-button type="link" danger>删除</a-button>
          </a-popconfirm>
        </template>
      </template>
    </a-table>

    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleSubmit"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="排班日期" required>
          <a-date-picker v-model:value="formData.scheduleDate" />
        </a-form-item>
        <a-form-item label="时间段" required>
          <a-input v-model:value="formData.timeSlot" placeholder="如：09:00-09:30" />
        </a-form-item>
        <a-form-item label="地点">
          <a-input v-model:value="formData.location" />
        </a-form-item>
        <a-form-item label="最大预约数">
          <a-input-number v-model:value="formData.maxAppointments" :min="1" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { getSchedulesByDoctorId, createSchedule, updateSchedule, deleteSchedule as deleteScheduleApi } from '@/api/appointment';

const schedules = ref([]);
const loading = ref(false);
const modalVisible = ref(false);
const modalTitle = ref('添加排班');
const formData = reactive({
  id: null,
  scheduleDate: null,
  timeSlot: '',
  location: '',
  maxAppointments: 1,
});

const columns = [
  { title: '排班日期', dataIndex: 'scheduleDate', key: 'scheduleDate' },
  { title: '时间段', dataIndex: 'timeSlot', key: 'timeSlot' },
  { title: '地点', dataIndex: 'location', key: 'location' },
  { title: '最大预约数', dataIndex: 'maxAppointments', key: 'maxAppointments' },
  { title: '当前预约数', dataIndex: 'currentAppointments', key: 'currentAppointments' },
  { title: '操作', key: 'action' },
];

const fetchSchedules = async () => {
  loading.value = true;
  try {
    const res = await getSchedulesByDoctorId(doctorId.value);
    schedules.value = res.data;
  } catch (error) {
    message.error('获取排班记录失败');
  } finally {
    loading.value = false;
  }
};

const showAddModal = () => {
  modalTitle.value = '添加排班';
  resetFormData();
  modalVisible.value = true;
};

const showEditModal = (record) => {
  modalTitle.value = '编辑排班';
  Object.assign(formData, record);
  modalVisible.value = true;
};

const handleSubmit = async () => {
  try {
    if (formData.id) {
      await updateSchedule(formData.id, formData);
      message.success('排班更新成功');
    } else {
      await createSchedule(formData);
      message.success('排班添加成功');
    }
    modalVisible.value = false;
    fetchSchedules();
  } catch (error) {
    message.error('操作失败');
  }
};

const deleteSchedule = async (scheduleId) => {
  try {
    await deleteScheduleApi(scheduleId);
    message.success('排班已删除');
    fetchSchedules();
  } catch (error) {
    message.error('删除失败');
  }
};

const resetFormData = () => {
  formData.id = null;
  formData.scheduleDate = null;
  formData.timeSlot = '';
  formData.location = '';
  formData.maxAppointments = 1;
};

onMounted(() => {
  fetchSchedules();
});
</script>
```

### 8.2 注意事项

- **关于医生 ID**: 需要从登录信息获取 `doctorId`
- **关于日期选择**: 使用 `a-date-picker` 组件，注意日期格式转换
- **关于表单验证**: 可以使用 `a-form` 的验证功能

---

## 9. Risks and Mitigations

### Risk 1: 后端 API 尚未完成
- **Description**: 如果 TASK-005 尚未完成，前端无法测试
- **Impact**: Medium
- **Mitigation**: 使用 Mock 数据先开发前端，或等待后端完成

### Risk 2: 页面样式与现有系统不一致
- **Description**: 新页面可能看起来与现有页面不同
- **Impact**: Low
- **Mitigation**: 仔细参考现有页面的样式

---

## 10. Deliverables

- **Deliverable 1**: `DoctorSchedule.vue` 组件文件
- **Deliverable 2**: 路由配置更新

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（npm run build，退出码 0）
- **Manual test results**: 证据显示页面功能正常工作（截图或测试记录）

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
