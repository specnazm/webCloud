import Vue from 'vue'
import VueRouter from 'vue-router'

import store from './store'
import HomePage from './components/home/home.vue'
import DashboardPage from './components/dashboard/dashboard.vue'
import SigninPage from './components/auth/signin.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', component: HomePage },
  { 
    path: '/signin', 
    component: SigninPage,
    beforeEnter(to, from ,next) {
        store.getters.isAuth ? next('dashboard') : next();
    } 
  },
  { path: '/dashboard', component: DashboardPage }
]

const router =  new VueRouter({mode: 'history', routes, props : []});
router.beforeEach((to,from,next) => {
  if (to.path === '/' || to.path === '/signin' || store.getters.isAuth )
    next();
  else
     next('signin');
})
export default router;