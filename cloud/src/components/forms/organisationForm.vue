<template>   
<div v-if="showModal">
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">New organisation</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true" @click="close">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <form @submit.prevent="addOrg">
                    <div class="input">
                        <label for="name">Name</label>
                        <input
                            required
                            type="text"
                            id="name"
                            v-model="name">
                    </div>
                    <div class="input">
                        <label for="description">Description</label>
                        <input
                            type="text"
                            id="description"
                            v-model="description">
                    </div>
                    <label for="logo">Upload logo</label>
                    <input 
                        type="file" 
                        id="logo"
                        @change="onFileChanged($event)"
                    />
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">Add organisation</button>
                            <button type="button" class="btn btn-secondary" @click="close">Cancel</button>
                        </div>
                </form>
                <div class="alert alert-danger" role="alert" v-if="nameNotUnique">
                    Name is not unique!
                </div>
            </div>  
        </div>
    </div>
    </div>
    </div>
    </transition>
  </div>
</template>

<script>
import { ADD_ORGANISATION } from '../../actions'

export default {
    props: ['showModal'],
     data () {
      return {
        name: '',
        description: '',
        logo: null,
        nameNotUnique: false
      }
    },
    methods: {
      close() {
        this.$emit('closeModal')
        this.resetData()
      },
      resetData() {
        this.name = ''
        this.description = ''
        this.logo = null
        this.nameNotUnique = false
    },
      onFileChanged (event) {
       const file = event.target.files[0]
       this.logo  = {
        'lastModified'     : file.lastModified,
        'lastModifiedDate' : file.lastModifiedDate,
        'name'             : file.name,
        'size'             : file.size,
        'type'             : file.type
      }  
      },
      addOrg() {
        console.log('ovde' , this.logo)
        const data = { 
                    name: this.name, 
                    description: this.description, 
                    logo: this.logo
                    }
        this.$store.dispatch(ADD_ORGANISATION, data)
        .then( res => {
            this.close()
        })
        .catch(error => console.log(error))
    }
    },
    beforeRouteEnter(to,from,next) {
        const role = this.$state.getters.role
        if ( role !== 'SUPER_ADMIN' && role !== 'ADMIN') {
            next('/dashboard')
        } else {
            next()
        }
    }  
}
</script>

<style scoped>
@import '../../css/organisationForm.css';
</style>