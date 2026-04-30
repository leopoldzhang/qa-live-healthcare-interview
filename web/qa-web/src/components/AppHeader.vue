<template>
  <a-layout-header class="header">
    <div class="header-content">
      <div class="logo">
        <img src="https://images.pexels.com/photos/40568/medical-appointment-doctor-healthcare-40568.jpeg?auto=compress&cs=tinysrgb&w=100" alt="QA Live Healthcare" />
        <span>{{ t('header.logo') }}</span>
      </div>
      <a-menu v-model:selectedKeys="selectedKeys" mode="horizontal" class="nav-menu">
        <a-menu-item key="home" @click="navigateTo('/')">
          <HomeOutlined />
          {{ t('header.home') }}
        </a-menu-item>
        <a-menu-item key="consultation" @click="navigateTo('/consultation')">
          <MessageOutlined />
          {{ t('header.consultation') }}
        </a-menu-item>
        <a-menu-item key="appointment" @click="navigateTo('/appointments')">
          <CalendarOutlined />
          {{ t('header.appointment') }}
        </a-menu-item>
        <a-menu-item key="doctors" @click="navigateTo('/doctors')">
          <TeamOutlined />
          {{ t('header.doctors') }}
        </a-menu-item>
        <a-menu-item key="about" @click="navigateTo('/about')">
          <InfoCircleOutlined />
          {{ t('header.about') }}
        </a-menu-item>
      </a-menu>
      <div class="header-actions">
        <!-- 患者入口 -->
        <template v-if="currentPatient">
          <a-dropdown>
            <a-button class="patient-btn">
              <UserOutlined />
              {{ currentPatient.name }}
            </a-button>
            <template #overlay>
              <a-menu>
                <a-menu-item key="appointments" @click="navigateTo('/appointments')">
                  <CalendarOutlined />
                  我的预约
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item key="logout" @click="handlePatientLogout">
                  <LogoutOutlined />
                  退出登录
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
        <template v-else>
          <a-button type="link" @click="navigateTo('/patient/login')">
            患者登录
          </a-button>
          <a-button type="link" @click="navigateTo('/patient/register')">
            注册
          </a-button>
        </template>

        <!-- 医生入口 -->
        <a-dropdown v-if="currentDoctor">
          <a-button type="primary" class="login-btn">
            <UserOutlined />
            {{ currentDoctor.name }}
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item key="schedule" @click="navigateTo('/doctor/schedule')">
                <CalendarOutlined />
                我的排班
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout" @click="handleDoctorLogout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <a-button v-else type="primary" class="login-btn" @click="navigateTo('/doctor/login')">
          <UserOutlined />
          医生登录
        </a-button>

        <!-- 语言选择 -->
        <a-select
          v-model:value="currentLocale"
          @change="changeLanguage"
          :style="{ width: 120 }"
          class="language-selector"
        >
          <a-select-option value="zh-CN">中文</a-select-option>
          <a-select-option value="en-US">English</a-select-option>
        </a-select>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { HomeOutlined, MessageOutlined, TeamOutlined, InfoCircleOutlined, UserOutlined, CalendarOutlined, LogoutOutlined } from '@ant-design/icons-vue';
import { useI18n } from 'vue-i18n';
import { store } from '../store';

const { locale, t } = useI18n();
const currentLocale = ref(locale.value);

const router = useRouter();
const route = useRoute();
const selectedKeys = ref<string[]>(['home']);

const currentPatient = ref<any>(null);
const currentDoctor = ref<any>(null);

// 更新登录状态
const updateLoginStatus = () => {
  const patientStr = localStorage.getItem('currentPatient');
  const doctorStr = localStorage.getItem('currentDoctor');
  currentPatient.value = patientStr ? JSON.parse(patientStr) : null;
  currentDoctor.value = doctorStr ? JSON.parse(doctorStr) : null;
};

onMounted(() => {
  updateLoginStatus();
});

// 监听路由变化，更新登录状态
watch(() => route.path, () => {
  updateLoginStatus();
});

watch(() => route.path, (newPath) => {
  if (newPath === '/') {
    selectedKeys.value = ['home'];
  } else if (newPath.startsWith('/consultation')) {
    selectedKeys.value = ['consultation'];
  } else if (newPath.startsWith('/appointments')) {
    selectedKeys.value = ['appointment'];
  } else if (newPath.startsWith('/doctors')) {
    selectedKeys.value = ['doctors'];
  } else if (newPath.startsWith('/about')) {
    selectedKeys.value = ['about'];
  }
}, { immediate: true });

const navigateTo = (path: string) => {
  router.push(path);
};

const changeLanguage = (value: string) => {
  locale.value = value;
  localStorage.setItem('locale', value);
  currentLocale.value = value;
};

const handlePatientLogout = () => {
  store.logoutPatient();
  currentPatient.value = null;
  router.push('/');
};

const handleDoctorLogout = () => {
  store.logoutDoctor();
  currentDoctor.value = null;
  router.push('/');
};
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 0;
  height: 64px;
  line-height: 64px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo img {
  height: 40px;
  width: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.logo span {
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
}

.nav-menu {
  flex: 1;
  border: none;
  margin: 0 40px;
  line-height: 64px;
}

.login-btn {
  background: #52c41a;
  border-color: #52c41a;
}

.login-btn:hover {
  background: #73d13d;
  border-color: #73d13d;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.language-selector {
  min-width: 120px;
}
</style>
