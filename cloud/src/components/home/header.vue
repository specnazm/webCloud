<template>
<div>
  <header id="header" class="navbar navbar-dark bg-dark">
    <div class="logo">
      <router-link to="/">Home</router-link>
    </div>
    <nav>
      <ul>
        <li v-if="!isAuth">
          <router-link to="/signin">Sign In</router-link>
        </li>
         <li v-if="isAuth">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
         <li v-if="this.$store.getters.isAuth">
          <a @click="logout">Log out</a>
        </li>
        <li v-if="this.$store.getters.isAuth">
          <SidebarBtn :isOpen="this.isPanelOpen" @toggle="toggleSideBar"></SidebarBtn>
        </li>
      </ul>
    </nav>
  </header>
  <Sidebar :isOpen="isPanelOpen" :toggleSideBar="toggleSideBar">
     <SuperAdminMenu v-if="role === 'SUPER_ADMIN'"></SuperAdminMenu>
     <AdminMenu v-else-if="role === 'ADMIN'"></AdminMenu>
     <UserMenu v-else></UserMenu>
   </Sidebar>
  </div>
</template>

<script>
import { AUTH_LOGOUT } from '../../types'
import SidebarBtn from './sidebarBtn'
import Sidebar from './Sidebar';
import SuperAdminMenu from '../menu/superAdminMenu'
import AdminMenu from '../menu/adminMenu'
import UserMenu from '../menu/userMenu'

export default {
  components: {
   SidebarBtn,
   Sidebar,
   SuperAdminMenu,
   AdminMenu,
   UserMenu
 },
 computed: {
   isAuth() {
    return this.$store.getters.isAuth
   },
   role() {
     return this.$store.getters.role
   }
  },
  data: () => ({ isPanelOpen: false }),
  methods : {
    logout() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => this.$router.push('/signin'))
    },
    toggleSideBar() {
      this.isPanelOpen = !this.isPanelOpen
    }
  }
}
</script>
<style scoped>
  #header {
    height: 56px;
    display: flex;
    flex-flow: row;
    justify-content: space-between;
    align-items: center;
    background-color: #521751;
    padding: 0 20px;
  }

  .logo {
    font-weight: bold;
    color: white;
  }

  .logo a {
    text-decoration: none;
    color: white;
  }

  nav {
    height: 100%;
  }

  ul {
    list-style: none;
    margin: 0;
    padding: 0;
    height: 100%;
    display: flex;
    flex-flow: row;
    align-items: center;
  }

  li {
    margin: 0 16px;
  }

  li a {
    text-decoration: none;
    color: white;
  }

  li a:hover,
  li a:active,
  li a.router-link-active {
    color: #fa923f;
  }
</style>