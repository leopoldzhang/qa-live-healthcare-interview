<template>
  <div class="doctor-login">
    <div class="login-container">
      <div class="login-header">
        <h1>{{ t('doctorLogin.title') }}</h1>
        <p>{{ t('doctorLogin.subtitle') }}</p>
      </div>
      <a-form
        :model="formState"
        :rules="rules"
        @finish="onFinish"
        layout="vertical"
        class="login-form"
      >
        <a-form-item :label="t('doctorLogin.username')" name="username">
          <a-input
            v-model:value="formState.username"
            size="large"
            :placeholder="t('doctorLogin.usernamePlaceholder')"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item :label="t('doctorLogin.password')" name="password">
          <a-input-password
            v-model:value="formState.password"
            size="large"
            :placeholder="t('doctorLogin.passwordPlaceholder')"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="loading">
            {{ t('doctorLogin.loginButton') }}
          </a-button>
        </a-form-item>
      </a-form>

      <a-alert
        :message="t('doctorLogin.testAccount')"
        :description="t('doctorLogin.testAccountInfo')"
        type="info"
        show-icon
        closable
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { store } from '../store';

const { t } = useI18n();

const router = useRouter();
const loading = ref(false);

const formState = reactive({
  username: '',
  password: '',
});

const rules = {
  username: [{ required: true, message: t('doctorLogin.usernameRequired') }],
  password: [{ required: true, message: t('doctorLogin.passwordRequired') }],
};

const onFinish = async () => {
  loading.value = true;

  setTimeout(() => {
    const doctor = store.loginDoctor(formState.username, formState.password);

    if (doctor) {
      message.success(t('doctorLogin.loginSuccess'));
      router.push(`/doctor/room/${doctor.username}`);
    } else {
      message.error(t('doctorLogin.loginFailed'));
    }

    loading.value = false;
  }, 500);
};
</script>

<style scoped>
.doctor-login {
  min-height: calc(100vh - 64px);
  padding-top: 64px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-container {
  background: #fff;
  border-radius: 16px;
  padding: 48px;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-header h1 {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 16px;
  color: #666;
}

.login-form {
  margin-bottom: 24px;
}

@media (max-width: 768px) {
  .login-container {
    margin: 24px;
    padding: 32px 24px;
  }

  .login-header h1 {
    font-size: 24px;
  }
}
</style>
