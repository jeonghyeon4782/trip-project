<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { deleteReview, detailReview, createLike, deleteLike } from "@/api/review";

import favorite from '@/assets/favorite.svg';
import favoriteFill from '@/assets/favorite_fill.svg';
import location from '@/assets/location.svg';
import defaultImage from '@/assets/defaultImage.jpg';

import CommentListItem from '@/components/review/item/CommentListItem.vue';
import Swal from "sweetalert2";


const route = useRoute();
const router = useRouter();

const { reviewid } = route.params;

const review = ref({});

const comments = ref({});
const isLogin = ref({});

onMounted(() => {
    if (localStorage.getItem('isLogin') == "true") {
        isLogin.value = true;
    } else {
        isLogin.value = false;
    }
    getReview();
})

const getReview = () => {
    console.log(reviewid + "번 리뷰 요청");
    detailReview(
        reviewid,
        ({ data }) => {
            console.log(data);
            review.value = data.data;
            console.log(review);
        },
        (error) => {
            console.log(error);
        }
    );
};

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: '확인'
  });
};

function onDeleteReview() {
    Swal.fire({
        title: '정말 삭제하시겠습니까?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            console.log(reviewid + "번 리뷰 삭제 요청");
            deleteReview(
                reviewid,
                (response) => {
                    if (response.status == 204) {
                        router.replace({ name: "review-list" });
                    }
                },
                (error) => {
                    console.log(error);
                    Swal.fire({
                        title: '삭제 실패',
                        text: '리뷰 삭제에 실패했습니다.',
                        icon: 'error',
                        confirmButtonText: '확인'
                    });
                }
            );
        }
    });
}

const favoriteIcon = computed(() => {
    console.log(review.value.isCheckLike);
    return review.value.isCheckLike ? favoriteFill : favorite;
})

function favoriteClick() {

    if (review.value.isCheckLike) {
        subLikes()
    } else {
        addLikes()
    }

    console.log(review.value.isCheckLike, review.value.likes)
    review.value.isCheckLike = !review.value.isCheckLike;
}

function addLikes() {
    createLike(
        reviewid,
        (response) => {
            console.log(response)
            if (response.status == 201) {
                review.value.likes++;
            }
        },
        (error) => {
            console.log(error);
        }
    );
}

function subLikes() {
    deleteLike(
        reviewid,
        (response) => {
            if (response.status == 204) {
                review.value.likes--;
            }
        },
        (error) => {
            console.log(error);
        }
    );

}

function updateComments(newCount) {
    comments.value = newCount;
}
</script>

<template>
    <div class="container">
        <div class="link">
            <RouterLink :to="{ name: 'review-list' }"  style="font-size: 20px; cursor: pointer">뒤로가기</RouterLink>
            <span class="user-link">
                <RouterLink :to="{ name: 'review-modify' }" v-if="review.isWriteByMe">수정</RouterLink>
                <a @click="onDeleteReview" v-if="review.isWriteByMe">삭제</a>
            </span>
        </div>
        <hr style="margin-top: 70px;">
        <!-- 게시판 -->
        <div class="review">
            <div class="review-content">
                <h1 class="review-title">{{ review.title }}</h1>
                <div class="top-info">
                    <div class="review-profile">
                        <img :src="review.profileImageUrl ? review.profileImageUrl : defaultImage"
                            class="profile-image">
                        <span class="username">{{ review.nickname }}</span>
                        <a>
                            <img :src="location" class="location-icon">
                        </a>
                        <span class="location-text">{{ review.attractionName }}</span>
                        <span class="review-date">{{ review.createDate }}</span>
                    </div>
                    <img v-if="!review.isWriteByMe && isLogin" :src="favoriteIcon" class="favorite-icon"
                        @click="favoriteClick">
                </div>
                <img :src="review.reviewImageUrl" v-if="review.reviewImageUrl" class="review-image">
                <hr>
                <p class="review-description">{{ review.content }}</p>
                <hr>
                <div class="review-info">
                    <span>좋아요: {{ review.likes }}</span>
                    <span>댓글: {{ comments }}</span>
                    <span>조회수: {{ review.hits }}</span>
                </div>
                <hr>
            </div>
        </div>
        <CommentListItem @update-comment-count="updateComments" :reviewId="reviewid" />
    </div>
</template>

<style scoped>
.container {
    margin: 20px auto;
  margin-top: 50px;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px #f1f1f6;
  text-align: center; /* 내부 요소들을 가로 방향으로 중앙 정렬 */
}

hr {
    border: 1px solid #ccc;
}

.link {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    text-align: right;
    padding: 0 10px;
}

.user-link>a {
    margin-right: 30px;
    cursor: pointer;
    font-size: 20px;
}

.review {
    overflow: hidden;
    margin-bottom: 20px;
}

.review-content {
    background-color: white;
    text-align: center;
    padding: 20px;
}

.review-title {
    margin: 20px;
}

.review-profile {
    display: flex;
    /* text-align: center; */
    align-items: center;
    margin-left: 10px;
}


.profile-image {
    width: 50px;
    height: 50px;
    border: 3px solid #f1ebeb;
    border-radius: 50%;
    margin-right: 20px;
}

.username {
    margin-right: 30px;
    font-size: 17px
}

.top-info {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 10px;
    margin: 0 10%;
}

.review-date {
    margin-left: 16px;
}

.location-icon {
    width: 50px;
    height: 50px;
    margin-right: 5px;
}

.location-text {
    font-size: 17px;
}

.favorite-icon {
    margin-right: 20px;
    margin-bottom: 20px;
    cursor: pointer;
}

.review-title {
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 10px;
}

.review-description {
    font-size: 20px;
    margin: 20px 0;
    height: 500px;
    overflow-y: auto;
    text-align: center;
}

.review-image {
    width: 50%;
    height: auto;
    margin-top: 70px;
    margin-bottom: 20px;
    border-radius: 5px;
}

.review-info {
    display: flex;
    justify-content: space-between;
    font-size: 0.9rem;
    color: gray;
    margin: 0 10%;
}

a:hover {
  background-color: #e3effa;
}
</style>