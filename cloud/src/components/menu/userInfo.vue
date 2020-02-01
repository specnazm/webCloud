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
          <div class="profile-user-role">
						{{user.org}}
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
  @import '../../css/userInfo.css';
</style>