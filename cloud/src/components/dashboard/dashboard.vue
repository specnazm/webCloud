<template>
   <div id="dashboard" class="container">
    <div class="row">
        <div class="col-lg-12 my-3">
            <div class="pull-right">
                <div class="btn-group">
                    <button class="btn btn-info" id="list">
                        Add new virtual machine
                    </button>
                </div>
            </div>
        </div>
    </div> 
 </div>
</template>

<script>

//import { GET_ORGANISATIONS } from '../../actions'

export default {
  name: 'dashboard',
  components: {
    
  },
  data : function() {
    return {
      organisations: []
    }
  },
  mounted() {
  //   this.$store.dispatch(GET_ORGANISATIONS)
  //       .then( res => this.organisations = res.data)
  //       .catch(error => alert("Sorry, something went wrong!"))
   }
}
</script>

<style>
html {
   height: 100%;
   overflow:auto;
 }
 body {
   border: 0; margin: 0; padding: 0;
   font-family: 'Lato';
   height: 100
 }
</style>