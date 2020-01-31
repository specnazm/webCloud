<template>
<div id="organisationPage" class="container">
    <Form :showModal="showModal" @closeModal="closeModal" :org="org"/>
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
						<h5 class="sizes">Resources:
                            <ul>
                            <li 
                                class="size" data-toggle="tooltip" title="small"
                                v-for="res in rsrc" :key="res.name">{{res.name}} </li>
                            </ul>
						</h5>
						<h5 class="sizes">Users:
                            <ul>
                            <li 
                                class="size" data-toggle="tooltip" title="small"
                                v-for="user in users" :key="user.email">{{user.name}} {{user.surname}} </li>
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
import { GET_ORGANISATION } from '../../actions'
import Form from '../forms/organisationForm'

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
        org() {
            return this.$store.getters.organisation 
        },
        userNum() {
            return Object.keys(this.users).length
        },
        rsrcNum() {
            return Object.keys(this.rsrc).length
        }
    },
    methods: {
        setData() {
            this.name = this.org.name
            this.desc = this.org.desc
            this.logo_url = this.org.logo_url
            this.users = this.org.users
            this.rsrc = this.org.rsrc
        },
        closeModal() {
                this.showModal = false;
            }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        if (!this.$store.getters.organisation)
        this.$store.dispatch(GET_ORGANISATION, data)
            .then( res => this.setData(res.data))
            .catch(error => {
                this.$router.push('/dashboard');
                alert(error)
            })
    }
}
</script>


<style scoped>
@import '../../css/organisationPage.css';
</style>