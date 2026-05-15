export interface Doctor {
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

const API_BASE_URL = 'http://localhost:8080/api';

export const doctorApi = {
  async getAllDoctors(): Promise<Doctor[]> {
    const response = await fetch(`${API_BASE_URL}/doctors`);
    const result = await response.json();

    if (result.code === 200 && result.data) {
      return result.data;
    }
    throw new Error(result.message || 'Failed to fetch doctors');
  },

  async getActiveDoctors(): Promise<Doctor[]> {
    const response = await fetch(`${API_BASE_URL}/doctors/active`);
    const result = await response.json();

    if (result.code === 200 && result.data) {
      return result.data;
    }
    throw new Error(result.message || 'Failed to fetch active doctors');
  },

  async getDoctorByUsername(username: string): Promise<Doctor> {
    const response = await fetch(`${API_BASE_URL}/doctors/${username}`);
    const result = await response.json();

    if (result.code === 200 && result.data) {
      return result.data;
    }
    throw new Error(result.message || 'Failed to fetch doctor');
  }
};
