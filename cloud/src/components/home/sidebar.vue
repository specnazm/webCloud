<template>
    <div class="sidebar">
        <div class="sidebar-backdrop" v-if="isOpen"></div>
        <transition name="slide">
            <div v-if="isOpen"
                 class="sidebar-panel bg-dark">
                <slot></slot>
            </div>
        </transition>
    </div>
</template>
<script>
    export default {
        name: "sidebar",
        props: {
            isOpen : {
                type : Boolean
            }
        }
    }
</script>
<style>
    @import '../../css/sidebar.css';
</style>