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
        <a-button type="primary" class="login-btn" @click="navigateTo('/doctor/login')">
          <UserOutlined />
          {{ t('header.doctorLogin') }}
        </a-button>
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
import { ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { HomeOutlined, MessageOutlined, TeamOutlined, InfoCircleOutlined, UserOutlined } from '@ant-design/icons-vue';
import { useI18n } from 'vue-i18n';

const { locale, t } = useI18n();
const currentLocale = ref(locale.value);

const router = useRouter();
const route = useRoute();
const selectedKeys = ref<string[]>(['home']);

watch(() => route.path, (newPath) => {
  if (newPath === '/') {
    selectedKeys.value = ['home'];
  } else if (newPath.startsWith('/consultation')) {
    selectedKeys.value = ['consultation'];
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
