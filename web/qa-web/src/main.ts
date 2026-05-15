import { createApp } from 'vue';
import { createI18n } from 'vue-i18n';
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import './style.css';
import App from './App.vue';
import router from './router';
import zhCN from './locales/zh-CN';
import enUS from './locales/en-US';

// 自定义消息解析器，直接返回消息值而不进行链接格式解析
const messageResolver = (obj: any, path: string) => {
  const keys = path.split('.');
  let result = obj;
  for (const key of keys) {
    if (result && typeof result === 'object' && key in result) {
      result = result[key];
    } else {
      return null;
    }
  }
  return result;
};

const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('locale') || 'zh-CN',
  fallbackLocale: 'zh-CN',
  messages: {
    'zh-CN': zhCN,
    'en-US': enUS
  },
  missingWarn: false,
  fallbackWarn: false,
  warnHtmlMessage: false,
  // 禁用消息编译，避免 @ 符号被解析
  messageResolver: messageResolver,
  // 设置为空消息编译器以禁用链接格式解析
  messageCompiler: (message: string) => (_ctx: any) => message
});

const app = createApp(App);

app.use(Antd);
app.use(router);
app.use(i18n);
app.mount('#app');
