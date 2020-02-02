<template>
<div id="users" class="container">
    <Form :showModal="showModal" @closeModal="closeModal"/>
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button class="btn btn-info" id="list" @click="showModal = true">
                        Add new user
                    </button>
                </div>
            </div>
        </div>
    </div> 
<table class="table table-hover" >
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Email</th>
      <th scope="col">Name</th>
      <th scope="col">Surname</th>
      <th scope="col">Organisation</th>
    </tr>
  </thead>
  <tbody class="user" v-for="(user, ind) in users" :key="user.email" @click="userInfo(user)">
    <tr>
      <th scope="row">{{index(ind)}}</th>
      <td>{{user.email}}</td>
      <td>{{user.name}}</td>
      <td>{{user.surname}}</td>
      <td>{{user.org}}</td>
    </tr>
  </tbody>
</table>
</div>
</template>

<script>
import Form from '../forms/userForm'
import { GET_USERS } from '../../actions'
import store from '../../store'
import { SET_USER } from '../../mutations'

  export default {
      name: "users",
      components: {
        Form
      },
      data : function() {
          return {
          showModal : false
          }
      },
      computed: {
          users() {
            return this.$store.getters.users
          }
      },
      mounted() {
        console.log('mounted')
          this.$store.dispatch(GET_USERS)
              .then( res => console.log(res))
              .catch(error => alert(error.response.data.msg))
      },
      methods: {
          closeModal() {
              this.showModal = false
          },
          index(ind) {
            return ind + 1
          },
          userInfo(user) {
            store.commit(SET_USER, user)
            this.$router.push(`/user/${user.email}`)
          }
      },
       beforeRouteEnter(to,from,next) {
            const role = store.getters.role
            if ( role !== 'SUPER_ADMIN' && role !== 'ADMIN') {
                next('/dashboard')
            } else {
                next()
            }
        }  
  }
</script>

<style scoped>
.user:hover {
  cursor: pointer;
}
</style>