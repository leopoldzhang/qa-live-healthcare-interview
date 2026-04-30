<template>
  <div class="add-appointment">
    <div class="page-header">
      <h2>新增预约</h2>
      <a-button @click="goBack">返回</a-button>
    </div>

    <!-- 步骤条 -->
    <a-steps v-model:current="currentStep" style="margin-bottom: 30px;">
      <a-step title="选择医生" />
      <a-step title="选择时间段" />
      <a-step title="填写信息" />
    </a-steps>

    <!-- 步骤1: 选择医生 -->
    <div v-if="currentStep === 0">
      <a-spin v-if="loadingDoctors" tip="加载医生列表中..." />
      <div v-else>
        <a-row :gutter="16">
          <a-col :span="6" v-for="doctor in doctors" :key="doctor.id">
            <a-card
              :hoverable="true"
              :class="{ 'selected-doctor': selectedDoctor?.id === doctor.id }"
              @click="selectDoctor(doctor)"
              style="margin-bottom: 16px;"
            >
              <div style="display: flex; align-items: center;">
                <a-avatar :src="doctor.avatar" size="large" style="margin-right: 12px;" />
                <div>
                  <h3 style="margin: 0;">{{ doctor.name }}</h3>
                  <p style="margin: 4px 0; color: #666;">{{ doctor.title }} - {{ doctor.department }}</p>
                  <a-tag v-for="spec in doctor.specialties" :key="spec" color="blue">{{ spec }}</a-tag>
                </div>
              </div>
            </a-card>
          </a-col>
        </a-row>
        <a-empty v-if="doctors.length === 0" description="暂无可用医生" />
      </div>
    </div>

    <!-- 步骤2: 选择时间段 -->
    <div v-if="currentStep === 1">
      <a-button @click="currentStep = 0" style="margin-bottom: 16px;">上一步</a-button>
      <a-spin v-if="loadingSchedules" tip="加载可用时间段..." />
      <div v-else>
        <a-date-picker
          v-model:value="selectedDate"
          :disabled-date="disabledDate"
          @change="loadSchedules"
          style="margin-bottom: 16px;"
        />
        <a-table
          :columns="scheduleColumns"
          :data-source="schedules"
          :row-key="(record: any) => record.id"
          :pagination="{ pageSize: 10 }"
          v-if="schedules.length > 0"
        >
          <template #bodyCell="{ column, record }: { column: any; record: any }">
            <template v-if="column.key === 'timeSlot'">
              <a-tag :color="getSlotColor(record.timeSlot)">{{ record.timeSlot }}</a-tag>
            </template>
            <template v-if="column.key === 'action'">
              <a-button type="primary" size="small" @click="selectSchedule(record)">选择</a-button>
            </template>
          </template>
        </a-table>
        <a-empty v-if="schedules.length === 0 && !loadingSchedules" description="该日期无可用时间段" />
      </div>
    </div>

    <!-- 步骤3: 填写信息 -->
    <div v-if="currentStep === 2">
      <a-button @click="currentStep = 1" style="margin-bottom: 16px;">上一步</a-button>
      <a-card title="确认预约信息" style="margin-top: 16px;">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="医生">{{ selectedDoctor?.name }}</a-descriptions-item>
          <a-descriptions-item label="科室">{{ selectedDoctor?.department }}</a-descriptions-item>
          <a-descriptions-item label="职称">{{ selectedDoctor?.title }}</a-descriptions-item>
          <a-descriptions-item label="预约日期">{{ selectedSchedule?.scheduleDate }}</a-descriptions-item>
          <a-descriptions-item label="时间段">{{ selectedSchedule?.timeSlot }}</a-descriptions-item>
          <a-descriptions-item label="地点">{{ selectedSchedule?.location }}</a-descriptions-item>
        </a-descriptions>

        <a-divider />

        <a-form :model="formData" layout="vertical">
          <a-form-item label="患者姓名" required>
            <a-input v-model:value="formData.patientName" placeholder="请输入患者姓名" />
          </a-form-item>
          <a-form-item label="联系电话" required>
            <a-input v-model:value="formData.patientPhone" placeholder="请输入联系电话" />
          </a-form-item>
          <a-form-item label="症状描述">
            <a-textarea v-model:value="formData.symptomDescription" placeholder="请描述您的症状" :rows="4" />
          </a-form-item>
        </a-form>

        <div style="text-align: center; margin-top: 24px;">
          <a-button type="primary" @click="submitAppointment" :loading="submitting">
            确认预约
          </a-button>
        </div>
      </a-card>
    </div>

    <!-- 错误提示 -->
    <a-alert v-if="error" type="error" :message="error" show-icon style="margin-top: 20px;" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { doctorApi, type Doctor } from '../../api/doctor'
import { getAvailableSchedules, createAppointment } from '../../api/appointment'

const router = useRouter()

// 步骤控制
const currentStep = ref(0)

// 医生相关
const doctors = ref<Doctor[]>([])
const loadingDoctors = ref(false)
const selectedDoctor = ref<Doctor | null>(null)

// 排班相关
const schedules = ref<any[]>([])
const loadingSchedules = ref(false)
const selectedDate = ref<any>(null)
const selectedSchedule = ref<any>(null)

// 表单数据
const formData = ref({
  patientName: '',
  patientPhone: '',
  symptomDescription: ''
})

// 提交状态
const submitting = ref(false)
const error = ref('')

// 表格列定义
const scheduleColumns = [
  { title: '日期', dataIndex: 'scheduleDate', key: 'scheduleDate' },
  { title: '时间段', dataIndex: 'timeSlot', key: 'timeSlot' },
  { title: '地点', dataIndex: 'location', key: 'location' },
  { title: '可预约数', dataIndex: 'availableSlots', key: 'availableSlots' },
  { title: '操作', key: 'action' }
]

// 获取活跃医生列表
const fetchDoctors = async () => {
  loadingDoctors.value = true
  error.value = ''

  try {
    const result = await doctorApi.getActiveDoctors()
    doctors.value = result
  } catch (err: any) {
    error.value = err.message || '获取医生列表失败'
  } finally {
    loadingDoctors.value = false
  }
}

// 选择医生
const selectDoctor = (doctor: Doctor) => {
  selectedDoctor.value = doctor
  currentStep.value = 1
  selectedDate.value = null
  schedules.value = []
}

// 禁用过去的日期
const disabledDate = (current: any) => {
  return current && current < dayjs().startOf('day')
}

// 加载可用排班
const loadSchedules = async () => {
  if (!selectedDoctor.value || !selectedDate.value) return

  loadingSchedules.value = true
  error.value = ''

  try {
    const params = {
      doctorId: selectedDoctor.value.id,  // 后端接受字符串 ID
      startDate: selectedDate.value.format('YYYY-MM-DD'),
      endDate: selectedDate.value.format('YYYY-MM-DD')
    }

    const result = await getAvailableSchedules(params)
    if (result.data && result.data.code === 200) {
      schedules.value = result.data.data || []
    } else {
      error.value = result.data?.message || '获取可用时间段失败'
    }
  } catch (err: any) {
    error.value = err.message || '网络错误'
  } finally {
    loadingSchedules.value = false
  }
}

// 选择排班
const selectSchedule = (schedule: any) => {
  selectedSchedule.value = schedule
  currentStep.value = 2

  // 预填充患者信息
  const patientStr = localStorage.getItem('currentPatient')
  if (patientStr) {
    try {
      const patient = JSON.parse(patientStr)
      formData.value.patientName = patient.name || ''
      formData.value.patientPhone = patient.phone || ''
    } catch {}
  }
}

// 获取时间段颜色
const getSlotColor = (timeSlot: string) => {
  if (timeSlot.includes('上午')) return 'green'
  if (timeSlot.includes('下午')) return 'blue'
  if (timeSlot.includes('晚上')) return 'purple'
  return 'default'
}

// 提交预约
const submitAppointment = async () => {
  if (!formData.value.patientName || !formData.value.patientPhone) {
    message.error('请填写完整信息')
    return
  }

  if (!selectedDoctor.value || !selectedSchedule.value) {
    message.error('请选择医生和时间段')
    return
  }

  submitting.value = true
  error.value = ''

  try {
    const patientStr = localStorage.getItem('currentPatient')
    let userId = ''
    if (patientStr) {
      try {
        const patient = JSON.parse(patientStr)
        userId = patient.id  // 字符串 ID
      } catch {}
    }

    if (!userId) {
      error.value = '请先登录'
      submitting.value = false
      return
    }

    const data = {
      patientId: userId,  // 后端使用 patientId
      doctorId: selectedDoctor.value.id,
      appointmentDate: selectedSchedule.value.scheduleDate,
      timeSlot: selectedSchedule.value.timeSlot,
      location: selectedSchedule.value.location || '',
      description: formData.value.symptomDescription || ''
    }

    const result = await createAppointment(data)
    if (result.data && result.data.code === 200) {
      message.success('预约成功！')
      router.push('/appointments')
    } else {
      error.value = result.data?.message || '预约失败'
    }
  } catch (err: any) {
    error.value = err.message || '网络错误，请稍后重试'
  } finally {
    submitting.value = false
  }
}

// 返回
const goBack = () => {
  router.back()
}

// 初始化
onMounted(() => {
  fetchDoctors()
})
</script>

<style scoped>
.add-appointment {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.selected-doctor {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}
</style>
