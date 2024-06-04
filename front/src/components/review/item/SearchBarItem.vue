<script setup>
import { ref, watch, onMounted, defineEmits } from 'vue';

const emit = defineEmits(['search'])

import { getSidoList } from "@/api/board.js";

const sidos = ref([]);

onMounted(() => {
  getSidos();
})

const showFilterModal = ref(false);
const keyword = ref('');
const sortOrder = ref('latest');
const selectedRegions = ref([]);
const allCheckbox = ref(false);
let regionId = ref([]);

function filterModal() {
  showFilterModal.value = !showFilterModal.value;
};

function search() {
  setRegionId();
  console.log('검색어:', keyword.value);
  console.log('정렬 순:', sortOrder.value);
  console.log('선택 지역:', selectedRegions.value);

  emit('search', keyword.value, sortOrder.value, regionId.value);
};

const setSortOrder = (order) => {
  sortOrder.value = order;
  setRegionId()
  emit('search', keyword.value, sortOrder.value, regionId.value);
};

function setRegionId() {
  if (selectedRegions.value.length === sidos.value.length) {
    regionId.value = [];
  } else {
    regionId.value = selectedRegions.value;
  }
}

const toggleAllRegions = () => {
  if (selectedRegions.value.length === sidos.value.length) {
    selectedRegions.value = [];
  } else {
    selectedRegions.value = sidos.value.map(sido => sido.sidoId);
  }
};

const getSidos = () => {
  console.log("sido 목록 요청");
  getSidoList(
    ({ data }) => {
      console.log(data.msg)
      console.log(data);
      sidos.value = data.data;
    },
    (error) => {
      console.log(error);
    }
  );
};

watch(selectedRegions, () => {
  if (selectedRegions.value.length === sidos.value.length) {
    allCheckbox.value = true;
  } else {
    allCheckbox.value = false;
  }
});

</script>

<template>
  <!-- 검색 바 -->
  <div class="search-bar">
    <!-- 필터 버튼 -->
    <button class="btn" @click="filterModal">{{ showFilterModal ? '닫기' : '지역 선택' }}</button>
    <!-- 검색 입력 필드 -->
    <input type="text" placeholder="검색어를 입력하세요." v-model="keyword" style="margin-left: 50px;">
    <!-- 검색 버튼 -->
    <button class="btn" @click="search">검색</button>
  </div>

  <!-- 정렬 버튼 -->
  <div class="sort-buttons">
    <button class="btn" :class="{ active: sortOrder === 'latest' }" @click="setSortOrder('latest')">최신순</button>
    <button class="btn" :class="{ active: sortOrder === 'like' }" @click="setSortOrder('like')">좋아요 순</button>
  </div>

  <!-- 필터 모달 -->
  <div v-if="showFilterModal" class="filter-modal">
    <div class="filter-group">
      <input type="checkbox" id="all" value="전체" v-model="allCheckbox" @change="toggleAllRegions">
      <label for="all">전체</label><br>
      <div v-for="sido in sidos" :key="sido.sidoId" class="checkbox-item">
        <input type="checkbox" :id="sido.sidoId" :value="sido.sidoId" v-model="selectedRegions">
        <label :for="sido.sidoId">{{ sido.name }}</label><br>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  height: 40px;
  width: 70%;
  margin: 20px auto;
}

.search-bar input[type="text"] {
  flex: 1;
  margin-right: 10px;
  height: 45px;
  font-size: 20px;
  padding-left: 10px;
}

.search-bar button {
  margin-top: 10px;
  display: block;
  width: 10%; 
  padding: 10px;
  border: 1px solid #f1ebeb;
  border-radius: 10px;
  background-color: white;
  color: black;
  text-align: center;
  cursor: pointer;
  margin: 0 auto; /* 중앙 정렬 */
  font-family: "Gaegu", cursive;
  font-size: 15px;
  font-weight: bold;
}

.sort-buttons {
  display: flex;
  justify-content: center;
  margin: 10px auto;
}

.sort-buttons .btn {
  margin-top: 10px;
  margin-right: 20px;
  display: block;
  width: 10%; 
  padding: 10px;
  border: 1px solid #f1ebeb;
  border-radius: 10px;
  background-color: white;
  color: black;
  text-align: center;
  cursor: pointer;
  font-family: "Gaegu", cursive;
  font-size: 15px;
  font-weight: bold;
}

.sort-buttons .btn.active {
  background-color: #E3EFFA;
}

.btn:hover {
  background-color: #E3EFFA;
}

.filter-modal {
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  margin-top: 40px;
}

.filter-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-group label {
  margin-right: 10px;
}

.filter-group input[type="checkbox"],
.filter-group input[type="radio"] {
  margin-right: 5px;
}

input {
  border: 1px solid #f1ebeb;
  border-radius: 10px;
}
</style>