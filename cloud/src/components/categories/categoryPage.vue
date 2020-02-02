<template>
<div id="categoryPage" >
    <Form 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :category="selectedCat"
        />
    <div class="container emp-profile">
            <div class="row">
                <div class="col-md-4">
                    <div class="profile-img">
                        <img src="../../../img/cat.png" alt="Category photo"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="profile-head">
                        <h5>{{name}}</h5>
                        <p class="proile-rating">  <span></span></p>
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" id="home-tab" data-toggle="tab"  role="tab" aria-controls="home" aria-selected="true">About</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn-user" @click="showModal = true">Edit Category</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <button  type="button" class="btn btn-outline-danger" @click="deleteCat">Delete category</button>
                </div>
                <div class="col-md-8">
                    <div class="tab-content profile-tab" id="myTabContent">
                        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Name :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{name}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Number of CPU cores :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{cpuCores}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Number of GPU cores :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{gpuCores}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Ram memory size in GB :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{ram}}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>         
    </div>
    </div>
</template>

<script>
import Form from '../forms/categoryForm'
import { GET_CATEGORY , DELETE_CATEGORY } from '../../actions'
import { SET_CATEGORY } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "categoryPage",
    components: {
        Form
    },
    data : function() {
        return {
            routeName : '',
            name: '',
            cpuCores : '',
            gpuCores: '',
            ram: '',
            showModal : false
        }
    },
    computed: mapState([    
        'selectedCat'
    ]),
    methods: {
        setData(cat) {
            this.name = cat.name
            this.cpuCores = cat.cpuCores
            this.gpuCores = cat.gpuCores
            this.ram = cat.ram
        },
        closeModal() {
            this.showModal = false;
        },
        deleteCat() {
            this.$store.dispatch(DELETE_CATEGORY, this.selectedCat.name)
            .then( res => { this.closeModal() 
                    this.$router.push('/categories') })
            .catch(error => {
                alert(error.response.data.msg)
                this.$router.push('/categories')})
        }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        if (this.selectedCat && this.selectedCat.name === data.name) {
                this.setData(this.selectedCat)
        }
        else {
             this.$store.dispatch(GET_CATEGORY, data)
                    .then( res => this.setData(res.data))
                    .catch(error => {
                        this.$router.push('/dashboard')
                        alert(error.response.data.msg)
                    })
        }
    },
    beforeRouteLeave (to, from, next) {
         this.$store.commit(SET_CATEGORY, null)
         next()
  }
}
</script>

<style scoped>
    @import '../../css/page.css';
</style>