<template>
  <div class="patient-login">
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <UserOutlined class="login-icon" />
          <h1>{{ t('patientLogin.title') }}</h1>
          <p>{{ t('patientLogin.subtitle') }}</p>
        </div>

        <a-form
          :model="loginForm"
          :rules="loginRules"
          @finish="handleLogin"
          layout="vertical"
        >
          <a-form-item :label="t('patientLogin.username')" name="username">
            <a-input
              v-model:value="loginForm.username"
              size="large"
              :placeholder="t('patientLogin.usernamePlaceholder')"
            >
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item :label="t('patientLogin.password')" name="password">
            <a-input-password
              v-model:value="loginForm.password"
              size="large"
              :placeholder="t('patientLogin.passwordPlaceholder')"
            >
              <template #prefix>
                <LockOutlined />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              block
              :loading="loading"
            >
              {{ t('patientLogin.loginButton') }}
            </a-button>
          </a-form-item>
        </a-form>

        <div class="login-footer">
          <span>{{ t('patientLogin.noAccount') }}</span>
          <router-link to="/patient/register">{{ t('patientLogin.registerNow') }}</router-link>
        </div>

        <a-alert
          v-if="errorMessage"
          :message="errorMessage"
          type="error"
          show-icon
          closable
          @close="errorMessage = ''"
          style="margin-top: 16px"
        />
      </div>

      <div class="demo-credentials">
        <a-alert
          :message="t('patientLogin.testAccount')"
          :description="t('patientLogin.testAccountInfo')"
          type="info"
          show-icon
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { login } from '../api/auth';
import { store } from '../store';

const { t } = useI18n();

const router = useRouter();
const route = useRoute();

const loginForm = reactive({
  username: '',
  password: '',
});

const loginRules = {
  username: [
    { required: true, message: t('patientLogin.usernameRequired') },
    { min: 4, max: 20, message: t('patientLogin.usernameLength') },
  ],
  password: [
    { required: true, message: t('patientLogin.passwordRequired') },
    { min: 6, max: 20, message: t('patientLogin.passwordLength') },
  ],
};

const loading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  loading.value = true;
  errorMessage.value = '';

  try {
    const response = await login({
      username: loginForm.username,
      password: loginForm.password,
    });

    if (response.code === 200) {
      // 保存登录状态到 localStorage 和 store
      store.loginPatient(response.data);
      message.success(t('patientLogin.loginSuccess'));

      // 跳转到预约列表页面
      const redirect = route.query.redirect as string;
      router.push(redirect || '/appointments');
    } else {
      errorMessage.value = response.message || t('patientLogin.loginFailed');
    }
  } catch (error: any) {
    console.error('Login error:', error);
    if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = t('patientLogin.networkError');
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.patient-login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.login-container {
  width: 100%;
  max-width: 450px;
}

.login-card {
  background: #fff;
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-icon {
  font-size: 56px;
  color: #667eea;
  margin-bottom: 16px;
}

.login-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #666;
}

.login-footer a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.login-footer a:hover {
  text-decoration: underline;
}

.demo-credentials {
  margin-top: 24px;
}

@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
  }

  .login-header h1 {
    font-size: 24px;
  }
}
</style>
