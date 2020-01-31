<template>
<div id="menu">
    <UserInfo></UserInfo>
   <ul class="sidebar-panel-nav">
    <li><router-link to="/users" v-if="role !== 'USER'">Users</router-link></li>
    <li><router-link 
            to="/organisations"  
            v-if="role !== 'USER'"
            @click.native="toggleSideBar"
            >
            Organizations
         </router-link>
   </li>
    <li><router-link to="/categories"  v-if="role !== 'USER'">Categories</router-link></li>
    <li><router-link to="/drives">Drives</router-link></li>
    <li><router-link to="/profile">Account settings</router-link></li>
    </ul>
</div>
</template>

<script>
import UserInfo from './userInfo'

export default {
 name: "menu",
 components: {
     UserInfo
 },
 computed : {
     orgName() {
        return this.$store.getters.org
     },
     role() {
     return this.$store.getters.role
   }
},
methods: {
         toggleSideBar() {
            this.$emit('toggle')
         }
        }
}
</script>

<style scoped>
ul.sidebar-panel-nav {
   list-style-type: none;
    display: table-cell;
 }

ul.sidebar-panel-nav > li  {
   color: #fff;
   text-decoration: none;
   font-size: 1.5rem;
   display: block;
   padding-bottom: 0.5em;
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