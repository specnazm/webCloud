<template>
  <div id="app">
    <app-header />
    <router-view></router-view>
  </div>
</template>

<script>
  import Header from './components/home/header'
  export default {
    name: 'app',
    components: {
      'app-header': Header
    }
  }
</script>

<style>
</style>