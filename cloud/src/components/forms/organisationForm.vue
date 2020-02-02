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
                        <label for="name">Name</label>
                        <input
                            required
                            type="text"
                            id="name"
                            v-model="name">
                    </div>
                    <div class="input">
                        <label for="desc">Description</label>
                        <input
                            type="text"
                            id="desc"
                            v-model="desc">
                    </div>
                    <label for="logo">Upload logo</label>
                    <input 
                        type="file" 
                        id="logo"
                        @change="onFileChanged($event)"
                    />
                    <div class="modal-footer">
                            <button type="submit" class="btn btn-primary addOrg">{{btnTitle}}</button>
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
import { ADD_ORGANISATION, EDIT_ORGANISATION } from '../../actions'
import store from '../../store'
import { mapState } from 'vuex'

export default {
    props: ['showModal', 'org'],
     data () {
      return {
        name: '',
        desc: '',
        logo: null,
        nameNotUnique: false,
        title: "New organisation",
        btnTitle: "Add organisation",
      }
    },
    mounted() {
      if(this.org) {
        this.setData()
      }
    },
    methods: {
      close() {
        this.$emit('closeModal')
        if (!this.org)
          this.resetData()
      },
      setData() {
        this.name = this.org.name
        this.desc = this.org.desc
        this.logo = this.org.logo_url
        this.title = "Edit organisation"
        this.btnTitle = "Save changes"
      },
      resetData() {
        this.name = ''
        this.desc = ''
        this.logo = null
        this.nameNotUnique = false
    },
      onFileChanged (event) {
       const file = event.target.files[0]
     /*  this.logo  = {
        'lastModified'     : file.lastModified,
        'lastModifiedDate' : file.lastModifiedDate,
        'name'             : file.name,
        'size'             : file.size,
        'type'             : file.type
      }  */
      this.logo = file.name
      },
      storeData() {
        const data = { 
                    name: this.name, 
                    desc: this.desc, 
                    logo: this.logo
                    }
        let action = ADD_ORGANISATION
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
        .catch(error => alert(error.response.data.msg))
    }
    }
}
</script>

<style scoped>
@import '../../css/form.css';
</style>