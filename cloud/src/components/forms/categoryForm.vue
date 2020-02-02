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
                            v-model="name"
                          />
                    </div>
                    <div class="input">
                        <label for="cpuCores">Number of cpu cores</label>
                        <input
                            min="1"
                            required
                            type="number"
                            id="cpuCores"
                            v-model="cpuCores">
                    </div>
                    <div class="input">
                        <label for="gpuCores">Number of gpu cores</label>
                        <input
                            min="1"
                            required
                            type="number"
                            id="gpuCores"
                            v-model="gpuCores">
                    </div>
                    <div class="input">
                        <label for="ram">Ram memory [GB]</label>
                        <input
                             min="0"
                            required
                            type="number"
                            id="ram"
                            v-model="ram">
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
import { ADD_CATEGORY, EDIT_CATEGORY } from '../../actions'
import store from '../../store'
import { mapState } from 'vuex'

export default {
  name: 'categoryForm',
  props: ['showModal', 'category'],
  data () {
    return {
      name: '',
      cpuCores: '',
      gpuCores: '',
      ram: '',
      title: "New category",
      btnTitle: "Add category"
    }
  },
  mounted() {
    if(this.category) {
      this.setData()
    }
  },
  computed:  mapState({
    categories: state => state.categories
  }),
  methods: {
    close() {
      this.$emit('closeModal')
      if (!this.category)
        this.resetData()
    },
    setData() {
      this.name = this.category.name
      this.cpuCores = this.category.cpuCores
      this.gpuCores = this.category.gpuCores
      this.ram = this.category.ram,
      this.title = "Edit category"
      this.btnTitle = "Save changes"
    },
    resetData() {
      this.name = ''
      this.cpuCores = ''
      this.gpuCores = ''
      this.ram = ''
    },
    storeData() {
      const data = { 
                name: this.name, 
                gpuCores: this.gpuCores,
                cpuCores: this.cpuCores,
                ram: this.ram
                }
      let action = ADD_CATEGORY
       if (this.category) {
          action = EDIT_CATEGORY
          data.oldName = this.category.name
        }
      this.$store.dispatch(action, data)
      .then( res => {
          if (action === EDIT_CATEGORY)
            this.$router.push('/categories')
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