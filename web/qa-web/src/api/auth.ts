import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/auth/patient';

export interface Patient {
  id: string;
  username: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
  createdAt?: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface ApiResponse<T> {
  code: number;
  data: T;
  message: string;
}

// 患者注册
export const register = async (data: RegisterRequest): Promise<ApiResponse<Patient>> => {
  const response = await axios.post<ApiResponse<Patient>>(`${API_BASE_URL}/register`, data);
  return response.data;
};

// 患者登录
export const login = async (data: LoginRequest): Promise<ApiResponse<Patient>> => {
  const response = await axios.post<ApiResponse<Patient>>(`${API_BASE_URL}/login`, data);
  return response.data;
};

// 患者登出
export const logout = async (): Promise<ApiResponse<void>> => {
  const response = await axios.post<ApiResponse<void>>(`${API_BASE_URL}/logout`);
  return response.data;
};

// 检查用户名是否存在
export const checkUsername = async (username: string): Promise<ApiResponse<{ exists: boolean }>> => {
  const response = await axios.get<ApiResponse<{ exists: boolean }>>(
    `${API_BASE_URL}/check-username?username=${username}`
  );
  return response.data;
};

// 获取所有患者
export const getAllPatients = async (): Promise<ApiResponse<Patient[]>> => {
  const response = await axios.get<ApiResponse<Patient[]>>(API_BASE_URL);
  return response.data;
};

// 根据 ID 获取患者
export const getPatientById = async (id: string): Promise<ApiResponse<Patient>> => {
  const response = await axios.get<ApiResponse<Patient>>(`${API_BASE_URL}/${id}`);
  return response.data;
};
