<template>   
<div v-if="showModal">
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">{{title}}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true" @click="close">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <form @submit.prevent="storeData">
                   <div v-if="!user" class="input">
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
                     <div v-if="!user" class="input">
                        <label for="organisation">Organisation</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="organisation" 
                          :disabled="disable"
                          :selected="selected">
                          <option v-for="org in organisations" v-bind:value="org.name" :key="org.name">
                           {{ org.name }}
                          </option>
                        </select>
                    </div>
                    <div class="input">
                        <label for="role">Role</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="role" 
                        >
                          <option>ADMIN</option>
                          <option>USER</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">{{btnTitle}}</button>
                            <button type="button" class="btn btn-secondary" @click="close">Cancel</button>
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
import { GET_ORGANISATIONS, ADD_USER, EDIT_USER } from '../../actions'
import store from '../../store'

export default {
  props: ['showModal', 'user'],
  data () {
    return {
      email: '',
      name: '',
      surname: '',
      password: '',
      organisation: '',
      role : '',
      title: "New user",
      btnTitle: "Add user",
      selected: "",
      disable: false
    }
  },
  mounted() {
    if(this.user) {
      this.setData()
    }
    if (store.getters.role === 'ADMIN') {
      this.organisation = store.getters.org
      this.selected = this.organisation
      this.disable = true
    }
    else {
      this.$store.dispatch(GET_ORGANISATIONS)
                .then( res => console.log(res))
                .catch(error => alert(error.response.data.msg))
      }
  },
  computed: {
    organisations() {
        return store.getters.orgs
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
      this.role = this.user.role
      this.title = "Edit user"
      this.btnTitle = "Save changes"
    },
    resetData() {
      this.email = ''
      this.name = ''
      this.surname = ''
      this.password = ''
      this.role = ''
    },
    storeData() {
      const data = { 
                email : this.email,
                name: this.name, 
                surname: this.surname,
                password: this.password,
                org: this.organisation,
                role: this.role
                }
      const action = this.user? EDIT_USER: ADD_USER
      this.$store.dispatch(action, data)
      .then( res => {
          if (action === EDIT_USER)
            this.$router.push('/users')
          this.close()
      })
      .catch(error => aler(error.response.data.msg)) 
  }
  }
}
</script>

<style scoped>
@import '../../css/form.css';
</style>