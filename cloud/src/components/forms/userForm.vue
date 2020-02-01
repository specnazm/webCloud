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
                            type="text"
                            id="surname"
                            v-model="surname">
                    </div>
                    <div class="input">
                        <label for="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            v-model="password">
                    </div>
                     <div class="input">
                        <label for="organisation">Organisation</label>
                        <select 
                          class="form-control" 
                          v-model="organisation" 
                          :disabled="disable"
                          :selected="selected">
                          <option v-for="org in organisations" v-bind:value="org.name" :key="org.name">
                           {{ org.name }}
                          </option>
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
import { GET_ORGANISATIONS, ADD_USER } from '../../actions'
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
    watch: {
      user() {
        this.setData()
      }
    },
     computed: {
            organisations() {
                return this.$store.getters.orgs
            }
        },
    methods: {
      close() {
        this.$emit('closeModal')
        if (!this.user)
          this.resetData()
      },
      setData() {
        this.name = this.org.name
        this.description = this.org.desc
        this.logo = this.org.logo_url
        this.title = "Edit user"
        this.btnTitle = "Save changes"
      },
      resetData() {
        this.name = ''
        this.description = ''
        this.logo = null
        this.nameNotUnique = false
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
        let action = ADD_USER
        if (this.org) {
          action = EDIT_ORGANISATION
          data.oldName = this.org.name
        }
        this.$store.dispatch(action, data)
        .then( res => {
            if (action === EDIT_ORGANISATION)
              this.$router.push('/organisations')
            this.close()
        })
        .catch(error => console.log(error))
    }
    }
}
</script>

<style scoped>
@import '../../css/organisationForm.css';
</style>