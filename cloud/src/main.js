import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'

import router from './router.js'


axios.defaults.baseURL = 'http://localhost:8079/api'
axios.defaults.headers.common['Authorization'] = 'fasfdsa'
axios.defaults.headers.get['Accepts'] = 'application/json'
axios.defaults.headers.common['milena'] = '*'


const reqInterceptor = axios.interceptors.request.use(config => {
  console.log('Request Interceptor', config)
  return config
})
const resInterceptor = axios.interceptors.response.use(res => {
  console.log('Response Interceptor', res)
  return res
})

new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
