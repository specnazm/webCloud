import axios from './axios'


export default {
    login(data) {
        axios.post('/auth/login', data)
          .then(res => this.$router.push('dashboard'))
          .catch(error => console.log(error))
    },
    logout() {
        axios.post('/auth/logout')
           .then(res =>  this.$router.push('signin'))
           .catch(error => console.log(error))
    } 
}