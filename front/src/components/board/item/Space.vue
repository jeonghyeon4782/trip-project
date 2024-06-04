<script setup>
import { defineProps } from "vue";
const props = defineProps({
  attractionName: String,
});
</script>

<template>
  <div>
    <p>{{ attractionName }}</p>
  </div>
</template>

<style scoped>

p {
  font-size: 17px;
  font-weight: bold; 
  margin-top: 55px;
}
</style>
