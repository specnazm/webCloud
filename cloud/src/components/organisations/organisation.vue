<template>
 <div id="organisation" class="item col-xs-4 col-lg-4">
    <div class="thumbnail card">
        <div class="img-event">
            <img class="group list-group-image img-fluid" :src="organisation.logo_url" alt="Organisation logo" />
        </div>
        <div class="caption card-body">
            <h4 class="group card-title inner list-group-item-heading">{{organisation.name}}</h4>
            <p class="group inner list-group-item-text">
                {{organisation.desc}} 
            </p>
            <div class="row">
                <div class="col-xs-12 col-md-6">
                    <a class="btn" :href="url" @click="setOrg">More info</a>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import { SET_ORGANISATION } from '../../mutations'
import store from '../../store'

    export default {
        name: "organisation",
        props: ['organisation'],
        computed: {
            url() {
                return`/organisation/${this.organisation.name}`
            }
        },
        methods: {
            setOrg() {
                store.commit(SET_ORGANISATION, this.organisation)
            }
        }
    }
</script>

<style scoped>
img {   
    width:  400px;
    height: 150px;
    background-size: cover;
}
.btn {
    background-color: #ff9f1a
}
.btn:hover {
    background: #b36800;
    color: #fff;
}

</style>