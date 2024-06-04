<!-- Test.vue -->
<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  sidoId: String
});
</script>

<template>
  <div>
    <h1>선택된 시도의 ID: {{ sidoId }}</h1>
  </div>
</template>

<style scoped>
    div {
        width: 1000px;
        height: 1000px;
        background-color: aliceblue;
    }
</style>
