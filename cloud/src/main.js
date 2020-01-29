import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'

import router from './router.js'


axios.defaults.baseURL = 'http://localhost:8079/api'
axios.defaults.headers.get['Accepts'] = 'application/json'

new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
