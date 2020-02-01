<template>
<div id="categories" class="container">
    <Form :showModal="showModal" @closeModal="closeModal"/>
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button class="btn btn-info" id="list" @click="showModal = true">
                        Add new category
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
      <th scope="col">Ram</th>
    </tr>
  </thead>
  <tbody class="category" v-for="(cat, ind) in categories" :key="cat.name" @click="categoryInfo(cat)">
    <tr>
      <th scope="row">{{index(ind)}}</th>
      <td>{{cat.name}}</td>
      <td>{{cat.cpuCores}}</td>
      <td>{{cat.gpuCores}}</td>
      <td>{{cat.ram}}</td>
    </tr>
  </tbody>
</table>
</div>
</template>

<script>
import Form from '../forms/categoryForm'
import { GET_CATEGORIES } from '../../actions'
import store from '../../store'
import { SET_CATEGORY } from '../../mutations'

  export default {
      name: "categories",
      components: {
        Form
      },
      data : function() {
          return {
          showModal : false
          }
      },
      computed: {
          categories() {
            return this.$store.getters.categories
          }
      },
      mounted() {
          this.$store.dispatch(GET_CATEGORIES)
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
          categoryInfo(cat) {
            store.commit(SET_CATEGORY, cat)
            this.$router.push(`/user/${cat.name}`)
          }
      },
       beforeRouteEnter(to,from,next) {
            const role = store.getters.role
            if ( role !== 'SUPER_ADMIN') {
                next('/dashboard')
            } else {
                next()
            }
        }  
  }
</script>

<style scoped>
.category:hover {
  cursor: pointer;
}
</style>