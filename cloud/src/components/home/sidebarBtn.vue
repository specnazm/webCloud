<template>
    <div id="burger"
         :class="{ 'active' : isOpen }"
         @click.prevent="toggleActive">
        <slot>
            <button type="button" class="burger-button" title="Menu">
                <span class="burger-bar burger-bar--1"></span>
                <span class="burger-bar burger-bar--2"></span>
                <span class="burger-bar burger-bar--3"></span>
            </button>
        </slot>
    </div>
</template>
<script>
export default {
    name: "burger",
    props: {
        isOpen : {
            type : Boolean
        }
    },
    methods: {
        toggleActive() {
            this.$emit('toggle')
        }
    }
}
</script>
<style>
  @import '../../css/sidebarBtn.css';
</style>