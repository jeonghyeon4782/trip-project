<script setup>
import horseImage from "@/assets/horse.gif";
import 서두나 from "@/assets/서두나.png"
import 박찬호 from "@/assets/박찬호.png"
import 김태균 from "@/assets/김태균.png"
import { ref, watch, defineProps, onMounted } from "vue";

const props = defineProps({
  now: Number,
  imageId: Number
});
const rotateXVal = ref(0);
const rotateYVal = ref(0);
const rotateZVal = ref(0);
const faceMarginLeft = ref(86);
const faceMarginBottom = ref(10);
const faceMarginTop = ref(0);
const imageSrc = ref("");

watch(() => props.now, (newValue) => {
  if (0 <= newValue && newValue < 7) {
    rotateXVal.value = 0;
    rotateYVal.value = 180;
    rotateZVal.value = 0;
    faceMarginLeft.value = 24;
    faceMarginBottom.value = 3;
    faceMarginTop.value = 0;
  } else if (7 <= newValue && newValue < 14) {
    rotateXVal.value = 0;
    rotateYVal.value = 180;
    rotateZVal.value = -90;
    faceMarginLeft.value = 92;
    faceMarginBottom.value = 100;
    faceMarginTop.value = 10;
  } else if (14 <= newValue && newValue < 21) {
    rotateXVal.value = 0;
    rotateYVal.value = 0;
    rotateZVal.value = 0;
    faceMarginLeft.value = 87;
    faceMarginBottom.value = 98;
    faceMarginTop.value = 0;
  } else {
    rotateXVal.value = 0;
    rotateYVal.value = 180;
    rotateZVal.value = 90;
    faceMarginLeft.value = 15;
    faceMarginBottom.value = 0;
    faceMarginTop.value = 80;
  }
});

onMounted(() => {
  if (props.imageId == 1) {
    imageSrc.value = 서두나;
  } else if (props.imageId == 2) {
    imageSrc.value = 박찬호;
  } else if (props.imageId == 3) {
    imageSrc.value = 김태균;
  }
});
</script>

<template>
  <div class="horse-container">
    <img
      class="horse"
      :src="horseImage"
      :style="{
        transform: `rotateX(${rotateXVal}deg) rotateY(${rotateYVal}deg) rotateZ(${rotateZVal}deg)`,
      }"
      alt="Horse Image"
    />
    <img
      class="face"
      :src="imageSrc"
      :style="{
        transform: `rotateX(${rotateXVal}deg) rotateY(${rotateYVal}deg) rotateZ(${rotateZVal}deg)`,
        marginLeft: `${faceMarginLeft}px`,
        marginBottom: `${faceMarginBottom}px`,
        marginTop: `${faceMarginTop}px`,
      }"
      alt="Face Image"
    />
  </div>
</template>

<style scoped>
.horse-container {
  width: 180px;
  height: 160px;
  position: relative;
}

img {
  width: 180px;
  height: 150px;
  transition: transform 0.5s ease;
  position: absolute;
}

.horse {
  margin-top: 5px;
  z-index: 1; 
}

.face {
  width: 75px;
  height: 75px;
  z-index: 2; 
  transition: transform 0.5s ease;
}
</style>
