<template>
  <header id="header" class="navbar navbar-dark bg-primary">
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
      </ul>
    </nav>
  </header>
</template>

<script>
import { AUTH_LOGOUT } from '../../mutationTypes'

export default {
 computed: {
   isAuth() {
    return this.$store.getters.isAuth;
   }
  },
  methods : {
    logout() {
      this.$store.dispatch(AUTH_LOGOUT).then(() => this.$router.push('/sigin'))
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