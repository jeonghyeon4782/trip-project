<script setup>
import { onMounted, ref } from 'vue';
import { listReview, updateHits } from "@/api/review.js";


import ReviewListItem from '@/components/review/item/ReviewListItem.vue';
import SearchBarItem from '@/components/review/item/SearchBarItem.vue';
import PageNavigation from '@/components/common/PageNavigation.vue'

const reviews = ref([]);
const currentPage = ref(1);
const totalPage = ref(0);
const isLogin = ref({});
const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;
const param = ref({
    pageno: currentPage.value,
    pagesize: VITE_ARTICLE_LIST_SIZE,
    keyword: "",
    order: "",
    sidos: []
});

onMounted(() => {
    if (localStorage.getItem('isLogin') == "true") {
        isLogin.value = true;
    } else {
        isLogin.value = false;
    }
    getReviewList();
})

function updateParam(keyword, order, sidos) {
    param.value.keyword = keyword;
    param.value.order = order;
    param.value.sidos = [];
    sidos.forEach((sido) => {
        param.value.sidos.push(Number(sido));
    });

    console.log(param.value.sidos);
    getReviewList();
};

const onPageChange = (val) => {
    console.log(val + "번 페이지로 이동 요청");
    currentPage.value = val;
    param.value.pageno = val;
    getReviewList();
};


const getReviewList = () => {
    console.log("서버에 review 목록 요청", param.value);
    listReview(
        param.value,
        ({ data }) => {
            console.log(data.msg)
            console.log(data);
            console.log(data.data.reviewInfos);
            reviews.value = data.data.reviewInfos;
            currentPage.value = data.data.page;
            totalPage.value = data.data.total;
        },
        (error) => {
            console.log(error);
        }
    );
};

function incrementHits(reviewid) {
    updateHits(
        reviewid,
        (response) => {
            if (response.status == 200) {
                reviews.value.hits++;
            }
        },
        (error) => {
            console.log(error);
        }
    );
}

</script>

<template>
    <div>
        <SearchBarItem @search="updateParam" />
    </div>
    <hr>
    <div class="write-link">
        <RouterLink v-if="isLogin" :to="{ name: 'review-write' }" class="custom-link">글 작성</RouterLink>
    </div>
    <div class="review-grid">
        <ReviewListItem v-for="review in reviews" :key="review.reviewId" :review="review"
            @increment-hits="incrementHits">
        </ReviewListItem>
    </div>
    <div>
        <PageNavigation :current-page="currentPage" :total-page="totalPage" @pageChange="onPageChange" style="margin-top: 100px;"></PageNavigation>
    </div>

</template>

<style scoped>
hr {
    margin-top: 50px;
    margin-bottom: 50px;
    border: 1px solid #f1ebeb;
}
.review-grid {
    margin-top: 70px;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
}

.write-link {
    font-size: 25px;
    margin: 10px;
    text-align: right;
}


.custom-link {
  text-decoration: none;
  color: black;
}

.custom-link:hover {
  background-color: #E3EFFA;  /* 마우스를 올렸을 때 글자 색상을 파란색으로 변경 */
}
</style>