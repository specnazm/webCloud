import Vue from 'vue'
import VueRouter from 'vue-router'

import store from './store'
import HomePage from './components/home/home.vue'
import DashboardPage from './components/dashboard/dashboard.vue'
import SigninPage from './components/auth/signin.vue'
import OrganisationList from './components/organisations/OrganisationList'
import OrganisationPage from './components/organisations/OrganisationPage'
import UserList from './components/users/userList'
import UserPage from './components/users/userPage'
import UserProfilePage from './components/users/userProfilePage'
import CategoryList from './components/categories/CategoryList'
import CategoryPage from './components/categories/CategoryPage'
import DiscList from './components/discs/discList'
import DiscPage from './components/discs/discPage'

import VMList from './components/dashboard/dashboard'
import VMPage from './components/dashboard/VMPage'

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
  { path: '/dashboard', component: DashboardPage },

  { path: '/organisations', component: OrganisationList },
  { path: '/organisation/:name', component: OrganisationPage },

  { path: '/users', component: UserList },
  { path: '/user/:email', component: UserPage },
  { path: '/me', component: UserProfilePage },

  { path: '/categories', component: CategoryList },
  { path: '/category/:name', component: CategoryPage },

  { path: '/discs', component: DiscList },
  { path: '/disc/:name', component: DiscPage },

  { path: '/vms', component: VMList },
  { path: '/vm/:name', component: VMPage },

]

const router =  new VueRouter({mode: 'history', routes, props : []});
router.beforeEach((to,from,next) => {
  if (to.path === '/' || to.path === '/signin' || store.getters.isAuth )
    next();
  else
     next('signin');
})
export default router;