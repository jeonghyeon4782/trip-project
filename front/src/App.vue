<script setup>
import { RouterView } from 'vue-router'
import TheHeadingNavbar from '@/components/layout/TheHeadingNavbar.vue';
import TheFooter from '@/components/layout/TheFooter.vue';

</script>

<template>
  <TheHeadingNavbar />
  <div class="main">
    <RouterView class="main-view"/>
  </div>
  <TheFooter/>
</template>

<style scoped>
.main{
  height: auto;
  width: 100vw;
  margin-top: 10%;
  margin-bottom: 10%;
  grid-template-columns: 1fr 1fr;
}

.main-view{
    width: 80%;
    margin: auto;
}
</style>