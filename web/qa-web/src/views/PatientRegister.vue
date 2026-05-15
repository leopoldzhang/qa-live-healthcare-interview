<template>
  <div class="patient-register">
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <UserAddOutlined class="register-icon" />
          <h1>{{ t('patientRegister.title') }}</h1>
          <p>{{ t('patientRegister.subtitle') }}</p>
        </div>

        <a-form
          :model="registerForm"
          :rules="registerRules"
          @finish="handleRegister"
          layout="vertical"
        >
          <a-form-item :label="t('patientRegister.username')" name="username" :validate-status="usernameStatus" :help="usernameHelp">
            <a-input
              v-model:value="registerForm.username"
              size="large"
              :placeholder="t('patientRegister.usernamePlaceholder')"
              @blur="checkUsernameUnique"
              @input="clearUsernameError"
            >
              <template #prefix>
                <UserOutlined />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item :label="t('patientRegister.password')" name="password">
            <a-input-password
              v-model:value="registerForm.password"
              size="large"
              :placeholder="t('patientRegister.passwordPlaceholder')"
            >
              <template #prefix>
                <LockOutlined />
              </template>
            </a-input-password>
            <div v-if="passwordStrength" class="password-strength">
              <span :class="passwordClass">{{ t('patientRegister.password' + passwordStrength.charAt(0).toUpperCase() + passwordStrength.slice(1)) }}</span>
            </div>
          </a-form-item>

          <a-form-item :label="t('patientRegister.confirmPassword')" name="confirmPassword">
            <a-input-password
              v-model:value="registerForm.confirmPassword"
              size="large"
              :placeholder="t('patientRegister.confirmPasswordPlaceholder')"
            >
              <template #prefix>
                <LockOutlined />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item :label="t('patientRegister.name')" name="name">
            <a-input
              v-model:value="registerForm.name"
              size="large"
              :placeholder="t('patientRegister.namePlaceholder')"
            >
              <template #prefix>
                <IdcardOutlined />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item :label="t('patientRegister.birthday')" name="birthday">
            <a-date-picker
              v-model:value="registerForm.birthday"
              size="large"
              format="YYYY-MM-DD"
              :placeholder="t('patientRegister.birthdayPlaceholder')"
              style="width: 100%"
            />
          </a-form-item>

          <a-form-item :label="t('patientRegister.phone')" name="phone">
            <a-input
              v-model:value="registerForm.phone"
              size="large"
              :placeholder="t('patientRegister.phonePlaceholder')"
            >
              <template #prefix>
                <PhoneOutlined />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item :label="t('patientRegister.gender')" name="gender">
            <a-radio-group v-model:value="registerForm.gender" size="large">
              <a-radio value="男">{{ t('patientRegister.male') }}</a-radio>
              <a-radio value="女">{{ t('patientRegister.female') }}</a-radio>
            </a-radio-group>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              block
              :loading="loading"
            >
              {{ t('patientRegister.registerButton') }}
            </a-button>
          </a-form-item>
        </a-form>

        <div class="register-footer">
          <span>{{ t('patientRegister.hasAccount') }}</span>
          <router-link to="/patient/login">{{ t('patientRegister.loginNow') }}</router-link>
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useI18n } from 'vue-i18n';
import { Dayjs } from 'dayjs';
import {
  UserOutlined,
  LockOutlined,
  IdcardOutlined,
  PhoneOutlined,
  UserAddOutlined,
} from '@ant-design/icons-vue';
import { register, checkUsername } from '../api/auth';
import { store } from '../store';

const { t } = useI18n();

const router = useRouter();

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  birthday: null as Dayjs | null,
  phone: '',
  gender: '男',
});

const loading = ref(false);
const errorMessage = ref('');
const usernameStatus = ref('');
const usernameHelp = ref('');

// 密码强度计算
const passwordStrength = computed(() => {
  const pwd = registerForm.password;
  if (!pwd) return '';

  let score = 0;
  if (pwd.length >= 6) score++;
  if (pwd.length >= 8) score++;
  if (/[a-zA-Z]/.test(pwd)) score++;
  if (/[0-9]/.test(pwd)) score++;
  if (/[^a-zA-Z0-9]/.test(pwd)) score++;

  if (score <= 2) return 'Weak';
  if (score <= 3) return 'Medium';
  return 'Strong';
});

const passwordClass = computed(() => {
  const strength = passwordStrength.value;
  if (strength === 'Weak') return 'strength-weak';
  if (strength === 'Medium') return 'strength-medium';
  return 'strength-strong';
});

// 验证用户名唯一性
const validateUsername = async (_rule: any, value: string) => {
  if (!value) {
    return Promise.resolve();
  }

  try {
    const response = await checkUsername(value);
    if (response.data.exists) {
      return Promise.reject(t('patientRegister.usernameExists'));
    }
    return Promise.resolve();
  } catch (error) {
    return Promise.reject(t('patientRegister.usernameCheckFailed'));
  }
};

// 表单验证规则
const registerRules = {
  username: [
    { required: true, message: t('patientRegister.usernameRequired') },
    {
      pattern: /^[a-zA-Z_][a-zA-Z0-9_]{3,19}$/,
      message: t('patientRegister.usernameInvalid'),
    },
    { validator: validateUsername, trigger: 'blur' },
  ],
  password: [
    { required: true, message: t('patientRegister.passwordRequired') },
    {
      pattern: /^(?=.*[a-zA-Z])(?=.*\d).{6,20}$/,
      message: t('patientRegister.passwordInvalid'),
    },
  ],
  confirmPassword: [
    { required: true, message: t('patientRegister.confirmPasswordRequired') },
    {
      validator: (_rule: any, value: string) => {
        if (value !== registerForm.password) {
          return Promise.reject(t('patientRegister.confirmPasswordMismatch'));
        }
        return Promise.resolve();
      },
      trigger: 'blur',
    },
  ],
  name: [
    { required: true, message: t('patientRegister.nameRequired') },
    { min: 2, max: 20, message: t('patientRegister.nameLength') },
    { pattern: /^[\u4e00-\u9fa5]+$/, message: t('patientRegister.nameInvalid') },
  ],
  birthday: [
    {
      required: true,
      message: t('patientRegister.birthdayRequired'),
      validator: (_rule: any, value: Dayjs | null) => {
        if (!value) {
          return Promise.reject(t('patientRegister.birthdayRequired'));
        }
        return Promise.resolve();
      },
      trigger: 'change',
    },
  ],
  phone: [
    {
      pattern: /^1[3-9]\d{9}$/,
      message: t('patientRegister.phoneInvalid'),
    },
  ],
  gender: [{ required: true, message: t('patientRegister.genderRequired') }],
};

// 用户名失焦时检查唯一性
const checkUsernameUnique = async () => {
  if (!registerForm.username || !/^[a-zA-Z_][a-zA-Z0-9_]{3,19}$/.test(registerForm.username)) {
    return;
  }

  try {
    const response = await checkUsername(registerForm.username);
    if (response.data.exists) {
      usernameStatus.value = 'error';
      usernameHelp.value = t('patientRegister.usernameExists');
    } else {
      usernameStatus.value = 'success';
      usernameHelp.value = t('patientRegister.usernameAvailable');
    }
  } catch (error) {
    usernameStatus.value = '';
    usernameHelp.value = '';
  }
};

const clearUsernameError = () => {
  if (usernameStatus.value === 'error') {
    usernameStatus.value = '';
    usernameHelp.value = '';
  }
};

const handleRegister = async () => {
  loading.value = true;
  errorMessage.value = '';

  try {
    const birthday = registerForm.birthday?.format('YYYY-MM-DD');
    if (!birthday) {
      message.error(t('patientRegister.birthdayRequired'));
      loading.value = false;
      return;
    }

    const response = await register({
      username: registerForm.username,
      password: registerForm.password,
      name: registerForm.name,
      birthday: birthday,
      phone: registerForm.phone,
      gender: registerForm.gender,
    });

    if (response.code === 200) {
      // 保存登录状态到 localStorage 和 store
      store.loginPatient(response.data);
      message.success(t('patientRegister.registerSuccess'));

      // 跳转到问诊页面
      router.push('/consultation');
    } else {
      errorMessage.value = response.message || t('patientRegister.registerFailed');
    }
  } catch (error: any) {
    console.error('Register error:', error);
    if (error.response?.data?.message) {
      errorMessage.value = error.response.data.message;
    } else {
      errorMessage.value = t('patientRegister.networkError');
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.patient-register {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 24px;
}

.register-container {
  width: 100%;
  max-width: 500px;
}

.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 48px 40px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  max-height: 90vh;
  overflow-y: auto;
}

.register-header {
  text-align: center;
  margin-bottom: 32px;
}

.register-icon {
  font-size: 56px;
  color: #667eea;
  margin-bottom: 16px;
}

.register-header h1 {
  font-size: 28px;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.register-header p {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.password-strength {
  margin-top: 8px;
  font-size: 12px;
}

.strength-weak {
  color: #ff4d4f;
}

.strength-medium {
  color: #faad14;
}

.strength-strong {
  color: #52c41a;
}

.register-footer {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #666;
}

.register-footer a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-card {
    padding: 32px 24px;
  }

  .register-header h1 {
    font-size: 24px;
  }
}
</style>
