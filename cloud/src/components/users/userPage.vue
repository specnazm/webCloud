<template>
<div id="userPage" >
    <Form 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :user="user"
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
    computed: {
        user() {
            return this.$store.getters.selectedUser 
        }
    },
    methods: {
        setData() {
            this.name = this.user.name
            this.surname = this.user.surname
            this.email = this.user.email
            this.role = this.user.role
            this.org = this.user.org
            this.password = this.user.password
        },
        closeModal() {
            this.showModal = false;
        },
        deleteUser() {
            this.$store.dispatch(DELETE_USER, this.user.email)
            .then( res => this.closeModal())
                    .catch(error => {
                        this.$router.push('/users');
                        alert(error.response.data.msg)
                    })
        }
    },
    mounted() {
        this.routeName =  this.$route.params.email
        const data = { email: this.routeName }
        if (this.user)
            if(this.user.email === data.email)
                this.setData(this.user)
        else {
             this.$store.dispatch(GET_USER, data)
                    .then( res => this.setData(res.data))
                    .catch(error => {
                        this.$router.push('/dashboard');
                        alert(error.response.data.msg)
                    })
        }
            

    }
}
</script>

<style scoped>
    @import '../../css/userPage.css';
</style>