<template>
  <div class="doctor-schedule">
    <div class="page-header">
      <h2>我的排班</h2>
      <a-button type="primary" @click="showAddModal">
        添加排班
      </a-button>
    </div>

    <!-- 加载状态 -->
    <a-spin v-if="loading" tip="加载中..." />

    <!-- 错误提示 -->
    <a-alert v-if="error" type="error" :message="error" show-icon style="margin-bottom: 20px;" />

    <!-- 排班表格 -->
    <a-table
      v-if="!loading && !error"
      :columns="columns"
      :data-source="schedules"
      :row-key="(record: any) => record.id"
      :pagination="{ pageSize: 10 }"
    >
      <template #bodyCell="{ column, record }: { column: any; record: any }">
        <template v-if="column.key === 'scheduleDate'">
          {{ formatDate(record.scheduleDate) }}
        </template>
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 'AVAILABLE' ? 'green' : 'red'">
            {{ record.status === 'AVAILABLE' ? '可用' : '不可用' }}
          </a-tag>
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" @click="showEditModal(record)">编辑</a-button>
            <a-popconfirm
              title="确定删除该排班吗？"
              ok-text="确定"
              cancel-text="取消"
              @confirm="handleDelete(record.id)"
            >
              <a-button type="link" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 空状态 -->
    <a-empty v-if="!loading && !error && schedules.length === 0" description="暂无排班记录" />

    <!-- 添加/编辑排班模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="modalTitle"
      @ok="handleSubmit"
      @cancel="handleCancel"
      :confirm-loading="submitLoading"
    >
      <a-form :model="formData" layout="vertical">
        <a-form-item label="排班日期" required>
          <a-date-picker
            v-model:value="formData.scheduleDate"
            style="width: 100%;"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </a-form-item>
        <a-form-item label="时间段" required>
          <a-input
            v-model:value="formData.timeSlot"
            placeholder="如：09:00-09:30"
          />
        </a-form-item>
        <a-form-item label="地点">
          <a-input
            v-model:value="formData.location"
            placeholder="请输入地点"
          />
        </a-form-item>
        <a-form-item label="最大预约数">
          <a-input-number
            v-model:value="formData.maxAppointments"
            :min="1"
            style="width: 100%;"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import axios from 'axios'
import dayjs from 'dayjs'

const API_BASE_URL = 'http://localhost:8080/api'

// 获取当前医生ID
const getCurrentDoctorId = () => {
  const doctorStr = localStorage.getItem('currentDoctor')
  if (doctorStr) {
    try {
      const doctor = JSON.parse(doctorStr)
      return doctor.id
    } catch {
      return null
    }
  }
  return null
}

// 表格列定义
const columns = [
  {
    title: '排班日期',
    dataIndex: 'scheduleDate',
    key: 'scheduleDate',
  },
  {
    title: '时间段',
    dataIndex: 'timeSlot',
    key: 'timeSlot',
  },
  {
    title: '地点',
    dataIndex: 'location',
    key: 'location',
  },
  {
    title: '最大预约数',
    dataIndex: 'maxAppointments',
    key: 'maxAppointments',
  },
  {
    title: '当前预约数',
    dataIndex: 'currentAppointments',
    key: 'currentAppointments',
  },
  {
    title: '状态',
    key: 'status',
  },
  {
    title: '操作',
    key: 'action',
  },
]

const schedules = ref<any[]>([])
const loading = ref(false)
const error = ref('')
const modalVisible = ref(false)
const modalTitle = ref('添加排班')
const submitLoading = ref(false)
const formData = reactive({
  id: null as number | null,
  scheduleDate: null as any,
  timeSlot: '',
  location: '',
  maxAppointments: 1,
})

// 获取排班列表
const fetchSchedules = async () => {
  loading.value = true
  error.value = ''

  try {
    const doctorId = getCurrentDoctorId()
    if (!doctorId) {
      error.value = '未找到医生信息，请重新登录'
      return
    }

    const response = await axios.get(`${API_BASE_URL}/schedule/doctor/${doctorId}`)
    if (response.data && response.data.code === 200) {
      schedules.value = response.data.data || []
    } else {
      error.value = response.data?.message || '获取排班列表失败'
    }
  } catch (err: any) {
    error.value = err.message || '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 显示添加模态框
const showAddModal = () => {
  modalTitle.value = '添加排班'
  resetFormData()
  modalVisible.value = true
}

// 显示编辑模态框
const showEditModal = (record: any) => {
  modalTitle.value = '编辑排班'
  formData.id = record.id
  formData.scheduleDate = dayjs(record.scheduleDate)
  formData.timeSlot = record.timeSlot
  formData.location = record.location || ''
  formData.maxAppointments = record.maxAppointments || 1
  modalVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formData.scheduleDate) {
    message.warning('请选择排班日期')
    return
  }
  if (!formData.timeSlot) {
    message.warning('请输入时间段')
    return
  }

  submitLoading.value = true

  try {
    const doctorId = getCurrentDoctorId()
    if (!doctorId) {
      message.error('未找到医生信息')
      return
    }

    const doctorStr = localStorage.getItem('currentDoctor')
    const doctor = doctorStr ? JSON.parse(doctorStr) : null

    const data = {
      doctorId,
      doctorName: doctor?.name || '',
      scheduleDate: dayjs(formData.scheduleDate).format('YYYY-MM-DD'),
      timeSlot: formData.timeSlot,
      location: formData.location,
      maxAppointments: formData.maxAppointments,
    }

    if (formData.id) {
      // 更新排班
      const response = await axios.put(`${API_BASE_URL}/schedule/${formData.id}`, data)
      if (response.data && response.data.code === 200) {
        message.success('排班更新成功')
      } else {
        message.error(response.data?.message || '更新失败')
        return
      }
    } else {
      // 创建排班
      const response = await axios.post(`${API_BASE_URL}/schedule`, data)
      if (response.data && response.data.code === 200) {
        message.success('排班添加成功')
      } else {
        message.error(response.data?.message || '添加失败')
        return
      }
    }

    modalVisible.value = false
    fetchSchedules() // 刷新列表
  } catch (err: any) {
    message.error(err.message || '操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 取消模态框
const handleCancel = () => {
  modalVisible.value = false
  resetFormData()
}

// 删除排班
const handleDelete = async (scheduleId: number) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/schedule/${scheduleId}`)
    if (response.data && response.data.code === 200) {
      message.success('排班已删除')
      fetchSchedules() // 刷新列表
    } else {
      message.error(response.data?.message || '删除失败')
    }
  } catch (err: any) {
    message.error(err.message || '删除失败')
  }
}

// 重置表单数据
const resetFormData = () => {
  formData.id = null
  formData.scheduleDate = null
  formData.timeSlot = ''
  formData.location = ''
  formData.maxAppointments = 1
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  return dayjs(dateStr).format('YYYY-MM-DD')
}

onMounted(() => {
  fetchSchedules()
})
</script>

<style scoped>
.doctor-schedule {
  padding: 84px 20px 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  color: #2c3e50;
  font-size: 24px;
  margin: 0;
}
</style>
