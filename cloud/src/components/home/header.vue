<template>
<div>
  <div id="header" class="navbar navbar-dark bg-dark">
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
          <a class="logout" @click="logout">Log out</a>
        </li>
        <li v-if="this.$store.getters.isAuth">
          <SidebarBtn :isOpen="isPanelOpen" @toggle="toggleSideBar"></SidebarBtn>
        </li>
      </ul>
    </nav>
  </div>
  <Sidebar :isOpen="isPanelOpen">
     <Menu :userRole="userRole" @toggle="toggleSideBar"/>
   </Sidebar>
  </div>
</template>

<script>
import { AUTH_LOGOUT } from '../../actions'
import SidebarBtn from './sidebarBtn'
import Sidebar from './Sidebar';
import Menu from '../menu/menu';

export default {
  components: {
   SidebarBtn,
   Sidebar,
   Menu
 },
 computed: {
   isAuth() {
    return this.$store.getters.isAuth
   }
  },
  data: function() {
    return {
      isPanelOpen: false,
      userRole: this.$store.getters.role 
      }
  },
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
  @import '../../css/header.css';
</style>