import Vue from 'vue'
import VueRouter from 'vue-router'

import App from './App.vue'
import HomePage from './components/home/home.vue'
import DashboardPage from './components/dashboard/dashboard.vue'
import SigninPage from './components/auth/signin.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', component: HomePage },
  { path: '/signin', component: SigninPage },
  { 
    path: '/dashboard', 
    component: DashboardPage,
    beforeEnter(to, from, next) {
        if(App.isAuth) {
            next()
        }else{
            next('signin')
        }
    } }
]

export default new VueRouter({mode: 'history', routes})