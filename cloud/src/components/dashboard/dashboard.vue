<template>
<div id="vms" class="container">
    <Form :showModal="showModal" @closeModal="closeModal"/>
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button  v-if="userRole !== 'USER'" class="btn btn-info" id="list" @click="showModal = true">
                        Add new VM
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
      <th scope="col">Cpu cores</th>
      <th scope="col">Gpu cores</th>
      <th scope="col">Ram memory</th>
      <th scope="col">Organisation</th>
    </tr>
  </thead>
  <tbody class="vm" v-for="(vm, ind) in vms" :key="vm.name" @click="vmInfo(vm)">
    <tr>
      <th scope="row">{{index(ind)}}</th>
      <td>{{vm.name}}</td>
      <td>{{vm.cpuCores}}</td>
      <td>{{vm.gpuCores}}</td>
      <td>{{vm.ram}}</td>
      <td>{{vm.org}}</td>
    </tr>
  </tbody>
</table>
</div>
</template>

<script>
import Form from '../forms/vmForm'
import { GET_VMS } from '../../actions'
import store from '../../store'
import { SET_VM } from '../../mutations'

  export default {
      name: "vms",
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
          vms() {
            return store.getters.vms
          }
      },
      mounted() {
        const data = { org : store.getters.org }
        console.log(data)
          this.$store.dispatch(GET_VMS, data)
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
          vmInfo(vm) {
            store.commit(SET_VM, vm)
            this.$router.push(`/vm/${vm.name}`)
          }
      }
  }
</script>

<style scoped>
.vm:hover {
  cursor: pointer;
}
</style>