<template>   
<div id="vmForm" v-if="showModal">
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Add new virtual machine</h5>
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
                    <div  class="input">
                        <label for="org">Organisation</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="org" 
                         :disabled="disable"
                          :selected="selOrg"
                          v-on:change="onChange($event)">
                          <option v-for="o in organisations" v-bind:value="o.name" :key="o.name">
                           {{ o.name }}
                          </option>
                        </select>
                    </div>
                    <div class="input">
                        <label for="org">Category</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="category" 
                          >
                          <option v-for="c in categories" v-bind:value="c.name" :key="c.name">
                           {{ c.name }}
                          </option>
                        </select>
                    </div>
                     <div  class="input">
                        <label for="org">Disc</label>
                        <select  
                          multiple
                          class="form-control" 
                          v-model="discs" 
                          :selected="selDiscs">
                          <option v-for="d in discList" v-bind:value="d.name" :key="d.name">
                           {{ d.name }}
                          </option>
                        </select>
                    </div>
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">Add VM</button>
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
import { ADD_VM, GET_ORGANISATIONS, GET_DISCS, GET_CATEGORIES } from '../../actions'
import store from '../../store'
import { mapState } from 'vuex'

export default {
  name: 'vmForm',
  props: ['showModal', 'disc'],
  data () {
    return {
      name: '',
      org: '',
      discs: [],
      category: '',
      selOrg: '',
      selDiscs: '',
      disable: false
    }
  },
  mounted() {
     this.$store.dispatch(GET_CATEGORIES)
            .then( res => console.log(res))
            .catch(error => alert(error.response.data.msg))

     if (store.getters.role === 'ADMIN') {
       this.org = store.getters.org
       this.selOrg = this.org
       this.disable = true
       this.getDiscs({ org: this.org })
    }
    else if (store.getters.role === 'SUPER_ADMIN') {
      this.$store.dispatch(GET_ORGANISATIONS)
            .then( res => console.log(res))
            .catch(error => alert(error.response.data.msg))
      }
    },
 computed:  mapState({
    organisations: state => state.organisations,
    discList : state => state.discs,
    categories: state => state.categories
  }),
  methods: {
    close() {
      this.$emit('closeModal')
      if (!this.disc)
        this.resetData()
    },
    resetData() {
      this.name = ''
      this.org = ''
      this.discs = []
      this.category = []
      this.selOrg = ''
      this.selDiscs = ''
      this.disable = false
    },
    storeData() {
      const data = { 
               name: this.name,
                org: this.org,
                discs : this.discs,
                category: this.category
              }
  
      this.$store.dispatch(ADD_VM, data)
      .then( res => this.close())
      .catch(error => alert(error.response.data.msg)) 
  },
    onChange(event) {
      const data = { org: event.target.value }
      this.getDiscs(data)
    },
    getDiscs(data) {
      this.$store.dispatch(GET_DISCS, data)
        .then( res => console.log(res))
        .catch(error => alert(error.response.data.msg))
    }
}
}
</script>

<style scoped>
@import '../../css/form.css';
</style>