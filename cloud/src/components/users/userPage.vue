<template>
<div id="userPage" >
    <Form 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :user="selectedUser"
        />
    <div class="container emp-profile">
            <div class="row">
                <div class="col-md-4">
                    <div class="profile-img">
                        <img src="../../../img/userImg.jpg" alt="User profile photo"/>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="profile-head">
                        <h5>{{name}} {{surname}}</h5>
                        <h6>{{role}}</h6>
                        <p class="proile-rating">  <span></span></p>
                        <ul class="nav nav-tabs" id="myTab" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" id="home-tab" data-toggle="tab"  role="tab" aria-controls="home" aria-selected="true">About</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-md-2">
                    <button class="btn-user" @click="showModal = true">Edit Profile</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    <button  type="button" class="btn btn-outline-danger" @click="deleteUser">Delete user</button>
                </div>
                <div class="col-md-8">
                    <div class="tab-content profile-tab" id="myTabContent">
                        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                            <div class="row">
                                <div class="col-md-6">
                                    <label>User email</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{email}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Name</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{name}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Surname</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{surname}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Password</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{password}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Organisation</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{org}}</p>
                                </div>
                            </div>
                                <div class="row">
                                <div class="col-md-6">
                                    <label>Role</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{role}}</p>
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
import Form from '../forms/userForm'
import { GET_USER, DELETE_USER } from '../../actions'
import { SET_USER } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "userPage",
    components: {
        Form
    },
    data : function() {
        return {
            routeName : '',
            name: '',
            surname : '',
            email: '',
            password: '',
            role: '',
            org: '',
            showModal : false
        }
    },
    computed: mapState([    
        'selectedUser'
    ]),
    methods: {
        setData(user) {
            this.name = user.name
            this.surname = user.surname
            this.email = user.email
            this.role = user.role
            this.org = user.org
            this.password = user.password
        },
        closeModal() {
            this.showModal = false;
        },
        deleteUser() {
            this.$store.dispatch(DELETE_USER, this.selectedUser.email)
            .then( res => { this.closeModal()
                this.$router.push('/users')})
                    .catch(error => {
                        this.$router.push('/users');
                        alert(error.response.data.msg)
                    })
        }
    },
    mounted() {
        this.routeName =  this.$route.params.email
        const data = { email: this.routeName }
        if (this.selectedUser)
            if(this.selectedUser.email === data.email)
                this.setData(this.selectedUser)
        else {
             this.$store.dispatch(GET_USER, data)
                    .then( res => this.setData(res.data))
                    .catch(error => {
                        this.$router.push('/dashboard');
                        alert(error.response.data.msg)
                    })
        }
    },
    beforeRouteLeave (to, from, next) {
         this.$store.commit(SET_USER, null)
         next()
  }
}
</script>

<style scoped>
    @import '../../css/userPage.css';
</style>