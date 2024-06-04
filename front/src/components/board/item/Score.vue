<script setup>
import axios from "axios";
import { ref, onMounted, defineProps } from "vue";

const scoreList = ref([]);
const myId = ref(null);
const myScore = ref(null);
const changedRows = ref([]);
const props = defineProps({
  sidoId: {
    type: Number,
    required: true,
  },
});

const getScore = async (sidoId) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/score/${sidoId}`
    );
    const prevRanking = scoreList.value.slice(1);
    scoreList.value = response.data.data;
    myId.value = response.data.data[0].memberId;
    myScore.value = response.data.data[0].score;


    const newRanking = response.data.data.slice(1);
    const changedIndexes = newRanking.reduce((acc, item, index) => {
      if (prevRanking[index].memberId !== item.memberId) {
        acc.push(index);
      }
      return acc;
    }, []);
    changedRows.value = changedIndexes;
  } catch (error) {
    console.error("Error fetching score info:", error);
  }
};

onMounted(() => {
  getScore(props.sidoId);
  setInterval(() => {
    getScore(props.sidoId);
  }, 5000);
});
</script>

<template>
  <div class="score-container">
    <div class="my-score">
      <h4>내 점수</h4>
      <table>
        <thead>
          <tr>
            <th>아이디</th>
            <th>점수</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ myId }}</td>
            <td>{{ myScore }}점</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="all-score">
      <h4>랭킹</h4>
      <table>
        <thead>
          <tr>
            <th>순위</th>
            <th>아이디</th>
            <th>점수</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(scoreItem, index) in scoreList.slice(1)"
            :key="scoreItem.member_id"
            :class="{ 'changed-row': changedRows.includes(index) }"
          >
            <td>{{ index + 1 }}</td>
            <td>{{ scoreItem.memberId }}</td>
            <td>{{ scoreItem.score }}점</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.score-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 250px;
  margin: auto;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.my-score,
.all-score {
  width: 100%;
  margin-bottom: 10px;
}

h4 {
  margin-bottom: 10px;
  font-size: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #dddddd;
  font-weight: bold;
  text-align: left;
  padding: 8px;
  font-size: 17px;
}

th {
  background-color: bisque;
  font-weight: bold;
}

tr:hover {
  background-color: #ddd;
}

.changed-row {
  background-color: bisque; 
  animation: changeEffect 5s; 
}

@keyframes changeEffect {
  from {
    background-color: chocolate; 
  }
  to {
    background-color: bisque; 
  }
}
</style>
