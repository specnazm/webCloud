<template>
  <div id="organisationList" class="container">
    <Form :showModal="showModal" @closeModal="closeModal"/>
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button class="btn btn-info" id="list" @click="showModal = true">
                        Add new organisation
                    </button>
                </div>
            </div>
        </div>
    </div> 
  <div id="products" class="row view-group">
        <Organisation v-for="org in organisations" :organisation="org" :key="org.name"/>
</div>
 </div>
</template>

<script>
    import Organisation from './organisation'
    import Form from '../forms/organisationForm'
    import { GET_ORGANISATIONS } from '../../actions'
    import store from '../../store'

    export default {
        name: "organisationList",
        components: {
            Organisation,
            Form
        },
        data : function() {
            return {
            showModal : false
            }
        },
        computed: {
            organisations() {
                return this.$store.getters.orgs
            }
        },
        mounted() {
            this.$store.dispatch(GET_ORGANISATIONS)
                .then( res => console.log(res))
                .catch(error => alert("Sorry, something went wrong!"))
        },
        methods: {
            closeModal() {
                this.showModal = false;
            }
        },
        beforeRouteEnter(to,from,next) {
            const role = store.getters.role
            if ( role !== 'SUPER_ADMIN' && role !== 'ADMIN') {
                next('/dashboard')
            } else {
                next()
            }
        }  
    }
</script>