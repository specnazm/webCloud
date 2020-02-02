<template>
<div id="userMenu">
<UserInfo/>
   <ul class="sidebar-panel-nav">
    <li><router-link 
            to="/users" 
             v-if="role !== 'USER'"
             @click.native="closeSideBar"
           >
            Users
            </router-link></li>
    <li><router-link 
            to="/organisations"  
            v-if="role !== 'USER'"
             @click.native="closeSideBar"
            >
            Organizations
         </router-link>
   </li>
    <li><router-link 
            to="/categories"  
            v-if="role === 'SUPER_ADMIN'"
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
import {  mapGetters } from 'vuex';

export default {
        name: "userMenu",
        components: {
                UserInfo
        },
        methods: {
                closeSideBar() {
                         this.$emit('toggle')
                }
        },
         computed: mapGetters([
                'role',
        ])
        }
</script>

<style scoped>
   @import '../../css/menu.css';
</style>