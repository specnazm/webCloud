<template>
<div id="discList" class="container">
    <Form 
      :showModal="showModal" 
      @closeModal="closeModal"/>
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button 
                        v-if="userRole !== 'USER'"
                        class="btn btn-info" 
                        id="list" 
                        @click="showModal = true">
                        Add new disc
                    </button>
                </div>
            </div>
        </div>
    </div> 
<table class="table table-hover" >
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Name</th>
      <th scope="col">Capacity</th>
      <th scope="col">Virtual machine</th>
      <th scope="col">Organisation</th>
    </tr>
  </thead>
  <tbody class="disc" v-for="(d, ind) in discs" :key="d.name" @click="discInfo(d)">
    <tr>
      <th scope="row">{{index(ind)}}</th>
      <td>{{d.name}}</td>
      <td>{{d.capacity}}</td>
      <td>{{d.vm.name}}</td>
      <td>{{d.org.name}}</td>
    </tr>
  </tbody>
</table>
</div>
</template>

<script>
import Form from '../forms/discForm'
import { GET_DISCS } from '../../actions'
import store from '../../store'
import { SET_DISC } from '../../mutations'

  export default {
      name: "discList",
      components: {
        Form
      },
      data : function() {
          return {
          showModal : false,
          userRole: store.getters.role
          }
      },
      computed: {
          discs() {
            return this.$store.getters.discs
          }
      },
      mounted() {
          // this.$store.dispatch(GET_DISCS)
          //     .then( res => console.log(res))
          //     .catch(error => alert(error.response.data.msg))
      },
      methods: {
          closeModal() {
              this.showModal = false
          },
          index(ind) {
            return ind + 1
          },
          discInfo(disc) {
            store.commit(SET_DISC, disc)
            this.$router.push(`/disc/${disc.name}`)
          }
      }
  }
</script>

<style scoped>
.disc:hover {
  cursor: pointer;
}
</style>