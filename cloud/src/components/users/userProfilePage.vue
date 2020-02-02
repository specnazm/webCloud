<template>
<div id="userProfilePage" >
    <ProfileForm 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :user="loggedUser"
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
                        <h5>{{loggedUser.name}} {{loggedUser.surname}}</h5>
                        <h6>{{loggedUser.role}}</h6>
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
                </div>
                <div class="col-md-8">
                    <div class="tab-content profile-tab" id="myTabContent">
                        <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                            <div class="row">
                                <div class="col-md-6">
                                    <label>User email</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.email}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Name</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.name}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Surname</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.surname}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Password</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.password}}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label>Organisation</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.org}}</p>
                                </div>
                            </div>
                                <div class="row">
                                <div class="col-md-6">
                                    <label>Role</label>
                                </div>
                                <div class="col-md-6">
                                    <p>{{loggedUser.role}}</p>
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
import ProfileForm from '../forms/profileForm'
import { GET_USER, DELETE_USER } from '../../actions'
import { SET_USER } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "userProfilePage",
        data : function() {
        return {
            showModal : false
        }
    },
    components: {
        ProfileForm
    },
   computed: mapState({
    loggedUser: state => state.user
   }),
    methods: {
        closeModal() {
            this.showModal = false;
        },
    }
}
</script>

<style scoped>
    @import '../../css/page.css';
</style>