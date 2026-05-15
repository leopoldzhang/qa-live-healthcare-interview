import { reactive, ref } from 'vue';
import doctorData from '../data/doctor-user-list.json';
import patientData from '../data/patient-user.json';
import questionData from '../data/question-list.json';
import { doctorApi } from '../api/doctor';

export interface Doctor {
  id: string;
  username: string;
  password?: string;
  name: string;
  title: string;
  department: string;
  avatar: string;
  experience: string;
  specialties: string[];
  isActive: boolean;
}

export interface Patient {
  id: string;
  username?: string;
  name: string;
  birthday: string;
  phone: string;
  gender: string;
  createdAt?: string;
}

export interface Question {
  id: string;
  patientId: string;
  patientName: string;
  doctorId: string;
  doctorName: string;
  question: string;
  submitTime: string;
  status: 'pending' | 'answered';
  answer: string | null;
  answerTime: string | null;
}

interface State {
  doctors: Doctor[];
  patients: Patient[];
  questions: Question[];
  currentDoctor: Doctor | null;
  currentPatient: Patient | null;
}

const state = reactive<State>({
  doctors: [],
  patients: patientData as Patient[],
  questions: questionData as Question[],
  currentDoctor: null,
  currentPatient: null,
});

// 从 localStorage 恢复登录状态
const savedPatient = localStorage.getItem('currentPatient');
if (savedPatient) {
  try {
    state.currentPatient = JSON.parse(savedPatient);
  } catch (error) {
    console.error('Failed to parse saved patient:', error);
  }
}

const savedDoctor = localStorage.getItem('currentDoctor');
if (savedDoctor) {
  try {
    state.currentDoctor = JSON.parse(savedDoctor);
  } catch (error) {
    console.error('Failed to parse saved doctor:', error);
  }
}

const doctorsLoaded = ref(false);

// 从 API 加载医生数据
async function loadDoctors() {
  if (doctorsLoaded.value) return;

  try {
    const doctors = await doctorApi.getAllDoctors();
    state.doctors = doctors.map(doc => ({
      ...doc,
      password: '123456' // 添加密码用于登录验证
    }));
    doctorsLoaded.value = true;
  } catch (error) {
    console.error('Failed to load doctors from API, using fallback data:', error);
    // 降级到本地 JSON 数据
    state.doctors = doctorData as Doctor[];
    doctorsLoaded.value = true;
  }
}

// 初始化时加载医生数据
loadDoctors();

export const store = {
  state,

  loginDoctor(username: string, password: string): Doctor | null {
    const doctor = state.doctors.find(
      d => d.username === username && d.password === password
    );
    if (doctor) {
      state.currentDoctor = doctor;
      localStorage.setItem('currentDoctor', JSON.stringify(doctor));
      return doctor;
    }
    return null;
  },

  logoutDoctor() {
    state.currentDoctor = null;
    localStorage.removeItem('currentDoctor');
  },

  verifyPatient(name: string, birthday: string): Patient {
    let patient = state.patients.find(
      p => p.name === name && p.birthday === birthday
    );

    if (!patient) {
      patient = {
        id: `patient${Date.now()}`,
        name,
        birthday,
        phone: '',
        gender: '',
      };
      state.patients.push(patient);
    }

    state.currentPatient = patient;
    return patient;
  },

  loginPatient(patient: Patient) {
    state.currentPatient = patient;
    localStorage.setItem('currentPatient', JSON.stringify(patient));
  },

  logoutPatient() {
    state.currentPatient = null;
    localStorage.removeItem('currentPatient');
  },

  getQuestionsByDoctor(doctorId: string): Question[] {
    return state.questions.filter(q => q.doctorId === doctorId);
  },

  getQuestionsByPatient(patientId: string): Question[] {
    return state.questions.filter(q => q.patientId === patientId);
  },

  addQuestion(question: Omit<Question, 'id' | 'submitTime' | 'status' | 'answer' | 'answerTime'>): Question {
    const newQuestion: Question = {
      ...question,
      id: `q${Date.now()}`,
      submitTime: new Date().toISOString(),
      status: 'pending',
      answer: null,
      answerTime: null,
    };
    state.questions.push(newQuestion);
    return newQuestion;
  },

  answerQuestion(questionId: string, answer: string) {
    const question = state.questions.find(q => q.id === questionId);
    if (question) {
      question.status = 'answered';
      question.answer = answer;
      question.answerTime = new Date().toISOString();
    }
  },

  markQuestionAsAnswered(questionId: string) {
    const question = state.questions.find(q => q.id === questionId);
    if (question) {
      question.status = 'answered';
      question.answer = '已口述解答';
      question.answerTime = new Date().toISOString();
    }
  },

  async getActiveDoctors(): Promise<Doctor[]> {
    if (!doctorsLoaded.value) {
      await loadDoctors();
    }
    return state.doctors.filter(d => d.isActive);
  },

  async getDoctorByUsername(username: string): Promise<Doctor | undefined> {
    if (!doctorsLoaded.value) {
      await loadDoctors();
    }
    return state.doctors.find(d => d.username === username);
  },

  async getStatistics() {
    const totalDoctors = state.doctors.length;
    const totalQuestions = state.questions.length;
    const activeSessions = state.questions.filter(q => q.status === 'pending').length;
    const totalSessions = state.doctors.filter(d => d.isActive).length;

    return {
      totalDoctors,
      totalQuestions,
      activeSessions,
      totalSessions,
    };
  },
};
