<template>
<div id="userInfo" class="container">
    <div class="row profile">
		<div class="col">
			<div class="profile-sidebar">
				<div class="profile-usertitle">
					<div class="profile-usertitle-name">
            {{user.name + ' ' + user.surname}}
					</div>
					<div class="profile-user-role">
						{{user.role}}
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</template>

<script>
import store from '../../store'

export default {
 name: "userInfo",
 computed : {
     user() {
         return store.state.user
     }
 }
}
</script>

<style scoped>
body {
  background: #F1F3FA;
}
.profile {
  margin: 20px 0;
}
.profile-sidebar {
  padding: 20px 0 10px 0;
  background: #fff;
}
.profile-usertitle {
  text-align: center;
  margin-top: 20px;
}
.profile-usertitle-name {
  text-transform: capitalize;
  color: #5a7391;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 7px;
}
.profile-user-role {
  text-transform: uppercase;
  color: #5b9bd1;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 15px;
}
</style>