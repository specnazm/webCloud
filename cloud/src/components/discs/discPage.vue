<template>
<div id="discPage" >
    <Form 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :disc="selectedDisc"
        />
    <div class="container emp-profile">
            <div class="row">
                <div class="col-md-4">
                    <div class="profile-img">
                        <img :src="getIcon()" alt="Disc photo"/>
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
                      @click="showModal = true">Edit Disc</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <button  
                        v-if="role === 'SUPER_ADMIN'"
                        type="button" 
                        class="btn btn-outline-danger" 
                        @click="deleteDisc">Delete disc</button>
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
                                    <label>Capacity:</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{capacity}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Virtual machine :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{vm}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Organisation :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{org}}</p>
                                </div>
                            </div>
                             <div class="row">
                                <div class="col-md-6">
                                    <label>Type :</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{type}}</p>
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
import Form from '../forms/discForm'
import { GET_DISC , DELETE_DISC } from '../../actions'
import { SET_DISC } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "discPage",
    components: {
        Form
    },
    data : function() {
        return {
            routeName : '',
            name: '',
            capacity : '',
            vm: '',
            org: '',
            type: '',
            showModal : false,
            role: store.getters.role
        }
    },
    computed: mapState([    
        'selectedDisc'
    ]),
    methods: {
        setData(disc) {
            this.name = disc.name
            this.capacity = disc.capacity
            this.vm = disc.vm.name
            this.type = disc.type
            this.org = disc.org.name
        },
        closeModal() {
            this.showModal = false;
        },
        deletedisc() {
            this.$store.dispatch(DELETE_DISC, this.selectedDisc.name)
            .then( res => { this.closeModal() 
                    this.$router.push('/discs') })
            .discch(error => alert(error.response.data.msg))
        },
        getIcon() {
           return `../../../img/${this.type}.jpg`
        }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        if (this.selectedDisc && this.selectedDisc.name === data.name) {
                this.setData(this.selectedDisc)
        }
        else {
             this.$store.dispatch(GET_DISC, data)
                    .then( res => this.setData(res.data))
                    .discch(error => {
                        this.$router.push('/dashboard')
                        alert(error.response.data.msg)
                    })
        }
    },
    beforeRouteLeave (to, from, next) {
         this.$store.commit(SET_DISC, null)
         next()
  }
}
</script>

<style scoped>
    @import '../../css/page.css';
</style>