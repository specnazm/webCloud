<template>
<div id="organisationPage" class="container">
    <Form 
        :showModal="showModal" 
        @closeModal="closeModal" 
        :org="selectedOrg" />
		<div class="card">
			<div class="container-fliud">
				<div class="wrapper row">
					<div class="preview col-md-6">	
						<div class="preview-pic tab-content">
						  <div class="tab-pane active" id="pic-1"><img :src="logo_url" /></div>
						  <div class="tab-pane" id="pic-2"><img src="http://placekitten.com/400/252" /></div>
						  <div class="tab-pane" id="pic-3"><img src="http://placekitten.com/400/252" /></div>
						  <div class="tab-pane" id="pic-4"><img src="http://placekitten.com/400/252" /></div>
						  <div class="tab-pane" id="pic-5"><img src="http://placekitten.com/400/252" /></div>
						</div>
					</div>
					<div class="details col-md-6">
						<h3 class="product-title">{{name}}</h3>
						<div class="rating">
							<div class="stars">
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star checked"></span>
								<span class="fa fa-star"></span>
								<span class="fa fa-star"></span>
							</div>
							<span class="review-no">Number of users in organisation : {{userNum}}</span><br/>
                            <span class="review-no">Number of resources in organisation : {{rsrcNum}}</span>
						</div>
						<p class="product-description">{{desc}}</p>
						<h5 class="list-group list-group-flush"><b>Resouruces</b>
                            <ul>
                            <li 
                                 class="list-group-item list-group-item-secondary"
                                v-for="res in rsrc" :key="res.name">{{res.name}} </li>
                            </ul>
						</h5>
						<h5 class="list-group list-group-flush"><b>Users</b>
                            <ul  class="list-group-item list-group-item-secondary"  v-for="user in users" :key="user.email">
                            <li 
                                class="user" title="small"
                               >{{user.name}} {{user.surname}} </li>
                            </ul>
						</h5>
						<div class="action">
							<button class="add-to-cart btn btn-default" type="button" @click="showModal = true">Edit</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { GET_ORGANISATION, EDIT_ORGANISATION } from '../../actions'
import Form from '../forms/organisationForm'
import { SET_ORGANISATION } from '../../mutations'
import { mapState } from 'vuex'
import store from '../../store'

export default {
    name: "organisationPage",
     components: {
            Form
        },
    data : function() {
        return {
            routeName : '',
            name: '',
            desc : '',
            logo_url: '',
            users: [],
            rsrc: [],
            showModal : false
        }
    },
    computed: {
        userNum() {
            return Object.keys(this.users).length
        },
        rsrcNum() {
            return Object.keys(this.rsrc).length
        },
        ...mapState([
            'selectedOrg'
        ])
    },
    watch: {
        selectedOrg(){
            this.setData()  
        }
    },
    methods: {
        setData() {
            this.name = this.selectedOrg.name
            this.desc = this.selectedOrg.desc
            this.logo_url = this.selectedOrg.logo_url
            this.users = this.selectedOrg.users
            this.rsrc = this.selectedOrg.rsrc
        },
        closeModal() {
                this.showModal = false;
            }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        if (this.selectedOrg && this.selectedOrg.name === data.name) {
                this.setData()
        }
        else {
            this.$store.dispatch(GET_ORGANISATION, data)
            .then( res => this.setData(res.data))
            .catch(error => {
                this.$router.push('/dashboard');
                alert(error.response.data.msg)
            })
        }
    },
     beforeRouteLeave (to, from, next) {
         this.$store.commit(SET_ORGANISATION, null)
         next()
  }
}
</script>


<style scoped>
@import '../../css/organisationPage.css';
</style>