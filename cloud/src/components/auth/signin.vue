<template>
  <div id="signin">
    <div class="signin-form">
      <form @submit.prevent="login">
        <div class="input">
          <label for="email">Mail</label>
          <input
                  required
                  type="email"
                  id="email"
                  v-model="email">
        </div>
        <div class="input">
          <label for="password">Password</label>
          <input
                  required
                  type="password"
                  id="password"
                  v-model="password">
        </div>
        <div class="submit">
          <button type="submit">Submit</button>
        </div>
      </form>
     <div class="alert alert-danger" role="alert" v-if="loginFailed">
       Not valid credentials!
    </div>
    </div>
  </div>
</template>

<script>
import { AUTH_REQUEST } from '../../actions'

  export default {
    data () {
      return {
        email: '',
        password: '',
        loginFailed : false
      }
    },
    methods: {
      login() {
        const data = { email: this.email, password: this.password }
        this.$store.dispatch(AUTH_REQUEST, data)
        .then( res => {
          this.$router.push('/dashboard');
        })
        .catch(error => this.loginFailed = true)
      }
    }
  }
</script>

<style scoped>
 @import '../../css/signin.css';
</style>