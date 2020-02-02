<template>
<div id="userMenu">
<UserInfo/>
   <ul class="sidebar-panel-nav">
    <li><router-link 
            to="/users" 
             v-if="userRole !== 'USER'"
             @click.native="closeSideBar"
           >
            Users
            </router-link></li>
    <li><router-link 
            to="/organisations"  
            v-if="userRole !== 'USER'"
             @click.native="closeSideBar"
            >
            Organizations
         </router-link>
   </li>
    <li><router-link 
            to="/categories"  
            v-if="userRole === 'SUPER_ADMIN'"
           @click.native="closeSideBar"
           >
            Categories
            </router-link></li>
    <li><router-link to="/discs" @click.native="closeSideBar">Discs</router-link></li>
    <li><router-link to="/me" @click.native="closeSideBar">Account settings</router-link></li>
    </ul>
</div>
</template>

<script>
import UserInfo from './userInfo'
import { SET_USER } from '../../mutations'

export default {
        name: "userMenu",
        components: {
                UserInfo
        },
        props : ['userRole'],
        methods: {
                closeSideBar() {
                         this.$emit('toggle')
                }
        }
        }
</script>

<style scoped>
   @import '../../css/menu.css';
</style>