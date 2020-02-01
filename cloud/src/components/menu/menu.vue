<template>
<div id="menu">
    <UserInfo></UserInfo>
   <ul class="sidebar-panel-nav">
    <li><router-link 
            to="/users" 
            v-if="role !== 'USER'"
            @click.native="toggleSideBar">
            Users
            </router-link></li>
    <li><router-link 
            to="/organisations"  
            v-if="role !== 'USER'"
            @click.native="toggleSideBar">
            Organizations
         </router-link>
   </li>
    <li><router-link 
            to="/categories"  
            v-if="role !== 'USER'"
            @click.native="toggleSideBar">
            Categories
            </router-link></li>
    <li><router-link to="/drives" @click.native="toggleSideBar">Drives</router-link></li>
    <li><router-link to="/me" @click.native="toggleSideBar">Account settings</router-link></li>
    </ul>
</div>
</template>

<script>
import UserInfo from './userInfo'
import { SET_USER } from '../../mutations'

export default {
 name: "menu",
 components: {
     UserInfo
 },
 computed : {
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
   @import '../../css/menu.css';
</style>