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
  <!-- Search form -->
<form class="form-inline" @submit.prevent="searchVM">
  <i class="fas fa-search" aria-hidden="false"></i>
  <input v-model="search.name" class="form-control form-control-sm ml-3 w-55" type="text" placeholder="Search"
    aria-label="Search">
    <div>
    <div>
    <input min = 0 v-model="search.cpuLow" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Cpu low">
    <input  min = 0 v-model="search.cpuHigh" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Cpu high">
    </div>
    <div>
    <input min = 0 v-model="search.gpuLow" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Gpu low">
    <input min = 0 v-model="search.gpuHigh" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Ggpu high">
    </div>
    <div>
    <input min = 0 v-model="search.ramLow" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Ram low">
    <input min = 0 v-model="search.ramHigh" class="form-control form-control-sm ml-3 w-55" type="number" placeholder="Ram high">
    </div>
    </div>
    <div>
    <button min = 0 type="submit" class="btn btn-primary form-control form-control-sm ml-3 w-55">Search</button>
    </div>
</form>
</div>
</template>

<script>
import Form from '../forms/vmFormAdd'
import { GET_VMS, SEARCH_VM } from '../../actions'
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
          userRole: store.getters.role,
          search : {
            name : null,
            cpuLow: null,
            cpuHigh : null,
            gpuLow: null,
            gpuHigh : null,
            ramLow: null,
            ramHigh : null,
          }
          }
      },
      computed: {
          vms() {
            return store.getters.vms
          }
      },
      mounted() {
        const data = { org : store.getters.org }
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
          },
     
        searchVM() {
          this.$store.dispatch(SEARCH_VM, this.search)
                .then( res => console.log(res))
                .catch(error => alert(error.response.data.msg))
        }
      }
  }
</script>

<style scoped>
.vm:hover {
  cursor: pointer;
}
</style>