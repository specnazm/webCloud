<template>
  <div id="app">
    <app-header :isAuth="isAuth"/>
    <router-view 
      :isAuth="isAuth" 
      :userRole="userRole"
      ></router-view>
  </div>
</template>

<script>
  import Header from './components/home/header'
  export default {
    name: 'app',
    data : function() {
      return {
        isAuth: false,
        userRole : String
      }
    },
    components: {
      'app-header': Header
    }
  }
</script>

<style>
</style>