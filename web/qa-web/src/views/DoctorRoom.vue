<template>
  <div class="doctor-room">
    <div class="room-container" v-if="currentDoctor">
      <div class="room-header">
        <div class="doctor-info">
          <img :src="currentDoctor.avatar" :alt="currentDoctor.name" class="doctor-avatar" />
          <div>
            <h1>{{ currentDoctor.name }}{{ t('doctorRoom.title') }}</h1>
            <p>{{ currentDoctor.title }} · {{ currentDoctor.department }}</p>
          </div>
        </div>
        <div class="room-actions">
          <a-button @click="copyRoomUrl">
            <CopyOutlined />
            {{ t('doctorRoom.copyRoomUrl') }}
          </a-button>
          <a-button danger @click="logout">
            <LogoutOutlined />
            {{ t('doctorRoom.logout') }}
          </a-button>
        </div>
      </div>

      <div class="room-url">
        <a-alert
          :message="`${t('doctorRoom.roomUrl')}: ${roomUrl}`"
          type="success"
          show-icon
        />
      </div>

      <div class="questions-section">
        <div class="section-header">
          <h2>{{ t('doctorRoom.pendingQuestions') }} ({{ pendingQuestions.length }})</h2>
          <a-button type="primary" @click="refreshQuestions">
            <ReloadOutlined />
            {{ t('doctorRoom.refresh') }}
          </a-button>
        </div>

        <a-empty v-if="pendingQuestions.length === 0" :description="t('doctorRoom.noPendingQuestions')" />

        <div v-else class="questions-list">
          <div
            v-for="question in pendingQuestions"
            :key="question.id"
            class="question-card"
          >
            <div class="question-header">
              <div class="patient-info">
                <UserOutlined class="patient-icon" />
                <span class="patient-name">{{ question.patientName }}</span>
              </div>
              <span class="submit-time">{{ formatTime(question.submitTime) }}</span>
            </div>
            <div class="question-content">
              <p>{{ question.question }}</p>
            </div>
            <div class="question-actions">
              <a-button type="primary" @click="showAnswerModal(question)">
                <EditOutlined />
                {{ t('doctorRoom.textReply') }}
              </a-button>
              <a-button @click="markAsAnswered(question.id)">
                <CheckOutlined />
                {{ t('doctorRoom.markAnswered') }}
              </a-button>
            </div>
          </div>
        </div>
      </div>

      <div class="answered-section">
        <h2>{{ t('doctorRoom.answeredQuestions') }} ({{ answeredQuestions.length }})</h2>
        <a-collapse v-if="answeredQuestions.length > 0" accordion>
          <a-collapse-panel
            v-for="question in answeredQuestions"
            :key="question.id"
            :header="`${question.patientName}: ${question.question.substring(0, 50)}...`"
          >
            <div class="answered-content">
              <p class="question-text"><strong>{{ t('doctorRoom.question') }}:</strong> {{ question.question }}</p>
              <p class="answer-text"><strong>{{ t('doctorRoom.replyQuestion') }}:</strong> {{ question.answer }}</p>
              <p class="answer-time">{{ t('doctorRoom.answerTime') }}: {{ formatTime(question.answerTime!) }}</p>
            </div>
          </a-collapse-panel>
        </a-collapse>
        <a-empty v-else :description="t('doctorRoom.noAnsweredQuestions')" />
      </div>
    </div>

    <a-modal
      v-model:open="answerModalVisible"
      :title="t('doctorRoom.replyQuestion')"
      @ok="submitAnswer"
      @cancel="closeAnswerModal"
      :confirmLoading="submitting"
    >
      <div v-if="selectedQuestion" class="modal-content">
        <div class="question-info">
          <p><strong>{{ t('doctorRoom.patient') }}:</strong> {{ selectedQuestion.patientName }}</p>
          <p><strong>{{ t('doctorRoom.question') }}:</strong> {{ selectedQuestion.question }}</p>
        </div>
        <a-form-item :label="t('doctorRoom.yourReply')">
          <a-textarea
            v-model:value="answerText"
            :rows="6"
            :placeholder="t('doctorRoom.replyPlaceholder')"
          />
        </a-form-item>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n';
import dayjs from 'dayjs';
import {
  CopyOutlined,
  LogoutOutlined,
  ReloadOutlined,
  UserOutlined,
  EditOutlined,
  CheckOutlined
} from '@ant-design/icons-vue';
import { store, Question } from '../store';

const { t } = useI18n();
const route = useRoute();
const router = useRouter();

const username = route.params.username as string;
const currentDoctor = computed(() => store.state.currentDoctor);
const roomUrl = computed(() => `${window.location.origin}/consultation/${username}`);

const pendingQuestions = computed(() =>
  currentDoctor.value
    ? store.getQuestionsByDoctor(currentDoctor.value.id).filter(q => q.status === 'pending')
    : []
);

const answeredQuestions = computed(() =>
  currentDoctor.value
    ? store.getQuestionsByDoctor(currentDoctor.value.id).filter(q => q.status === 'answered')
    : []
);

const answerModalVisible = ref(false);
const selectedQuestion = ref<Question | null>(null);
const answerText = ref('');
const submitting = ref(false);

onMounted(() => {
  if (!currentDoctor.value || currentDoctor.value.username !== username) {
    message.error(t('doctorRoom.pleaseLoginFirst'));
    router.push('/doctor/login');
  }
});

const copyRoomUrl = () => {
  navigator.clipboard.writeText(roomUrl.value);
  message.success(t('doctorRoom.roomUrlCopied'));
};

const logout = () => {
  store.logoutDoctor();
  message.success(t('doctorRoom.loggedOut'));
  router.push('/');
};

const refreshQuestions = () => {
  message.success(t('doctorRoom.questionsRefreshed'));
};

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm');
};

const showAnswerModal = (question: Question) => {
  selectedQuestion.value = question;
  answerText.value = '';
  answerModalVisible.value = true;
};

const closeAnswerModal = () => {
  answerModalVisible.value = false;
  selectedQuestion.value = null;
  answerText.value = '';
};

const submitAnswer = () => {
  if (!answerText.value.trim()) {
    message.error(t('doctorRoom.pleaseEnterReply'));
    return;
  }

  submitting.value = true;

  setTimeout(() => {
    if (selectedQuestion.value) {
      store.answerQuestion(selectedQuestion.value.id, answerText.value);
      message.success(t('doctorRoom.replySuccess'));
      closeAnswerModal();
    }
    submitting.value = false;
  }, 500);
};

const markAsAnswered = (questionId: string) => {
  store.markQuestionAsAnswered(questionId);
  message.success(t('doctorRoom.markedAsAnswered'));
};
</script>

<style scoped>
.doctor-room {
  min-height: calc(100vh - 64px);
  padding-top: 64px;
  background: #f0f2f5;
}

.room-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.room-header {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.doctor-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.doctor-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.doctor-info h1 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px;
}

.doctor-info p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.room-actions {
  display: flex;
  gap: 12px;
}

.room-url {
  margin-bottom: 24px;
}

.questions-section,
.answered-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.section-header h2,
.answered-section h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0 0 24px;
}

.questions-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.question-card {
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.question-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.patient-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.patient-icon {
  font-size: 16px;
  color: #1890ff;
}

.patient-name {
  font-weight: 600;
  color: #333;
}

.submit-time {
  font-size: 12px;
  color: #999;
}

.question-content {
  margin-bottom: 12px;
}

.question-content p {
  font-size: 15px;
  color: #333;
  line-height: 1.6;
  margin: 0;
}

.question-actions {
  display: flex;
  gap: 12px;
}

.answered-content {
  padding: 12px 0;
}

.question-text,
.answer-text {
  margin-bottom: 12px;
  line-height: 1.6;
}

.answer-time {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.modal-content .question-info {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.modal-content .question-info p {
  margin: 8px 0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .room-header {
    flex-direction: column;
    gap: 16px;
  }

  .room-actions {
    width: 100%;
    flex-direction: column;
  }

  .question-actions {
    flex-direction: column;
  }
}
</style>
