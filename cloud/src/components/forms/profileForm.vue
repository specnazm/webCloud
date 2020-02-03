<template>   
<div v-if="showModal">
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Edit your profile</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true" @click="close">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <form @submit.prevent="storeData">
                   <div class="input">
                        <label for="email">Email</label>
                        <input
                            required
                            type="email"
                            id="email"
                            v-model="email">
                    </div>
                    <div class="input">
                        <label for="name">Name</label>
                        <input
                            required
                            type="text"
                            id="name"
                            v-model="name">
                    </div>
                    <div class="input">
                        <label for="surname">Surname</label>
                        <input
                            required
                            type="text"
                            id="surname"
                            v-model="surname">
                    </div>
                    <div class="input">
                        <label for="password">Password</label>
                        <input
                            required
                            type="password"
                            id="password"
                            v-model="password">
                    </div>
                      <div class="input">
                        <label for="passwordConfirm">Confirm password</label>
                        <input
                            required
                            type="password"
                            id="passwordConfirm"
                            v-model="passwordConfirm"
                          />
                    </div>
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">Save changes</button>
                            <button type="button" class="btn btn-secondary" @click="close">Cancel</button>
                        </div>
                      <div class="alert alert-danger" role="alert" v-if="passFailed">
                        Password dont match!
                    </div>
                </form>
            </div>  
        </div>
    </div>
    </div>
    </div>
    </transition>
  </div>
</template>

<script>
import { EDIT_PROFILE } from '../../actions'
import store from '../../store'

export default {
  props: ['showModal', 'user'],
  data () {
    return {
      email: '',
      name: '',
      surname: '',
      password: '',
      role: '',
      org : '',
      passwordConfirm: '',
      passFailed: false
    }
  },
  mounted() {
    if(this.user) {
      this.setData()
    }
  },
   watch: {
      user() {
        this.setData()
      }
   },
  methods: {
    close() {
      this.$emit('closeModal')
      if (!this.user)
        this.resetData()
    },
    setData() {
      this.email = this.user.email
      this.name = this.user.name
      this.surname = this.user.surname
      this.password = this.user.password
      this.passwordConfirm = this.user.password
      this.role = this.user.role,
      this.org = this.user.org
    },
    resetData() {
      this.email = ''
      this.name = ''
      this.surname = ''
      this.password = ''
      this.role = '',
      this.org = '',
      this.passwordConfirm = false
    },
    storeData() {
      if (this.password !== this.passwordConfirm) {
        this.passFailed = true
        return
      }
      const data = { 
                email : this.email,
                name: this.name, 
                surname: this.surname,
                password: this.password,
                org: this.org,
                role: this.role
                }
    
      this.$store.dispatch(EDIT_PROFILE, data)
      .then( res => {
          this.close()
      })
      .catch(error => alert(error.response.data.msg)) 
  },
  }
}
</script>

<style scoped>
@import '../../css/form.css';
</style>