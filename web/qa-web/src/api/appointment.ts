import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * 预约相关 API 封装
 */

/**
 * 获取预约列表
 * @param params 查询参数
 * @returns 预约列表
 */
export const getAppointmentList = (params: {
  userId?: string | number
  doctorId?: number
  status?: string
  startDate?: string
  endDate?: string
}) => {
  // 后端接口为 /api/appointment/patient/{patientId}
  if (params.userId) {
    return axios.get(`${API_BASE_URL}/appointment/patient/${params.userId}`, {
      params: { status: params.status }
    })
  }
  // 如果没有 userId，返回空数据
  return Promise.resolve({ data: { code: 200, data: [] } })
}

/**
 * 创建预约
 * @param data 预约信息
 * @returns 创建结果
 */
export const createAppointment = (data: {
  patientId: string
  doctorId: string
  appointmentDate: string
  timeSlot: string
  location?: string
  description?: string
}) => {
  return axios.post(`${API_BASE_URL}/appointment`, data)
}

/**
 * 取消预约
 * @param appointmentNo 预约单号
 * @returns 取消结果
 */
export const cancelAppointment = (appointmentNo: string) => {
  return axios.put(`${API_BASE_URL}/appointment/${appointmentNo}/cancel`)
}

/**
 * 获取预约详情
 * @param appointmentNo 预约单号
 * @returns 预约详情
 */
export const getAppointmentDetail = (appointmentNo: string) => {
  return axios.get(`${API_BASE_URL}/appointment/${appointmentNo}`)
}

/**
 * 排班相关 API 封装
 */

/**
 * 获取医生的排班列表
 * @param doctorId 医生ID
 * @returns 排班列表
 */
export const getSchedulesByDoctorId = (doctorId: number) => {
  return axios.get(`${API_BASE_URL}/schedule/doctor/${doctorId}`)
}

/**
 * 获取可用排班时间段
 * @param params 查询参数
 * @returns 可用排班列表
 */
export const getAvailableSchedules = (params: {
  doctorId?: string
  startDate?: string
  endDate?: string
}) => {
  return axios.get(`${API_BASE_URL}/schedule/available`, { params })
}

/**
 * 创建排班
 * @param data 排班信息
 * @returns 创建结果
 */
export const createSchedule = (data: {
  doctorId: number
  doctorName: string
  scheduleDate: string
  timeSlot: string
  location?: string
  maxAppointments?: number
}) => {
  return axios.post(`${API_BASE_URL}/schedule`, data)
}

/**
 * 更新排班
 * @param scheduleId 排班ID
 * @param data 排班信息
 * @returns 更新结果
 */
export const updateSchedule = (scheduleId: number, data: {
  scheduleDate?: string
  timeSlot?: string
  location?: string
  maxAppointments?: number
}) => {
  return axios.put(`${API_BASE_URL}/schedule/${scheduleId}`, data)
}

/**
 * 删除排班
 * @param scheduleId 排班ID
 * @returns 删除结果
 */
export const deleteSchedule = (scheduleId: number) => {
  return axios.delete(`${API_BASE_URL}/schedule/${scheduleId}`)
}

/**
 * 获取排班详情
 * @param scheduleId 排班ID
 * @returns 排班详情
 */
export const getScheduleById = (scheduleId: number) => {
  return axios.get(`${API_BASE_URL}/schedule/${scheduleId}`)
}
