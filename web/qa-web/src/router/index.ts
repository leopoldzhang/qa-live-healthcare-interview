import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Home from '../views/Home.vue';
import Consultation from '../views/Consultation.vue';
import DoctorLogin from '../views/DoctorLogin.vue';
import DoctorRoom from '../views/DoctorRoom.vue';
import Doctors from '../views/Doctors.vue';
import About from '../views/About.vue';
import PatientLogin from '../views/PatientLogin.vue';
import PatientRegister from '../views/PatientRegister.vue';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/consultation',
    name: 'Consultation',
    component: Consultation,
    meta: { requiresAuth: true },
  },
  {
    path: '/consultation/:doctorUsername',
    name: 'ConsultationRoom',
    component: Consultation,
    meta: { requiresAuth: true },
  },
  {
    path: '/doctors',
    name: 'Doctors',
    component: Doctors,
  },
  {
    path: '/about',
    name: 'About',
    component: About,
  },
  {
    path: '/doctor/login',
    name: 'DoctorLogin',
    component: DoctorLogin,
  },
  {
    path: '/doctor/room/:username',
    name: 'DoctorRoom',
    component: DoctorRoom,
  },
  {
    path: '/patient/login',
    name: 'PatientLogin',
    component: PatientLogin,
  },
  {
    path: '/patient/register',
    name: 'PatientRegister',
    component: PatientRegister,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫 - 检查患者登录状态
router.beforeEach((to, _from, next) => {
  const requiresAuth = to.meta.requiresAuth;
  const currentPatient = localStorage.getItem('currentPatient');

  if (requiresAuth && !currentPatient) {
    // 需要登录但未登录,跳转到登录页
    next({
      path: '/patient/login',
      query: { redirect: to.fullPath },
    });
  } else if ((to.path === '/patient/login' || to.path === '/patient/register') && currentPatient) {
    // 已登录用户访问登录/注册页,跳转到问诊页
    next('/consultation');
  } else {
    next();
  }
});

export default router;
