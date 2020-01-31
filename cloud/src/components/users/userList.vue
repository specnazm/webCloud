<template>
<table id="users">
  <thead class="table table-dark">
    <tr>
      <th scope="col">#</th>
      <th scope="col">First</th>
      <th scope="col">Last</th>
      <th scope="col">Handle</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">1</th>
      <td>Mark</td>
      <td>Otto</td>
      <td>@mdo</td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr>
    <tr>
      <th scope="row">3</th>
      <td>Larry</td>
      <td>the Bird</td>
      <td>@twitter</td>
    </tr>
  </tbody>
</table>  
</template>

<script>


    export default {
        name: "organisationList",
        components: {
   
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
        }
    }
</script>