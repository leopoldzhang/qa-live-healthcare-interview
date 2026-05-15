import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Home from '../views/Home.vue';
import Consultation from '../views/Consultation.vue';
import DoctorLogin from '../views/DoctorLogin.vue';
import DoctorRoom from '../views/DoctorRoom.vue';
import Doctors from '../views/Doctors.vue';
import About from '../views/About.vue';
import PatientLogin from '../views/PatientLogin.vue';
import PatientRegister from '../views/PatientRegister.vue';
import AppointmentList from '../views/appointment/AppointmentList.vue';
import DoctorSchedule from '../views/appointment/DoctorSchedule.vue';
import AddAppointment from '../views/appointment/AddAppointment.vue';

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
  {
    path: '/appointments',
    name: 'AppointmentList',
    component: AppointmentList,
    meta: { requiresAuth: true },
  },
  {
    path: '/doctor/schedule',
    name: 'DoctorSchedule',
    component: DoctorSchedule,
    meta: { requiresDoctor: true },
  },
  {
    path: '/appointments/add',
    name: 'AddAppointment',
    component: AddAppointment,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫 - 检查登录状态
router.beforeEach((to, _from, next) => {
  const requiresAuth = to.meta.requiresAuth;
  const requiresDoctor = to.meta.requiresDoctor;
  const currentPatient = localStorage.getItem('currentPatient');
  const currentDoctor = localStorage.getItem('currentDoctor');

  if (requiresDoctor && !currentDoctor) {
    // 需要医生登录但未登录，跳转到医生登录页
    next({
      path: '/doctor/login',
      query: { redirect: to.fullPath },
    });
  } else if (requiresAuth && !currentPatient) {
    // 需要患者登录但未登录,跳转到登录页
    next({
      path: '/patient/login',
      query: { redirect: to.fullPath },
    });
  } else if ((to.path === '/patient/login' || to.path === '/patient/register') && currentPatient) {
    // 已登录患者访问登录/注册页,跳转到预约列表页
    next('/appointments');
  } else if (to.path === '/doctor/login' && currentDoctor) {
    // 已登录医生访问登录页,跳转到排班页
    next('/doctor/schedule');
  } else {
    next();
  }
});

export default router;
