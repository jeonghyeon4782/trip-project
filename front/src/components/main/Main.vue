<script setup>
import defaultImage from "@/assets/defaultImage.jpg";
import { ref, watch, onMounted } from "vue";
import { useRouter } from "vue-router";
import Swal from "sweetalert2";

import { getTopAttractionInfo } from "@/api/attractionInfo";
import { listReview, updateHits } from "@/api/review.js";
import { getSidoList } from "@/api/board.js";
import { listAttractionInfo } from "@/api/attractionInfo";

import SeoImage from "@/assets/서두나.png";
import ParkImage from "@/assets/박찬호.png";
import KimImage from "@/assets/김태균.png";
import WelcomeImage from "@/assets/welcome.jpg";

// import seoulImage from '@/assets/Seoul-attractions-N-Seoul-Tower.jpg';
// import busanImage from '@/assets/busan.jpg'
const router = useRouter();

const carousel = ref(null);
const attractions = ref({});
const reviews = ref([]);
const sidos = ref([]);

const confirmLogin = (sidoId, name) => {
  listAttractionInfo(
    (response) => {
      handleImageClick(sidoId, name)
    },
    (error) => {
      showSwal("error", "로그인을 해주세요", null);
    }
  );
}


const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: '확인'
  });
};

onMounted(() => {
  getAttractions();
  getReviewList();
  getSidos();
});

const param = ref({
  pageno: 1,
  pagesize: 4,
  keyword: "",
  order: "like",
  sidos: [],
});

const getAttractions = () => {
  console.log("관광지 정보 요청");
  getTopAttractionInfo(
    ({ data }) => {
      console.log(data);
      attractions.value = data.data;
      console.log(attractions);
    },
    (error) => {
      console.log(error);
    }
  );
};

const getReviewList = () => {
  console.log("서버에 review 목록 요청", param.value);
  listReview(
    param.value,
    ({ data }) => {
      console.log(data.msg);
      console.log(data);
      console.log(data.data.reviewInfos);
      reviews.value = data.data.reviewInfos;
    },
    (error) => {
      console.log(error);
    }
  );
};

const getSidos = () => {
  console.log("sido 목록 요청");
  getSidoList(
    ({ data }) => {
      console.log(data.msg);
      console.log(data);
      sidos.value = data.data;
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

const handleImageClick = (sidoId, name) => {
  console.log("해당 시도를 찾습니다." + sidoId);
  onGetCharacterSelection(sidoId, name);
};

const nextSlide = () => {
  if (carousel.value) {
    carousel.value.next();
  }
};

const prevSlide = () => {
  if (carousel.value) {
    carousel.value.prev();
  }
};

const onGetCharacterSelection = (sidoId, name) => {
  Swal.fire({
    title: "캐릭터를 선택해주세요!",
    html: `
    <button class="image-button" data-id="1" style="margin: 5px; border: none; background: none;">
    <img src="${SeoImage}" style="border-radius: 50%; width: 170px; height: 170px; cursor: pointer;" alt="서두나">
    <span style="display: block; text-align: center; font-size:30px">서두나</span>
  </button>
  <button class="image-button" data-id="2" style="margin: 5px; border: none; background: none;">
    <img src="${ParkImage}" style="border-radius: 50%; width: 170px; height: 170px; cursor: pointer;" alt="박찬호">
    <span style="display: block; text-align: center; font-size:30px">박찬호</span>
  </button>
  <button class="image-button" data-id="3" style="margin: 5px; border: none; background: none;">
    <img src="${KimImage}" style="border-radius: 50%; width: 170px; height: 170px; cursor: pointer;" alt="김태균">
    <span style="display: block; text-align: center; font-size:30px">김태균</span>
  </button>
    `,
    showCloseButton: true,
    showConfirmButton: false,
    focusConfirm: false,
    allowOutsideClick: false,
    didOpen: () => {
      const imageButtons =
        Swal.getHtmlContainer().querySelectorAll(".image-button");
      imageButtons.forEach((imageButton) => {
        imageButton.addEventListener("click", () => {
          const imageId = imageButton.getAttribute("data-id");
          Swal.close();
          Swal.fire({
            title: "부루마블 시작!",
            text: "즐거운 부루마블하세요🎉🎉",
            imageUrl: "src/assets/welcome.jpg",
            imageWidth: 400,
            imageHeight: 200,
            imageAlt: "환영",
            confirmButtonText: "확인",
          });
          showBoardMain(sidoId, imageId, name);
        });
      });
    },
  });
};

function showBoardMain(sidoId, imageId, name) {
  router.push({ name: "board", params: { sidoId, imageId, name } });
}

</script>

<template>
  <main class="main-wrapper">
    <h2 class="centered-text">🎲 부루마블</h2>
    <div class="carousel-container">
      <Carousel
        ref="carousel"
        :items-to-show="1"
        :items-to-scroll="1"
        :wrapAround="true"
        :autoplay="3000"
        pauseAutoplayOnHover="true"
      >
      <Slide v-for="sido in sidos" :key="sido" class="carousel-slide">
        <img
        :src="sido.imageUrl"
        alt="carousel image"
        @click="confirmLogin(sido.sidoId, sido.name)"
        class="carousel-image"
        />
        <div class="sido-name">{{ sido.name }}</div>
        </Slide>
    </Carousel>
    </div>
    <button @click="prevSlide" class="carousel-nav-button prev-button">
      ‹
    </button>
    <button @click="nextSlide" class="carousel-nav-button next-button">
      ›
    </button>

    <hr />

    <h2 class="centered-text">🧳 여행지 추천</h2>
    <div class="gray-band">
      <div class="image-gallery">
        <div
          class="image-item"
          v-for="(attraction, index) in attractions"
          :key="index"
        >
          <img :src="attraction.url" class="profile-image" />
          <div class="overlay">
            <div class="text">{{ attraction.name }}</div>
            <div class="text">{{ attraction.sidoName }}</div>
          </div>
        </div>
      </div>
    </div>

    <hr />

    <h2 class="centered-text">✍️ 인기 게시글</h2>
    <div class="description">
      <div class="image-gallery">
        <div
          class="image-item"
          v-for="review in reviews"
          :key="review.reviewId"
        >
          <RouterLink
            :to="{ name: 'review-view', params: { reviewid: review.reviewId } }"
            @click="incrementHits"
          >
            <img
              :src="
                review.reviewImageUrl ? review.reviewImageUrl : defaultImage
              "
              class="profile-image"
            />
            <div class="text">{{ review.title }}</div>
            <div class="text">♥️ {{ review.likes }}</div>
          </RouterLink>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
.carousel-container {
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
  text-align: center
}

.carousel-slide {
  display: flex;
  width: 100%;
  height: 800px;
  position: relative; /* 부모 요소를 상대 위치로 설정 */

}

.carousel-image {
  min-width: 1400px;
  cursor: pointer;
  transform: translateX(-40px); /* 이미지를 초기에 왼쪽으로 이동시킴 */
}

.carousel-nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  z-index: 1;
}

.prev-button {
  left: 100px;
  margin-top: 150px
}

.next-button {
  right: 100px;
  margin-top: 150px
}

.sido-info {
  font-size: 30px;
  width: 100%;
  text-align: left;
}

.description {
  background-color: white;
  clear: both;
  margin-bottom: 30px;
}

.gray-band {
  background-color: white;
  margin-bottom: 30px;
}

.image-gallery {
  display: flex;
  gap: 10px;
  text-align: center;
  font-weight: 800;
}

.image-item {
  position: relative;
  width: 25%;
  border: 2px solid #f1ebeb;
  padding: 20px;
}

.profile-image {
  width: 100%;
  display: block;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  /* 어두운 오버레이 */
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.image-item:hover .overlay {
  opacity: 1;
}

.text {
  margin: 5px 0;
}

hr {
  margin-top: 100px;
  margin-bottom: 100px;
  border: 1px solid #f1ebeb;
}

.centered-text {
  text-align: center;
  font-size: 30px;
}

.sido-name {
    position: absolute; /* 절대 위치로 설정 */
    top: 10px; /* 원하는 위치 조정 */
    left: 10px; /* 원하는 위치 조정 */
    z-index: 999; /* 다른 요소 위에 표시되도록 설정 */
    color: black; /* 원하는 색상 설정 */
    font-size: 50px;
    font-weight: bolder;
    margin-left: 200px;
    margin-top: 20px;
    border-radius: 30%;
    padding: 5px 10px; /* 여백 설정 */
}

</style>
