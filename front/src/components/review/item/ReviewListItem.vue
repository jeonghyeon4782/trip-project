<script setup>
import { defineProps, defineEmits } from 'vue';
import defaultImage from '@/assets/defaultImage.jpg'

const props = defineProps({ review: Object });

const emit = defineEmits(['increment-hits'])


function incrementHits() {
  console.log("reviewId", props.review.reviewId)
  emit('increment-hits', props.review.reviewId);
}
</script>

<template>
  <RouterLink :to="{ name: 'review-view', params: { reviewid: review.reviewId } }" @click="incrementHits">
    <div class="item">
      <img :src="review.reviewImageUrl ? review.reviewImageUrl : defaultImage" :alt="imageAlt">
      <div class="item-content">
        <div class="item-profile">
          <img :src="review.profileImageUrl ? review.profileImageUrl : defaultImage" class="profile-image">
          <span>{{ review.nickname }}</span>
        </div>
        <h3 class="item-title">{{ review.title }}</h3>
        <div class="item-info">
          <span>Likes: {{ review.likes }}</span>
          <span>Views: {{ review.hits }}</span>
        </div>
      </div>
    </div>
  </RouterLink>
</template>

<style scoped>
.item {
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  height: 450px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.item img {
  width: 100%;
  height: 70%;
  object-fit: cover;
}

.item-content {
  padding: 20px;
}

.item-profile {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.item-profile img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.item-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.item-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: gray;
}
</style>