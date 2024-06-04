<script setup>
import { defineProps, defineEmits, ref } from 'vue';
import defaultImage from '@/assets/defaultImage.jpg'
import { useRoute, useRouter } from "vue-router";
import { getReviewId } from "@/api/attractionInfo";

const props = defineProps({ boardLog: Object });

const imageAlt = "Post Image";

import checkReviewIcon from '@/assets/checkReview.svg';
import addReviewIcon from '@/assets/addReview.svg';

const route = useRoute();
const router = useRouter();

const reviewid = ref({});

const getReviewIdByAttractions = (boardLogId) => {
    console.log("관광지 정보 요청",boardLogId);
    getReviewId(
      boardLogId,
        ({ data }) => {
            console.log(data,data.data.reviewId);
            reviewid.value = data.data.reviewId;
            router.push({ name: 'review-view', params: { reviewid: reviewid.value } });
        },
        (error) => {
            console.log(error);
        }
    );
};

function checkReviewClick(boardLogId){
  getReviewIdByAttractions(boardLogId);

}

function addReviewClick(boardLogId){
    router.push({ name: 'write-board-review', params: { boardLogId: boardLogId } });
}

</script>

<template>
    <!-- <RouterLink :to="{ name: 'review-view', params: { reviewid: review.reviewId } }" @click="incrementHits"> -->
    <div class="item" >
      <img :src="boardLog.imageUrl ? boardLog.imageUrl : defaultImage" :alt="imageAlt">
      <div class="item-content">
        <div class="item-profile">
          <span>{{ boardLog.sidoName }}</span>
          <img v-if="boardLog.isWrite" :src="checkReviewIcon" class="check-review-icon" @click="checkReviewClick(boardLog.boardLogId)">
          <img v-else :src="addReviewIcon" class="add-review-icon" @click="addReviewClick(boardLog.boardLogId)">
        </div>
        <h3 class="item-title">{{ boardLog.name }}</h3>
        <div class="item-info">
          <span>Date: {{ boardLog.createDate }}</span>
        </div>
      </div>
    </div>
  <!-- </RouterLink> -->
</template>

<style scoped>

.item {
  background-color: #F1F1F6;
  border-radius: 10px;
  overflow: hidden;
  height: 450px;
  border: 1px solid #ccc;
  border-radius: 10px;
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

.check-review-icon, .add-review-icon {
  width: 30px;
  height: 30px;
  margin-left: 10px;
  cursor: pointer;
}

.item-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: gray;
}
</style>