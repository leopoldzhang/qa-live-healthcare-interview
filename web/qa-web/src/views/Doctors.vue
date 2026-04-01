<template>
  <div class="doctors-page">
    <div class="page-header">
      <h1>{{ t('doctors.title') }}</h1>
      <p>{{ t('doctors.subtitle') }}</p>
    </div>

    <div class="doctors-container">
      <Spin :spinning="loading" size="large" :tip="t('common.loading')">
        <div class="doctors-grid">
          <a-card
            v-for="doctor in allDoctors"
            :key="doctor.id"
            class="doctor-card"
            :class="{ 'active': doctor.isActive }"
          >
          <div class="card-header">
            <img :src="doctor.avatar" :alt="doctor.name" class="doctor-avatar" />
            <a-badge
              :status="doctor.isActive ? 'processing' : 'default'"
              :text="doctor.isActive ? t('common.online') : t('common.offline')"
            />
          </div>
          <div class="card-body">
            <h3>{{ doctor.name }}</h3>
            <p class="doctor-title">{{ doctor.title }}</p>
            <p class="doctor-department">{{ doctor.department }}</p>
            <p class="doctor-experience">{{ doctor.experience }}</p>
            <div class="doctor-specialties">
              <a-tag v-for="specialty in doctor.specialties" :key="specialty" color="blue">
                {{ specialty }}
              </a-tag>
            </div>
          </div>
          <div class="card-footer">
            <a-button
              type="primary"
              block
              :disabled="!doctor.isActive"
              @click="goToConsultation(doctor)"
            >
              {{ doctor.isActive ? t('doctors.enterRoom') : t('doctors.notAvailable') }}
            </a-button>
          </div>
        </a-card>
        </div>
      </Spin>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { store, Doctor } from '../store';
import { Spin } from 'ant-design-vue';

const { t } = useI18n();

const router = useRouter();
const loading = ref(true);
const allDoctors = ref<Doctor[]>([]);

onMounted(async () => {
  try {
    allDoctors.value = await store.getActiveDoctors();
  } catch (error) {
    console.error('Failed to load doctors:', error);
  } finally {
    loading.value = false;
  }
});

const goToConsultation = (doctor: Doctor) => {
  router.push(`/consultation/${doctor.username}`);
};
</script>

<style scoped>
.doctors-page {
  min-height: calc(100vh - 64px);
  padding-top: 64px;
  background: #f0f2f5;
}

.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 80px 24px;
  text-align: center;
  color: #fff;
}

.page-header h1 {
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
}

.page-header p {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
}

.doctors-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 24px;
}

.doctors-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.doctor-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s;
}

.doctor-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-4px);
}

.doctor-card.active {
  border: 2px solid #52c41a;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.doctor-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.card-body h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.doctor-title {
  font-size: 16px;
  color: #1890ff;
  font-weight: 500;
  margin-bottom: 4px;
}

.doctor-department {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.doctor-experience {
  font-size: 14px;
  color: #999;
  margin-bottom: 16px;
}

.doctor-specialties {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .page-header h1 {
    font-size: 32px;
  }

  .page-header p {
    font-size: 16px;
  }

  .doctors-grid {
    grid-template-columns: 1fr;
  }
}
</style>
