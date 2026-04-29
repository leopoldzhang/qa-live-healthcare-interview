<template>
  <div class="appointment-list">
    <h2 class="page-title">我的预约</h2>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <div class="filter-left">
        <label>状态筛选：</label>
        <select v-model="filterStatus" @change="handleFilter">
          <option value="">全部</option>
          <option value="PENDING">待确认</option>
          <option value="CONFIRMED">已确认</option>
          <option value="COMPLETED">已完成</option>
          <option value="CANCELLED">已取消</option>
        </select>
      </div>
      <a-button type="primary" @click="goToAdd">
        新增预约
      </a-button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 错误提示 -->
    <div v-if="error" class="error-message">{{ error }}</div>

    <!-- 预约列表 -->
    <div v-if="!loading && !error" class="appointment-table">
      <table v-if="appointments.length > 0">
        <thead>
          <tr>
            <th>预约单号</th>
            <th>医生姓名</th>
            <th>预约日期</th>
            <th>时间段</th>
            <th>患者姓名</th>
            <th>地点</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appointment in appointments" :key="appointment.appointmentNo">
            <td>{{ appointment.appointmentNo }}</td>
            <td>{{ appointment.doctorName }}</td>
            <td>{{ formatDate(appointment.appointmentDate) }}</td>
            <td>{{ appointment.timeSlot }}</td>
            <td>{{ appointment.patientName }}</td>
            <td>{{ appointment.location || '-' }}</td>
            <td>
              <span :class="['status-badge', getStatusClass(appointment.status)]">
                {{ getStatusText(appointment.status) }}
              </span>
            </td>
            <td>
              <button
                v-if="appointment.status === 'PENDING' || appointment.status === 'CONFIRMED'"
                class="btn-cancel"
                @click="handleCancel(appointment.appointmentNo)"
              >
                取消预约
              </button>
              <span v-else>-</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无预约记录</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { getAppointmentList, cancelAppointment } from '../../api/appointment'

const router = useRouter()
const route = useRoute()

// 跳转到新增预约页面
const goToAdd = () => {
  router.push('/appointments/add')
}

const appointments = ref<any[]>([])
const loading = ref(false)
const error = ref('')
const filterStatus = ref('')

// 获取当前用户ID
const getCurrentUserId = () => {
  const patientStr = localStorage.getItem('currentPatient')
  if (patientStr) {
    try {
      const patient = JSON.parse(patientStr)
      return patient.id  // 返回字符串 ID，如 "patient123"
    } catch {
      return null
    }
  }
  return null
}

// 获取预约列表
const fetchAppointments = async () => {
  loading.value = true
  error.value = ''

  try {
    const userId = getCurrentUserId()
    if (!userId) {
      error.value = '请先登录'
      loading.value = false
      // 重定向到登录页
      setTimeout(() => {
        router.push('/patient/login?redirect=/appointments')
      }, 1500)
      return
    }

    console.log('正在获取预约列表，userId:', userId, 'status:', filterStatus.value)
    // 直接调用后端接口 /api/appointment/patient/{patientId}
    const result = await getAppointmentList({ userId, status: filterStatus.value })
    console.log('获取预约列表结果:', result.data)
    if (result.data && result.data.code === 200) {
      appointments.value = result.data.data || []
    } else {
      error.value = result.data?.message || '获取预约列表失败'
    }
  } catch (err: any) {
    console.error('获取预约列表失败:', err)
    error.value = err.response?.data?.message || err.message || '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 筛选处理
const handleFilter = () => {
  fetchAppointments()
}

// 取消预约
const handleCancel = async (appointmentNo: string) => {
  if (!confirm('确定要取消此预约吗？')) {
    return
  }

  try {
    const result = await cancelAppointment(appointmentNo)
    if (result.data && result.data.code === 200) {
      message.success('预约已取消')
      fetchAppointments() // 刷新列表
    } else {
      message.error(result.data?.message || '取消预约失败')
    }
  } catch (err: any) {
    message.error(err.message || '网络错误，请稍后重试')
  }
}

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

// 获取状态样式类
const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    PENDING: 'status-pending',
    CONFIRMED: 'status-confirmed',
    COMPLETED: 'status-completed',
    CANCELLED: 'status-cancelled'
  }
  return classMap[status] || ''
}

// 获取状态文本
const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    PENDING: '待确认',
    CONFIRMED: '已确认',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return textMap[status] || status
}

onMounted(() => {
  fetchAppointments()
})
</script>

<style scoped>
.appointment-list {
  padding: 20px;
}

.page-title {
  color: #2c3e50;
  font-size: 24px;
  margin-bottom: 20px;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.filter-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-left label {
  font-weight: 500;
  color: #2c3e50;
}

.filter-left select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.appointment-table {
  overflow-x: auto;
}

.appointment-table table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.appointment-table th,
.appointment-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.appointment-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #2c3e50;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
}

.status-confirmed {
  background: #d1ecf1;
  color: #0c5460;
}

.status-completed {
  background: #d4edda;
  color: #155724;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.btn-cancel {
  padding: 6px 12px;
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-cancel:hover {
  background: #c82333;
}

.loading {
  text-align: center;
  padding: 40px;
  color: #666;
}

.error-message {
  padding: 12px;
  background: #f8d7da;
  color: #721c24;
  border-radius: 4px;
  margin-bottom: 20px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 16px;
}
</style>
