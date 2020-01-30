<template>
<div id="adminMenu">
    <UserInfo></UserInfo>
   <ul class="sidebar-panel-nav">
    <li><router-link to="/users">Users from {{orgName}}</router-link></li>
    <li><router-link to="/organization">Organization : {{orgName}}</router-link></li>
    <li><router-link to="/drives">Drives from {{orgName}}</router-link></li>
    <li><router-link to="/categories">Categories</router-link></li>
    <li><router-link to="/profile">Account settings</router-link></li>
    </ul>
</div>
</template>

<script>
import UserInfo from './userInfo'
import store from '../../store'

export default {
 name: "adminMenu",
 components: {
     UserInfo
 },
 computed : {
     orgName() {
        return this.$store.getters.org
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