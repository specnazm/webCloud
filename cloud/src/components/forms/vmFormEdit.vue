<template>   
<div id="vmFormEdit" v-if="showModal">
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Edit virtual machine</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true" @click="close">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <form @submit.prevent="storeData">
                    <div class="input">
                        <label for="name">Name</label>
                        <input
                            required
                            type="text"
                            id="name"
                            v-model="name"
                          />
                    </div>
                    <div class="input">
                        <label for="org">Category</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="category" 
                          >
                          <option v-for="c in categories" v-bind:value="c" :key="c.name">
                           {{ c.name }}
                          </option>
                        </select>
                    </div>
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">Edit VM</button>
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
import { ADD_VM, GET_ORGANISATIONS, GET_DISCS, GET_CATEGORIES, EDIT_VM } from '../../actions'
import store from '../../store'
import { mapState } from 'vuex'

export default {
  name: 'vmFormEdit',
  props: ['showModal', 'vm'],
  data () {
    return {
      disableLogSelect : false,
      name: '',
      org: '',
      category: '',
      log: []
    }
  },
  mounted() {
    this.name = this.vm.name
    this.category = this.vm.category
    this.log = this.vm.log

     this.$store.dispatch(GET_CATEGORIES)
            .then( res => console.log(res))
            .catch(error => alert(error.response.data.msg))
      if (store.getters.role === 'ADMIN') {
        this.disableLogSelect = true
        this.log = vm.log
      }
   
    },
 computed:  mapState({
    categories: state => state.categories
  }),
  methods: {
    close() {
      this.$emit('closeModal')
    },
    convertArrayToObject(array, key) {
      const initialValue = {};
      return array.reduce((obj, item) => {
        return {
          ...obj,
          [item[key]]: item,
        };
      }, initialValue);
    },
    storeData() {
      const data = { 
               name: this.name,
                org: this.vm.org,
                discs : this.vm.discs,
                log: this.log,
                category: this.category.name,
                cpuCores: this.category.cpuCores,
                gpuCores: this.category.gpuCores,
                ram: this.category.ram,
                oldName: this.vm.name
              }
  
      this.$store.dispatch(EDIT_VM, data)
      .then( res => this.close())
      .catch(error => alert(error.response.data.msg)) 
  }
}
}
</script>

<style scoped>
@import '../../css/form.css';
</style>