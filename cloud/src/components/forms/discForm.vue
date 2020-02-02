<template>   
<div id="discForm" v-if="showModal">
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
                        <label for="name">Name</label>
                        <input
                            required
                            type="text"
                            id="name"
                            v-model="name"
                          />
                    </div>
                    <div class="input">
                        <label for="capacity">Capacity [GB]</label>
                        <input
                            min="1"
                            required
                            type="number"
                            id="capacity"
                            v-model="capacity">
                    </div>
                     <div class="input">
                        <label for="type">Type</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="type" 
                        >
                          <option>SSD</option>
                          <option>HDD</option>
                        </select>
                    </div>
                    <div v-if="!disc" class="input">
                        <label for="org">Organisation</label>
                        <select 
                          required
                          class="form-control" 
                          v-model="org" 
                         :disabled="disable"
                          :selected="selOrg">
                          <option v-for="o in organisations" v-bind:value="o.name" :key="o.name">
                           {{ o.name }}
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
import { ADD_DISC, EDIT_DISC, GET_ORGANISATIONS } from '../../actions'
import store from '../../store'
import { mapState } from 'vuex'

export default {
  name: 'discForm',
  props: ['showModal', 'disc'],
  data () {
    return {
      name: '',
      capacity: '',
      vm: '',
      type: '',
      org: '',
      title: "New disc",
      btnTitle: "Add disc",
      selOrg: '',
      selVm: '',
      disable: false
    }
  },
  mounted() {
    console.log('mounted od disc form')
    console.log('disc u discform', this.disc)
    if(this.disc) {
      this.setData()
    } else {
     if (store.getters.role === 'ADMIN') {
     //   this.org = store.getters.org
      //  this.selOrg = this.org
      //  this.disable = true
    }
    else {
      this.$store.dispatch(GET_ORGANISATIONS)
            .then( res => console.log(res))
            .catch(error => alert(error.response.data.msg))
      }
    }
    
  },
 computed:  mapState({
    organisations: state => state.organisations
  }),
  methods: {
    close() {
      this.$emit('closeModal')
      if (!this.disc)
        this.resetData()
    },
    setData() {
      this.name = this.disc.name
      this.capacity = this.disc.capacity
      this.vm = this.disc.vm.name
      this.org = this.disc.org.name,
      this.type = this.disc.type,
      this.title = "Edit disc"
      this.btnTitle = "Save changes"
    },
    resetData() {
      this.name = ''
      this.capacity = ''
      this.type = ''
      this.org = ''
      this.vm = ''
    },
    storeData() {
      const data = { 
                name: this.name, 
                capacity: this.capacity,
                type: this.type,
                org: this.org,
                vm: this.vm
                }
      const action = this.disc? EDIT_DISC: ADD_DISC
      this.$store.dispatch(action, data)
      .then( res => {
          if (action === EDIT_DISC)
            this.$router.push('/discs')
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