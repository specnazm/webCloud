<template>
 <div id="organisation" class="item col-xs-4 col-lg-4">
    <div class="thumbnail card">
        <div class="img-event">
            <img class="group list-group-image img-fluid" src="" alt="Organisation logo" />
        </div>
        <div class="caption card-body">
            <h4 class="group card-title inner list-group-item-heading"></h4>
            <p class="group inner list-group-item-text">
               
            </p>
            <div class="row">
                <div class="col-xs-12 col-md-6">
                    <a class="btn btn-success" href="">Edit organisation</a>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
    export default {
        name: "organisation",
        props: ['name'],
        mounted() {
           /* this.$store.dispatch(GET_ORGANISATION, name)
                .then( res => console.log(res))
                .catch(error => alert("Sorry, something went wrong!"))*/
            console.log(this.name)
        },
        computed() {
            this.organisation = this.$store.getter.organisation
        }
    }
</script>

<style scoped>
img {   
    width:  400px;
    height: 150px;
    background-size: cover;
}
</style>