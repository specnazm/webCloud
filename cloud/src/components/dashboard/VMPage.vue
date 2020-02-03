<template>
<div id="vmPage" >
    <Form 
        v-if="role !== 'USER'"
        :showModal="showModal" 
        @closeModal="closeModal" 
        :vm="selectedVM"
        />
    <div class="container emp-profile">
            <div class="row">
                <div class="col-md-4">
                    <div class="profile-img">
                        <img src="../../../img/VM.png" alt="VM photo"/>
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
                    <button
                      v-if="role !== 'USER'"
                      class="btn-user" 
                      @click="showModal = true">Edit VM</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <button  
                        v-if="role !== 'USER'"
                        type="button" 
                        class="btn btn-outline-danger" 
                        @click="deleteVM" >Delete VM</button>
                    <button  
                        v-if="role !== 'USER'"
                        type="button" 
                        class="btn btn-outline-success"
                        @click="toggleVM" >{{toggleAction}}</button>
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
                                    <label>Organisation:</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{org}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Category :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{category}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>CPU cores :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{cpuCores}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>GPU cores :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{gpuCores}}</p>
                                </div>
                            </div>
                             <div class="row">
                                <div class="col-md-6">
                                    <label>Ram memory :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{ram}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Active :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{active}}</p>
                                </div>
                            </div>
                             <div class="row">
                                <div class="col-md-6">
                                    <label>Discs :</label>
                                </div>
                                <div class="col-md-6">
                                    <ul>
                                    <div  v-for="d in discs" :key="d.name">
                                        <li><p>{{d.name}},</p></li>
                                    </div>
                                    </ul> 
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-2">
                                    <label>Logs :</label>
                                </div>
                                <div class="col-sm-5">
                                    <ol>
                                    <div v-for="(l,ind) in log" :key="ind" v-if="ind % 2 == 0">
                                        <li><p>On: {{ l }}</p></li>
                                        <hr>
                                    </div>
                                    </ol>  
                                </div>
                                 <div class="col-sm-5">
                                    <ol>
                                    <div v-for="(l,ind) in log" :key="ind" v-if="ind % 2 != 0">
                                        <li><p>Off: {{ l }}</p></li>
                                        <hr>
                                    </div>
                                    </ol>  
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
import Form from '../forms/vmFormEdit'
import { GET_VM , DELETE_VM , TOGGLE_VM} from '../../actions'
import { SET_VM } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "vmPage",
    components: {
        Form
    },
    data : function() {
        return {
            routeName : '',
            org : '',
            discs : [],
            log : [],
            showModal : false,
            role: store.getters.role
        }
    },
    computed: {
        toggleAction() {
            return this.active ? 'Deactivate' : 'Activate'
        },
        category() {
            return this.selectedVM.category
        },
         cpuCores() {
            return this.selectedVM.cpuCores
        },
        gpuCores() {
            return this.selectedVM.gpuCores
        },
        ram() {
            return this.selectedVM.ram
        },
        active() {
            return this.selectedVM.active
        },
        name() {
            return this.selectedVM.name
        },
        ...mapState([    
        'selectedVM'
    ])},

    methods: {
        setData(vm) {
            this.name = vm.name
            this.org = vm.org
            this.cpuCores = vm.cpuCores
            this.gpuCores = vm.gpuCores
            this.ram = vm.ram
            this.category = vm.category
            this.discs = vm.discs
            this.log = vm.log
            this.active = vm.active
        },
        closeModal() {
            this.showModal = false;
        },
        deleteVM() {
            this.$store.dispatch(DELETE_VM, this.selectedVM.name)
            .then( res => { this.closeModal() 
                    this.$router.push('/dashboard') })
            .catch(error => alert(error.response.data.msg))
        },
        toggleVM() {
    
            this.$store.dispatch(TOGGLE_VM, { name: this.name})
            .then( res => this.setData(res.data))
            .catch(error => alert(error.response.data.msg))
        }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        if (this.selectedVM && this.selectedVM.name === data.name) {
                this.setData(this.selectedVM)
        }
        else {
             this.$store.dispatch(GET_VM, data)
                    .then( res => this.setData(res.data))
                    .catch(error => {
                        this.$router.push('/')
                        alert(error.response.data.msg)
                    })
        }
    },
    beforeRouteLeave (to, from, next) {
         this.$store.commit(SET_VM, null)
         next()
  }
}
</script>

<style scoped>
    @import '../../css/page.css';
</style>