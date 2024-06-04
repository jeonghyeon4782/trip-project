<script setup>
import { getModifyReview, modifyReview, registReview } from "@/api/review";
import { listAttractionInfo } from "@/api/attractionInfo";
import { ref, watch, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import Swal from "sweetalert2";

const router = useRouter();
const route = useRoute();

const props = defineProps({ type: String });

const file = ref({});
const preView = ref(null);

onMounted(() => {
  getboardLogs();
});

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: "확인",
    customClass: {
      title: "swal-title",
      text: "swal-text",
    },
  });
};

const review = ref({
  reviewId: 0,
  title: "",
  content: "",
  attractionInfoId: "",
});

const boardLogs = ref({});

if (props.type == "boardReview") {
  let { boardLogId } = route.params;
  console.log(
    "boardReview",
    review.value.attractionInfoId,
    "boardLogId",
    boardLogId
  );
  review.value.attractionInfoId = boardLogId;
}

if (props.type == "modify") {
  let { reviewid } = route.params;
  console.log(reviewid + "번 리뷰 수정 요청");
  getModifyReview(
    reviewid,
    ({ data }) => {
      review.value = data.data;
      preView.value = data.data.reviewImageUrl;
    },
    (error) => {
      console.log(error);
    }
  );
}

const titleErrMsg = ref("");
const contentErrMsg = ref("");
const attractionErrMsg = ref("");

watch(
  () => review.value.title,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 30) {
      titleErrMsg.value = "제목을 확인해 주세요!!!";
    } else titleErrMsg.value = "";
  },
  { immediate: true }
);

watch(
  () => review.value.content,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 500) {
      contentErrMsg.value = "내용을 확인해 주세요!!!";
    } else contentErrMsg.value = "";
  },
  { immediate: true }
);

watch(
  () => review.value.attractionInfoId,
  (value) => {
    if (value == "") {
      attractionErrMsg.value = "어트렉션을 선택해주세요.";
    } else attractionErrMsg.value = "";
  },
  { immediate: true }
);

function onSubmit() {
  if (titleErrMsg.value) {
    alert(titleErrMsg.value);
  } else if (contentErrMsg.value) {
    alert(contentErrMsg.value);
  } else if (attractionErrMsg.value) {
    alert(attractionErrMsg.value);
  } else {
    props.type === "modify" ? updateReivew() : writeReview();
  }
}

const getboardLogs = () => {
  console.log("board log 요청");
  listAttractionInfo(
    ({ data }) => {
      console.log(data);
      boardLogs.value = data.data;
      console.log(boardLogs);
    },
    (error) => {
      console.log(error);
    }
  );
};

function writeReview() {
  console.log("리뷰 등록 요청", review.value);
  registReview(
    review.value,
    file.value,
    (response) => {
      let msg = "리뷰등록 처리시 문제 발생했습니다.";
      console.log(response);
      if (response.status == 201) msg = "리뷰등록이 완료되었습니다.";
      showSwal('success', msg, null);
      const reviewId = response.data.data.reviewId;
      moveDetail(reviewId);
    },
    (error) => console.log(error)
  );
}

function updateReivew() {
  console.log(review.value.reviewId + "번 리뷰 수정 요청", review.value);
  modifyReview(
    review.value,
    review.value.reviewId,
    file.value,
    (response) => {
      let msg = "리뷰수정 처리시 문제 발생했습니다.";
      if (response.status == 201) msg = "리뷰정보 수정이 완료되었습니다.";
      showSwal('success', msg, null);
      const reviewId = response.data.data.reviewId;
      moveDetail(reviewId);
    },
    (error) => console.log(error)
  );
}

function moveDetail(reviewId) {
  router.replace({ name: "review-view", params: { reviewid: reviewId } });
}

// 이미지 업로드 처리
const handleImageUpload = (event) => {
  file.value = event.target.files[0];
  preView.value = URL.createObjectURL(file.value);
};

function goBack() {
  router.go(-1);
}
</script>

<template>
  <a @click="goBack" style="font-size: 20px; cursor: pointer">뒤로가기</a>
  <div class="item">
    <div class="review-image">
      <img v-if="preView" :src="preView" :alt="imageAlt" />
    </div>
    <form @submit.prevent="onSubmit">
      <div class="form-group">
        <label for="title" style="font-size: 20px; margin-top: 50px; text-align: left;"
          >제목</label
        >
        <input
          type="text"
          id="title"
          v-model="review.title"
          placeholder="제목을 입력하세요."
        />
      </div>
      <div class="form-group">
        <label for="content" style="font-size: 20px; margin-top: 50px; text-align: left;"
          >내용</label
        >
        <textarea
          id="content"
          v-model="review.content"
          rows="6"
          placeholder="내용을 입력하세요."
        ></textarea>
      </div>
      <div class="form-group">
        <label for="attractionList" style="font-size: 20px; margin-top: 50px; text-align: left;"
          >보드 기록 목록</label
        >
        <select id="attractionList" v-model="review.attractionInfoId">
          <option disabled value="">
            리뷰를 작성할 보드 기록을 선택하세요
          </option>
          <option
            v-for="boardLog in boardLogs"
            :key="boardLog.attractionInfoId"
            :value="boardLog.attractionInfoId"
          >
            이름: {{ boardLog.name }} | 위치: {{ boardLog.sidoName }} | 생성일:
            {{ boardLog.createDate }}
          </option>
        </select>
      </div>
      <!--  -->
      <div class="form-group">
        <label for="image" class="custom-file-upload">
          <span>파일 선택</span>
        </label>
        <input
          type="file"
          id="image"
          accept="image/*"
          @change="handleImageUpload"
          style="display: none"
        />
      </div>
      <button type="submit" class="btn" style="margin-top: 70px">완료</button>
    </form>
  </div>
</template>

<style scoped>
* {
  font-family: "Gaegu", cursive;
}

.review-image > img {
  max-width: 50%;
  border-radius: 10px;
}

.item {
  margin: 20px auto;
  margin-top: 50px;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px #f1f1f6;
  text-align: center; /* 내부 요소들을 가로 방향으로 중앙 정렬 */
}

.item h2 {
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group textarea,
#attractionList {
  width: 97%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 20px;
}

.form-group textarea {
  height: 400px;
}

.btn {
  margin-top: 10px;
  display: block;
  width: 20%; /* 입력창과 동일한 너비로 조정 */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
  color: black;
  text-align: center;
  cursor: pointer;
  margin: 0 auto; /* 중앙 정렬 */
  font-family: "Gaegu", cursive;
  font-size: 25px;
  font-weight: bold;
}

.btn:hover {
  background-color: #e3effa;
}

a:hover {
  background-color: #e3effa;
}

.custom-file-upload {
  margin-top: 10px;
  display: block;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: white;
  color: black;
  text-align: center;
  cursor: pointer;
  margin: 0 auto; /* 중앙 정렬 */
  margin-top: 50px;
  font-family: "Gaegu", cursive;
  font-size: 20px;
}

.custom-file-upload:hover {
  background-color: #e3effa;
}
</style>
