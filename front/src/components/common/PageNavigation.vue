<script setup>
import { computed } from "vue";

const props = defineProps({ currentPage: Number, totalPage: Number });
const emit = defineEmits(["pageChange"]);

const navigationSize = parseInt(import.meta.env.VITE_ARTICLE_NAVIGATION_SIZE);

const startPage = computed(() => {
    return parseInt((props.currentPage - 1) / navigationSize) * navigationSize + 1;
});

const endPage = computed(() => {
    let lastPage =
        parseInt((props.currentPage - 1) / navigationSize) * navigationSize + navigationSize;
    return props.totalPage < lastPage ? props.totalPage : lastPage;
});

const endRange = computed(() => {
    return parseInt((props.totalPage - 1) / navigationSize) * navigationSize < props.currentPage;
});

function range(start, end) {
    const list = [];
    for (let i = start; i <= end; i++) list.push(i);
    return list;
}

function onPageChange(pg) {
    console.log(pg + "로 이동!!!");
    emit("pageChange", pg);
}
</script>

<template>
    <ul class="pagination">
        <li class="page-item">
            <a class="page-link" @click="onPageChange(1)">최신</a>
        </li>
        <li class="page-item">
            <a class="page-link" @click="onPageChange(startPage == 1 ? 1 : startPage - 1)">이전</a>
        </li>
        <template v-for="pg in range(startPage, endPage)" :key="pg">
            <li :class="currentPage === pg ? 'page-item active' : 'page-item'">
                <a class="page-link" @click="onPageChange(pg)">{{ pg }}</a>
            </li>
        </template>
        <li class="page-item">
            <a class="page-link" @click="onPageChange(endRange ? totalPage : endPage + 1)">다음</a>
        </li>
        <li class="page-item"><a class="page-link" @click="onPageChange(totalPage)">마지막</a></li>
    </ul>
</template>

<style scoped>
.pagination {
    margin-top: 50px;
    list-style: none;
    display: flex;
    justify-content: center;
}

.pagination .page-item {
    margin: 0 5px;
}

.pagination .page-link {
    cursor: pointer;
    border: 1px solid #ddd;
    padding: 5px 10px;
    text-decoration: none;
    color: black;
    border-radius: 10px;
}

.pagination .page-link:hover {
    background-color: #E3EFFA;
}

.pagination .page-item.active .page-link {
    background-color: #E1CCEC;
    border-color: #E1CCEC;
    border-radius: 10px;
}
</style>
