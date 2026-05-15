<template>
  <div class="consultation">
    <div class="consultation-container">
      <div v-if="!currentPatient" class="auth-section">
        <div class="auth-card">
          <h1>{{ t('consultation.patientVerification') }}</h1>
          <p>{{ t('consultation.enterNameBirthday') }}</p>
          <a-form
            :model="authForm"
            :rules="authRules"
            @finish="verifyPatient"
            layout="vertical"
          >
            <a-form-item :label="t('consultation.name')" name="name">
              <a-input
                v-model:value="authForm.name"
                size="large"
                :placeholder="t('consultation.namePlaceholder')"
              >
                <template #prefix>
                  <UserOutlined />
                </template>
              </a-input>
            </a-form-item>

            <a-form-item :label="t('consultation.birthday')" name="birthday">
              <a-date-picker
                v-model:value="authForm.birthday"
                size="large"
                format="YYYY-MM-DD"
                :placeholder="t('consultation.birthdayPlaceholder')"
                style="width: 100%"
              />
            </a-form-item>

            <a-form-item>
              <a-button type="primary" html-type="submit" size="large" block>
                {{ t('consultation.verify') }}
              </a-button>
            </a-form-item>
          </a-form>

          <a-alert
            :message="t('consultation.tip')"
            :description="t('consultation.tipDescription')"
            type="info"
            show-icon
          />
        </div>
      </div>

      <div v-else class="patient-portal">
        <div class="portal-header">
          <div class="patient-info">
            <UserOutlined class="patient-icon-large" />
            <div>
              <h1>{{ currentPatient.name }}{{ t('consultation.welcomeConsultation') }}</h1>
              <p>{{ t('consultation.welcomeMessage') }}</p>
            </div>
          </div>
          <div class="portal-actions">
            <a-button @click="logoutPatient">
              <LogoutOutlined />
              {{ t('consultation.switchUser') }}
            </a-button>
          </div>
        </div>

        <div class="selected-doctor" v-if="selectedDoctor">
          <a-alert
            :message="`${t('consultation.currentRoom')}: ${selectedDoctor.name} - ${selectedDoctor.department}`"
            type="success"
            show-icon
            closable
            @close="clearSelectedDoctor"
          />
        </div>

        <div class="questions-section">
          <div class="section-header">
            <h2>{{ t('consultation.myQuestions') }}</h2>
            <a-button type="primary" @click="showSubmitModal">
              <PlusOutlined />
              {{ t('consultation.submitQuestion') }}
            </a-button>
          </div>

          <a-empty v-if="myQuestions.length === 0" :description="t('consultation.noQuestions')" />

          <div v-else class="my-questions-list">
            <a-card
              v-for="question in myQuestions"
              :key="question.id"
              class="question-item"
            >
              <template #title>
                <div class="question-title">
                  <span>{{ question.doctorName }}</span>
                  <a-tag :color="question.status === 'answered' ? 'green' : 'orange'">
                    {{ question.status === 'answered' ? t('consultation.answered') : t('consultation.pending') }}
                  </a-tag>
                </div>
              </template>
              <div class="question-detail">
                <p class="question-text"><strong>{{ t('consultation.questionText') }}:</strong> {{ question.question }}</p>
                <p class="submit-time">{{ t('consultation.submitTime') }}: {{ formatTime(question.submitTime) }}</p>
                <div v-if="question.status === 'answered'" class="answer-section">
                  <a-divider />
                  <p class="answer-text"><strong>{{ t('consultation.doctorReply') }}:</strong> {{ question.answer }}</p>
                  <p class="answer-time">{{ t('consultation.submitTime') }}: {{ formatTime(question.answerTime!) }}</p>
                </div>
              </div>
            </a-card>
          </div>
        </div>
      </div>
    </div>

    <a-modal
      v-model:open="submitModalVisible"
      :title="t('consultation.submitQuestionTitle')"
      @ok="submitQuestion"
      @cancel="closeSubmitModal"
      :confirmLoading="submitting"
      width="600px"
    >
      <a-form layout="vertical">
        <a-form-item :label="t('consultation.selectDoctor')" required>
          <a-select
            v-model:value="questionForm.doctorId"
            size="large"
            :placeholder="t('consultation.selectDoctorPlaceholder')"
            :disabled="!!selectedDoctor"
          >
            <a-select-option
              v-for="doctor in availableDoctors"
              :key="doctor.id"
              :value="doctor.id"
            >
              <div class="doctor-option">
                <img :src="doctor.avatar" :alt="doctor.name" class="doctor-option-avatar" />
                <div>
                  <div>{{ doctor.name }}</div>
                  <div style="font-size: 12px; color: #999;">
                    {{ doctor.title }} · {{ doctor.department }}
                  </div>
                </div>
              </div>
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :label="t('consultation.yourQuestion')" required>
          <a-textarea
            v-model:value="questionForm.question"
            :rows="6"
            :placeholder="t('consultation.questionPlaceholder')"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n';
import dayjs, { Dayjs } from 'dayjs';
import {
  UserOutlined,
  LogoutOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import { store, Doctor } from '../store';

const { t } = useI18n();
const route = useRoute();
const router = useRouter();

const currentPatient = computed(() => store.state.currentPatient);
const myQuestions = computed(() =>
  currentPatient.value
    ? store.getQuestionsByPatient(currentPatient.value.id)
    : []
);

const selectedDoctor = ref<Doctor | null>(null);

const authForm = reactive({
  name: '',
  birthday: null as Dayjs | null,
});

const authRules = {
  name: [{ required: true, message: t('consultation.namePlaceholder') }],
  birthday: [{ required: true, message: t('consultation.birthdayPlaceholder') }],
};

const submitModalVisible = ref(false);
const submitting = ref(false);

const questionForm = reactive({
  doctorId: '',
  question: '',
});

const availableDoctors = ref<Doctor[]>([]);

const loadDoctors = async () => {
  availableDoctors.value = await store.getActiveDoctors();
};

onMounted(async () => {
  await loadDoctors();
  const doctorUsername = route.params.doctorUsername as string;
  if (doctorUsername) {
    const doctor = await store.getDoctorByUsername(doctorUsername);
    if (doctor && doctor.isActive) {
      selectedDoctor.value = doctor;
      questionForm.doctorId = doctor.id;
    }
  }
});

const verifyPatient = () => {
  const birthday = authForm.birthday?.format('YYYY-MM-DD');
  if (!birthday) {
    message.error(t('consultation.pleaseSelectBirthday'));
    return;
  }

  const existingPatientCount = store.state.patients.filter(
    p => p.name === authForm.name && p.birthday === birthday
  ).length;

  store.verifyPatient(authForm.name, birthday);

  if (existingPatientCount > 0) {
    message.success(t('consultation.verifiedWelcomeBack'));
  } else {
    message.success(t('consultation.verifiedFirstTime'));
  }
};

const logoutPatient = () => {
  store.logoutPatient();
  selectedDoctor.value = null;
  message.success(t('consultation.loggedOut'));
  router.push('/patient/login');
};

const clearSelectedDoctor = () => {
  selectedDoctor.value = null;
  questionForm.doctorId = '';
};

const showSubmitModal = () => {
  if (selectedDoctor.value) {
    questionForm.doctorId = selectedDoctor.value.id;
  }
  submitModalVisible.value = true;
};

const closeSubmitModal = () => {
  submitModalVisible.value = false;
  if (!selectedDoctor.value) {
    questionForm.doctorId = '';
  }
  questionForm.question = '';
};

const submitQuestion = () => {
  if (!questionForm.doctorId) {
    message.error(t('consultation.pleaseSelectDoctor'));
    return;
  }

  if (!questionForm.question.trim()) {
    message.error(t('consultation.pleaseEnterQuestion'));
    return;
  }

  submitting.value = true;

  setTimeout(() => {
    const doctor = store.state.doctors.find(d => d.id === questionForm.doctorId);
    if (doctor && currentPatient.value) {
      store.addQuestion({
        patientId: currentPatient.value.id,
        patientName: currentPatient.value.name,
        doctorId: doctor.id,
        doctorName: doctor.name,
        question: questionForm.question,
      });

      message.success(t('consultation.questionSubmitted'));
      closeSubmitModal();
    }

    submitting.value = false;
  }, 500);
};

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm');
};
</script>

<style scoped>
.consultation {
  min-height: calc(100vh - 64px);
  padding-top: 64px;
  background: #f0f2f5;
}

.consultation-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.auth-section {
  min-height: calc(100vh - 112px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.auth-card {
  background: #fff;
  border-radius: 16px;
  padding: 48px;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.auth-card h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  text-align: center;
  margin-bottom: 8px;
}

.auth-card > p {
  font-size: 16px;
  color: #666;
  text-align: center;
  margin-bottom: 32px;
}

.patient-portal {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.portal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
}

.patient-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.patient-icon-large {
  font-size: 48px;
  color: #fff;
}

.patient-info h1 {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 4px;
}

.patient-info p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
}

.selected-doctor {
  padding: 16px 24px;
  background: #f6ffed;
  border-bottom: 1px solid #e8e8e8;
}

.questions-section {
  padding: 24px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.section-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.my-questions-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.question-item {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.question-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.question-detail {
  line-height: 1.6;
}

.question-text,
.answer-text {
  margin-bottom: 12px;
  color: #333;
}

.submit-time,
.answer-time {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.answer-section {
  margin-top: 16px;
}

.doctor-option {
  display: flex;
  align-items: center;
  gap: 12px;
}

.doctor-option-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

@media (max-width: 768px) {
  .auth-card {
    margin: 24px;
    padding: 32px 24px;
  }

  .portal-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .portal-actions {
    width: 100%;
  }
}
</style>
