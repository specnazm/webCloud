<template>
<div id="organisationPage" class="container">
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
						<ul class="preview-thumbnail nav nav-tabs">
						  <li class="active"><a data-target="#pic-1" data-toggle="tab"><img src="http://placekitten.com/200/126" /></a></li>
						  <li><a data-target="#pic-2" data-toggle="tab"><img src="http://placekitten.com/200/126" /></a></li>
						  <li><a data-target="#pic-3" data-toggle="tab"><img src="http://placekitten.com/200/126" /></a></li>
						  <li><a data-target="#pic-4" data-toggle="tab"><img src="http://placekitten.com/200/126" /></a></li>
						  <li><a data-target="#pic-5" data-toggle="tab"><img src="http://placekitten.com/200/126" /></a></li>
						</ul>
						
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
							<span class="review-no">Number of users in organisation : {{users}}</span><br/>
                            <span class="review-no">Number of resources in organisation : {{rsrc}}</span>
						</div>
						<p class="product-description">{{desc}}</p>
						<h4 class="price">current price: <span>$180</span></h4>
						<p class="vote"><strong>91%</strong> of buyers enjoyed this product! <strong>(87 votes)</strong></p>
						<h5 class="sizes">Users:
                            <div 
                                class="size" data-toggle="tooltip" title="small"
                                v-for="user in users" :key="user.email">{{user.name}} {{user.surname}} </div>
						</h5>
						<div class="action">
							<button class="add-to-cart btn btn-default" type="button">add to cart</button>
							<button class="like btn btn-default" type="button"><span class="fa fa-heart"></span></button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import { GET_ORGANISATION } from '../../actions'

export default {
    name: "organisationPage",
    data : function() {
        return {
            routeName : '',
            name: '',
            desc : '',
            logo_url: '',
            users: [],
            rsrc: []
        }
    },
    computed: {
        org() {
            return this.$store.getters.organisation 
        }
    },
    methods: {
        setData() {
            this.name = this.org.name
            this.desc = this.org.desc
            this.logo_url = this.org.logo_url
            this.users = this.org.users
            this.rsrc = this.org.rsrc
        }
    },
    mounted() {
        this.routeName =  this.$route.params.name
        const data = { name: this.routeName }
        this.$store.dispatch(GET_ORGANISATION, data)
            .then( res => this.setData(res.data))
            .catch(error => alert("Sorry, something went wrong!"))
    }
}
</script>


<style scoped>
@import '../../css/organisationPage.css';
</style>