# Task PRD: 前端：新增预约页面

**Feature ID**: FEAT-001-Appointment-Registration
**Feature Name**: 预约挂号功能
**Task ID**: TASK-008
**Created Date**: 2026-04-29
**Status**: DONE
**Language**: zh (中文)

---

## 1. Task Overview

### 1.1 Task Summary

本任务旨在创建患者预约医生的新增预约页面，包括选择医生、选择可用时间段、填写预约信息等功能。

**任务目标**：
- 创建 `AddAppointment.vue` 组件
- 实现医生列表展示和选择
- 实现可用时间段查询和选择
- 实现预约信息填写和提交

**必要性**：
- 这是预约功能的核心页面
- 患者需要能够方便地预约医生
- 页面需要引导患者完成预约流程

### 1.2 Task Objectives

- **Objective 1**: 创建新增预约页面 Vue 组件
- **Objective 2**: 实现医生列表展示和选择功能
- **Objective 3**: 实现可用时间段查询和选择
- **Objective 4**: 实现预约信息填写和提交
- **Objective 5**: 确保页面样式与现有系统一致

### 1.3 Related Feature Requirements

- 功能需求：REQ-002（患者预约功能）
- 功能需求：REQ-004（可用时间段查询）
- 用户故事：Story 1

---

## 2. Detailed Requirements

### 2.1 Functional Requirements

#### Requirement 1: 创建新增预约页面组件
- **ID**: TASK-008-REQ-001
- **Description**: 创建 `AddAppointment.vue` 组件
- **Location**: `web/qa-web/src/views/appointment/AddAppointment.vue`
- **Features**:
  - 医生列表展示
  - 可用时间段选择
  - 预约信息填写
  - 预约提交

#### Requirement 2: 实现医生列表展示
- **ID**: TASK-008-REQ-002
- **Description**: 展示可预约的医生列表
- **API Endpoint**: `GET /api/doctor/list` (需要确认现有 API)
- **Implementation**:
  - 使用 `a-card` 或 `a-list` 展示医生列表
  - 显示医生姓名、科室、职称等信息
  - 点击医生后，查询该医生的可用时间段

#### Requirement 3: 实现可用时间段查询和选择
- **ID**: TASK-008-REQ-003
- **Description**: 根据选择的医生，查询可用时间段
- **API Endpoint**: `GET /api/schedule/available?doctorId=xxx`
- **Implementation**:
  - 选择医生后，调用 API 获取可用时间段
  - 按日期分组展示可用时间段
  - 显示每个时间段的剩余名额
  - 患者可以选择一个时间段

#### Requirement 4: 实现预约信息填写和提交
- **ID**: TASK-008-REQ-004
- **Description**: 填写预约信息并提交
- **API Endpoint**: `POST /api/appointment`
- **Implementation**:
  - 填写症状描述（可选）
  - 确认预约信息（医生、日期、时间段、地点）
  - 点击提交按钮，调用创建预约 API
  - 成功后显示预约单号，跳转到预约列表页面

### 2.2 Technical Requirements

- **Framework**: Vue 3.5.10 (Composition API)
- **UI Library**: Ant Design Vue 4.2.6
- **HTTP Client**: Axios (通过封装的 API 模块)
- **Routing**: Vue Router 4

### 2.3 Constraints and Limitations

- **Constraint 1**: 页面样式需要与现有系统一致
- **Constraint 2**: 需要使用 Ant Design Vue 组件
- **Constraint 3**: 需要正确处理加载状态和错误状态
- **Constraint 4**: 需要考虑移动端适配

---

## 3. Implementation Approach

### 3.1 Recommended Methodology

1. **创建组件文件**: 在 `web/qa-web/src/views/appointment/` 目录创建 `AddAppointment.vue`
2. **实现模板**: 使用 Ant Design Vue 组件构建 UI
3. **实现脚本**: 使用 Composition API 实现业务逻辑
4. **实现样式**: 使用 CSS，保持与现有页面一致
5. **配置路由**: 在 `router/index.ts` 中添加路由
6. **测试**: 手动测试页面功能

### 3.2 Implementation Steps

1. **Step 1**: 创建 `web/qa-web/src/views/appointment/AddAppointment.vue` 文件
   - 添加 `<template>` 部分：分步引导患者完成预约
   - 可以使用 `a-steps` 组件实现分步表单
   - 或使用单个页面，逐步显示不同内容

2. **Step 2**: 实现 `<script setup>` 部分
   - 导入必要的依赖：`vue`, `vue-router`, `ant-design-vue`, axios
   - 定义响应式变量：`selectedDoctor`, `availableSchedules`, `selectedSchedule`, `description`
   - 实现 `fetchDoctors()` 方法：获取医生列表
   - 实现 `fetchAvailableSchedules(doctorId)` 方法：获取可用时间段
   - 实现 `submitAppointment()` 方法：提交预约
   - 使用 `onMounted()` 在组件创建时获取医生列表

3. **Step 3**: 添加样式 `<style scoped>`
   - 保持与现有页面一致的样式

4. **Step 4**: 配置路由
   - 在 `web/qa-web/src/router/index.ts` 中添加路由
   - Path: `/appointment/add`
   - Component: `AddAppointment.vue`
   - Meta: `{ title: '新增预约' }`

**Validation Step**: 使用 Vite 开发服务器验证页面：
  - Command: `cd web/qa-web && npm run dev`
  - Expected: 页面成功加载，无编译错误，功能正常工作

### 3.3 Technical Considerations

- **考虑 1**: 可以使用分步表单（`a-steps`）引导患者完成预约
- **考虑 2**: 可用时间段可以按日期分组显示
- **考虑 3**: 提交时需要显示加载状态，防止重复提交
- **考虑 4**: 成功后可以显示预约单号，并提供查看详情的链接

### 3.4 Reference to Project Context

- `.asdm/contexts/standard-project-structure.md`: 前端项目结构
- `.asdm/contexts/api.md`: API 端点设计
- 现有的视图组件：参考 `web/qa-web/src/views/Consultation.vue`

---

## 4. Acceptance Criteria

### 4.1 Primary Criteria

- **Criterion 1**: `AddAppointment.vue` 组件成功创建
  - Test method: 检查文件是否存在
  - **Validation tool**: `find . -name "AddAppointment.vue"`

- **Criterion 2**: 医生列表成功展示
  - Test method: 启动前端和后端，访问页面，查看医生列表
  - **Validation tool**: 手动测试

- **Criterion 3**: 可用时间段查询和选择功能正常工作
  - Test method: 选择医生，查看可用时间段
  - **Validation tool**: 手动测试，查看网络请求

- **Criterion 4**: 预约提交功能正常工作
  - Test method: 填写信息，提交预约，查看结果
  - **Validation tool**: 手动测试，查看后端数据库

- **Criterion 5**: 页面样式与现有系统一致
  - Test method: 视觉比对
  - **Validation tool**: 手动审查

### 4.2 Edge Cases

- **Edge case 1**: 医生列表为空
  - Expected behavior: 显示空状态提示

- **Edge case 2**: 可用时间段为空
  - Expected behavior: 显示提示，建议患者选择其他医生或日期

- **Edge case 3**: 提交时网络错误
  - Expected behavior: 显示错误提示，不丢失已填写的信息

### 4.3 Negative Tests

- **Negative test 1**: 后端 API 返回 500 错误
  - Expected behavior: 显示错误提示，不崩溃

- **Negative test 2**: 提交预约时，时间段已被预约满
  - Expected behavior: 显示错误提示，建议选择其他时间段

---

## 5. Dependencies

### 5.1 Task Dependencies

- **Depends on**: TASK-004（后端：预约 API 接口实现）→ 需要预约 API 可用
- **Depends on**: TASK-005（后端：排班 API 接口实现）→ 需要可用时间段查询 API
- **Depends on**: TASK-006（前端：预约列表页面）→ 可能需要从新增页面跳转
- **Blocks**: TASK-009（测试：单元测试和集成测试）→ 需要完成后的代码进行测试

### 5.2 External Dependencies

- **Dependency 1**: Ant Design Vue（已在项目中）
- **Dependency 2**: Axios（已在项目中）
- **Dependency 3**: 后端 API（由 TASK-004 和 TASK-005 提供）

### 5.3 Prerequisites

- **Prerequisite 1**: TASK-004 和 TASK-005 完成，后端 API 可用
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
- **Factor 2**: 需要处理多步操作，逻辑稍复杂
- **Factor 3**: 需要参考现有组件的实现风格

---

## 7. Testing Strategy

### 7.1 Automated Validation (Required)

- **Build validation**:
  - Command: `cd web/qa-web && npm run build`
  - Success criteria: 构建成功，无错误

### 7.2 Integration Testing

- **Test 1**: 测试页面加载和医生列表
  - Steps: 启动前端和后端 → 访问新增预约页面 → 查看医生列表
  - Success criteria: 数据显示正确

- **Test 2**: 测试可用时间段查询
  - Steps: 选择医生 → 查看可用时间段
  - Success criteria: 时间段显示正确

- **Test 3**: 测试预约提交
  - Steps: 填写信息 → 提交预约 → 查看结果
  - Success criteria: 预约成功，数据库记录正确

### 7.3 Manual Testing

- **Manual test 1**: 测试所有功能
  - Steps: 访问页面 → 选择医生 → 选择时间段 → 填写信息 → 提交
  - Success criteria: 所有功能按预期工作

---

## 8. Implementation Notes

### 8.1 代码示例

**AddAppointment.vue** (partial):
```vue
<template>
  <div class="add-appointment">
    <h2>新增预约</h2>

    <!-- 步骤 1: 选择医生 -->
    <div v-if="currentStep === 0">
      <h3>选择医生</h3>
      <a-row :gutter="16">
        <a-col :span="8" v-for="doctor in doctors" :key="doctor.id">
          <a-card
            :class="{ 'selected': selectedDoctor?.id === doctor.id }"
            @click="selectDoctor(doctor)"
          >
            <h4>{{ doctor.name }}</h4>
            <p>{{ doctor.department }}</p>
            <p>{{ doctor.title }}</p>
          </a-card>
        </a-col>
      </a-row>
      <a-button type="primary" @click="currentStep = 1" :disabled="!selectedDoctor">
        下一步
      </a-button>
    </div>

    <!-- 步骤 2: 选择时间段 -->
    <div v-if="currentStep === 1">
      <h3>选择时间段</h3>
      <a-spin :spinning="loading">
        <div v-for="(schedules, date) in groupedSchedules" :key="date">
          <h4>{{ date }}</h4>
          <a-button
            v-for="schedule in schedules"
            :key="schedule.id"
            :type="selectedSchedule?.id === schedule.id ? 'primary' : 'default'"
            :disabled="schedule.remainingSlots === 0"
            @click="selectSchedule(schedule)"
          >
            {{ schedule.timeSlot }} (剩余: {{ schedule.remainingSlots }})
          </a-button>
        </div>
      </a-spin>
      <a-button @click="currentStep = 0">上一步</a-button>
      <a-button type="primary" @click="currentStep = 2" :disabled="!selectedSchedule">
        下一步
      </a-button>
    </div>

    <!-- 步骤 3: 填写信息并提交 -->
    <div v-if="currentStep === 2">
      <h3>确认预约信息</h3>
      <a-form layout="vertical">
        <a-form-item label="医生">
          {{ selectedDoctor.name }}
        </a-form-item>
        <a-form-item label="日期">
          {{ selectedSchedule.scheduleDate }}
        </a-form-item>
        <a-form-item label="时间段">
          {{ selectedSchedule.timeSlot }}
        </a-form-item>
        <a-form-item label="地点">
          {{ selectedSchedule.location }}
        </a-form-item>
        <a-form-item label="症状描述（可选）">
          <a-textarea v-model:value="description" />
        </a-form-item>
      </a-form>
      <a-button @click="currentStep = 1">上一步</a-button>
      <a-button type="primary" @click="submitAppointment" :loading="submitting">
        提交预约
      </a-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { getDoctorList } from '@/api/doctor';
import { getAvailableSchedules, createAppointment } from '@/api/appointment';

const router = useRouter();
const currentStep = ref(0);
const doctors = ref([]);
const selectedDoctor = ref(null);
const availableSchedules = ref([]);
const selectedSchedule = ref(null);
const description = ref('');
const loading = ref(false);
const submitting = ref(false);

const groupedSchedules = computed(() => {
  const grouped = {};
  availableSchedules.value.forEach(schedule => {
    const date = schedule.scheduleDate;
    if (!grouped[date]) {
      grouped[date] = [];
    }
    grouped[date].push({
      ...schedule,
      remainingSlots: schedule.maxAppointments - schedule.currentAppointments,
    });
  });
  return grouped;
});

const fetchDoctors = async () => {
  try {
    const res = await getDoctorList();
    doctors.value = res.data;
  } catch (error) {
    message.error('获取医生列表失败');
  }
};

const selectDoctor = async (doctor) => {
  selectedDoctor.value = doctor;
  selectedSchedule.value = null;
  loading.value = true;
  try {
    const res = await getAvailableSchedules(doctor.id);
    availableSchedules.value = res.data;
  } catch (error) {
    message.error('获取可用时间段失败');
  } finally {
    loading.value = false;
  }
};

const selectSchedule = (schedule) => {
  selectedSchedule.value = schedule;
};

const submitAppointment = async () => {
  submitting.value = true;
  try {
    const appointmentData = {
      patientId: patientId.value, // 从登录信息获取
      doctorId: selectedDoctor.value.id,
      appointmentDate: selectedSchedule.value.scheduleDate,
      timeSlot: selectedSchedule.value.timeSlot,
      location: selectedSchedule.value.location,
      description: description.value,
    };
    const res = await createAppointment(appointmentData);
    message.success('预约成功！预约单号：' + res.data.appointmentNo);
    router.push('/appointment/list'); // 跳转到预约列表页面
  } catch (error) {
    message.error('预约失败');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  fetchDoctors();
});
</script>
```

### 8.2 注意事项

- **关于用户 ID**: 需要从登录信息获取 `patientId`
- **关于步骤控制**: 可以使用 `a-steps` 组件显示当前步骤
- **关于时间格式**: 使用 `dayjs` 或 `moment.js` 格式化日期

---

## 9. Risks and Mitigations

### Risk 1: 后端 API 尚未完成
- **Description**: 如果 TASK-004 和 TASK-005 尚未完成，前端无法测试
- **Impact**: Medium
- **Mitigation**: 使用 Mock 数据先开发前端，或等待后端完成

### Risk 2: 页面逻辑复杂，容易出错
- **Description**: 多步操作的逻辑可能较复杂，状态管理容易出错
- **Impact**: Medium
- **Mitigation**: 仔细设计状态管理，使用 computed 属性简化逻辑

---

## 10. Deliverables

- **Deliverable 1**: `AddAppointment.vue` 组件文件
- **Deliverable 2**: 路由配置更新

**Mandatory Deliverable**: Validation Results
- **Build output**: 证据显示代码编译成功（npm run build，退出码 0）
- **Manual test results**: 证据显示页面功能正常工作（截图或测试记录）

---

**Document Version**: 1.0
**Last Updated**: 2026-04-29
