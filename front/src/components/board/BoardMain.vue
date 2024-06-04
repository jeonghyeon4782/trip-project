<script setup>
import { ref, onBeforeMount, onBeforeUnmount  } from "vue";
import Space from "./item/Space.vue";
import Dice from "./item/Dice.vue";
import Horse from "./item/Horse.vue";
import Score from "./item/Score.vue";
import Swal from "sweetalert2";
import { useRoute, useRouter } from "vue-router";

import { getBoard, getAttractionInfo, updateLocation, drawGoldCard, updateScore } from "@/api/board";

import doubleImage from "@/assets/double.jpg";
import goldImage from "@/assets/gold-key.jpg";
import startImage from "@/assets/start.jpg";
import spaceGif from "@/assets/space.gif";
import spaceImage from "@/assets/space-travel.jpg";
import islandImage from "@/assets/island.jpg";

import gameStart from "@/assets/game-start.mp3"; 
import spaceStart from "@/assets/space-start.mp3"; 
import islandStart from "@/assets/island-start.mp3"; 
import move from "@/assets/move.mp3"; 

let isFirst = false;
const isRolling = ref(false);
const route = useRoute();
const router = useRouter();
const sidoId = route.params.sidoId;
const sidoName = route.params.name;
const imageId = parseInt(route.params.imageId);

const startAudio = new Audio(gameStart);
startAudio.loop = true;

const spaceAudio = new Audio(spaceStart);
spaceAudio.loop = true;

const islandAudio = new Audio(islandStart);
islandAudio.loop = true;

const moveAudio = new Audio(move);
moveAudio.loop = true;

let attractionInfo = {
  name: "",
  addr: "",
  url: "",
  description: "",
  latitude: null,
  longitude: null,
};

const mapInfo = ref({
  now: null,
  mapId: null,
  island: false,
  islandCnt: 0,
  spaces: [],
});

const horse = ref({
  horseTop: null,
  horseLeft: null,
});

const horseLeftArr = [
  850, 725, 600, 475, 350, 225, 100, -25, -25, -25, -25, -25, -25, -25, -25,
  100, 225, 350, 475, 600, 725, 850, 850, 850, 850, 850, 850,
];
const horseTopArr = [
  850, 850, 850, 850, 850, 850, 850, 850, 725, 600, 475, 350, 225, 100, -25,
  -25, -25, -25, -25, -25, -25, -25, 100, 225, 350, 475, 600, 725, 850,
];

const getRandCard = () => {
  return Math.round(Math.random() * 7 + 1);
};

const spaceTravelSwal = () => {
  Swal.fire({
    title: "우주여행",
    text: "당신을 우주여행으로 초대합니다!",
    imageUrl: spaceImage,
    input: "text",
    inputPlaceholder: "어디로 가고 싶으신가요??(0부터 27중 선택)",
    imageWidth: 400,
    imageHeight: 200,
    imageAlt: "우주여행",
    confirmButtonText: "확인",
  }).then((result) => {
    if (result.isConfirmed) {
      startAudio.pause();
      spaceAudio.play();
      const destination = result.value;
      Swal.fire({
        title: "꽉 잡으세요...",
        html: `<img src=${spaceGif} width='400' height='400'>`,
        showConfirmButton: false,
        timer: 5000,
        didOpen: () => {
          Swal.showLoading();
        },
      }).then(() => {
        spaceAudio.pause();
        startAudio.play();
        onUpdateLocation(destination);
        if (21 > destination) {
          onUpdateScore(50);
          startSwal();
        }
      });
    }
  });
};

const onDrawGoldCard = () => {
  let index = getRandCard();
  drawGoldCard(
    {
      memberBoardMapId: mapInfo.value.mapId,
      index: index,
      sidoId: sidoId,
    },
    (response) => {
      goldCardSwal(response.data.data.msg, response.data.data.nowLocation, index);
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
  });
};

const goldCardSwal = (msg, location, index) => {
  Swal.fire({
    title: "황금열쇠",
    html: msg,
    imageUrl: goldImage,
    imageWidth: 250,
    imageHeight: 300,
    imageAlt: "황금 카드",
    confirmButtonText: "확인",
  }).then((result) => {
    if (result.isConfirmed) {
      if (index > 0 && index < 5) {
        onUpdateLocation(location);
      }
    }
  });
};

const islandSwal = (islandCnt) => {
  Swal.fire({
    title: "무인도",
    html: `아쉽게도 무인도네요..<br>${
      2 - islandCnt
    }턴동안 이동이 불가합니다.<br>Double이 나오면 탈출이 가능합니다!`,
    imageUrl: islandImage,
    imageWidth: 400,
    imageHeight: 250,
    imageAlt: "무인도",
    confirmButtonText: "확인",
  });
};

const doubleSwal = () => {
  Swal.fire({
    title: "DOUBLE🎊🎊",
    html: "같은 숫자가 나오셨네요! 50점을 획득합니다!",
    imageUrl: doubleImage,
    imageWidth: 400,
    imageHeight: 250,
    imageAlt: "더블",
    confirmButtonText: "확인",
  });
};

const startSwal = () => {
  Swal.fire({
    title: "완주🎊🎊",
    html: "벌써 한바퀴를 완주했습니다!<br>50점을 획득합니다!!",
    imageUrl: startImage,
    imageWidth: 400,
    imageHeight: 250,
    imageAlt: "완주",
    confirmButtonText: "확인",
  });
};

const attractionInfoSwal = () => {
  Swal.fire({
    title: `${attractionInfo.name}`,
    html: `<b>주소</b> : ${attractionInfo.addr}<br><br><b>설명</b> : ${attractionInfo.description}`,
    imageUrl: attractionInfo.url,
    imageWidth: 400,
    imageHeight: 250,
    imageAlt: "관광지 사진",
    showCancelButton: true,
    confirmButtonText: "글 작성",
    cancelButtonText: "취소",
  }).then((result) => {
    // 확인 버튼을 클릭했을 때 실행되는 콜백 함수
    if (result.isConfirmed) {
      router.push({ name: "review-write"});
    }
  });
};


onBeforeMount(() => {
  startAudio.play();
  getBoard(
    sidoId,
    (response) => {
      mapInfo.value.spaces = response.data.data.boardInfoVoList;
      mapInfo.value.mapId = response.data.data.boardMemberMapId;
      mapInfo.value.now = response.data.data.nowLocation;
      mapInfo.value.islandCnt = response.data.data.islandCnt;
      console.log(mapInfo.value.islandCnt);
      if (mapInfo.value.now == 7) {
        isIslandTime == true;
        startAudio.pause();
        islandAudio.play();
      }
      horse.value.horseTop = horseTopArr[response.data.data.nowLocation];
      horse.value.horseLeft = horseLeftArr[response.data.data.nowLocation];
      if (mapInfo.value.now === 0) {
        isFirst = true;
      }
    },
    (error) => {
      showSwal("error", "오류가 발생했습니다...", null);
    }
  );
});

// 페이지를 떠날 때 오디오를 멈추는 beforeRouteLeave 가드 추가
onBeforeUnmount(() => {
  startAudio.pause();
  startAudio.currentTime = 0;
  islandAudio.pause();
  islandAudio.currentTime = 0;
  spaceAudio.pause();
  spaceAudio.currentTime = 0;
});

let isIslandTime = false;

const moveHorse = async (diceValues) => {
  console.log(isIslandTime)
  console.dir(diceValues)
  if (isIslandTime === true && diceValues[3] === 0) {
    isIslandTime = false;
    islandAudio.pause();
    startAudio.play();
  }
  let moveCount = 0;
  const moveInterval = 500;

  const moveOneStep = () => {
    moveAudio.play();
    if (moveCount < diceValues[0] + diceValues[1]) {
      if (mapInfo.value.now === 0 && isFirst == false) {
        onUpdateScore(50);
        startSwal();
      } else {
        isFirst = false;
      }
      if (diceValues[3] === 0) {
        mapInfo.value.now = (mapInfo.value.now + 1) % 28;
        horse.value.horseTop = horseTopArr[mapInfo.value.now];
        horse.value.horseLeft = horseLeftArr[mapInfo.value.now];
      } else {
        moveCount = diceValues[0] + diceValues[1] - 1;
      }
      moveCount++;
      setTimeout(moveOneStep, moveInterval);
    } else {
      setTimeout(async () => {
        if (
          diceValues[0] === diceValues[1] &&
          mapInfo.value.now !== 7 &&
          mapInfo.value.now !== 21 &&
          mapInfo.value.now !== 9 &&
          mapInfo.value.now !== 12 &&
          mapInfo.value.now !== 25 &&
          mapInfo.value.now !== 18 &&
          mapInfo.value.now !== 3
        ) {
          moveAudio.pause();
          doubleSwal();
          onUpdateScore(50);
        } else {
          if (mapInfo.value.now === 7) {
            moveAudio.pause();
            if (isIslandTime === false) {
              startAudio.pause();
              islandAudio.play();
              isIslandTime = true;
            }
            islandSwal(diceValues[3]);
          } else if (
            mapInfo.value.now === 9 ||
            mapInfo.value.now === 12 ||
            mapInfo.value.now === 25 ||
            mapInfo.value.now === 18 ||
            mapInfo.value.now === 3
          ) {
            moveAudio.pause();
            onDrawGoldCard();
          } else if (mapInfo.value.now === 21) {
            moveAudio.pause();
            spaceTravelSwal();
          } else {
            moveAudio.pause();
            await onGetAttractionInfo(mapInfo.value.mapId);
            attractionInfoSwal();
          }
        }
      }, 1000);
    }
  };
  setTimeout(moveOneStep, moveInterval);
};

const onGetAttractionInfo = (mapId) => {
  return new Promise((resolve, reject) => {
    getAttractionInfo(
      mapId,
      (response) => {
        attractionInfo.name = response.data.data.name;
        attractionInfo.addr = response.data.data.addr;
        attractionInfo.description = response.data.data.description;
        attractionInfo.url = response.data.data.url;
        resolve(); // 데이터가 성공적으로 가져와지면 프로미스를 resolve합니다.
      },
      (error) => {
        console.error(error);
        reject(error); // 에러 발생 시 프로미스를 reject합니다.
      }
    );
  });
};

// 위치 이동
const onUpdateLocation = (newLocation) => {
  updateLocation(
    {
      location: newLocation,
      memberBoardMapId: mapInfo.value.mapId,
    },
    (response) => {
      mapInfo.value.now = response.data.data.nowLocation;
      horse.value.horseTop = horseTopArr[mapInfo.value.now];
      horse.value.horseLeft = horseLeftArr[mapInfo.value.now];
      if (mapInfo.value.now === 7) {
        startAudio.pause();
        islandAudio.play();
        isIslandTime = true;
        islandSwal(0);
      } else if (
        mapInfo.value.now === 9 ||
        mapInfo.value.now === 12 ||
        mapInfo.value.now === 25 ||
        mapInfo.value.now === 18 ||
        mapInfo.value.now === 3
      ) {
        onDrawGoldCard();
      } else if (mapInfo.value.now === 21) {
        spaceTravelSwal();
      } else {
        onGetAttractionInfo(mapInfo.value.mapId).then(() => {
          attractionInfoSwal();
        });
      }
    },
    (error) => {
      console.log(error);
    }
  );
};

const onUpdateScore = (score) => {

  updateScore(
    sidoId,
    {
      plusScore: score,
    },
    (response) => {
      console.log(response.data.msg);
    },
    (error) => {
      console.log(error);
    }
  );
};
</script>

<template>
  <div class="center-container">
  <h3>{{ sidoName }}에 오신걸 환영합니다🎲</h3>
  <div class="container">
    <div
      :style="{
        position: 'absolute',
        top: horse.horseTop + 'px',
        left: horse.horseLeft + 'px',
        transition: 'left 1s, top 1s, right 1s, bottom 1s',
      }"
    >
      <Horse :now="mapInfo.now" :imageId="imageId" />
    </div>

    <table>
      <tr>
        <td v-for="index in 4" :key="index">
          <Space :attraction-name="mapInfo.spaces[index + 13].attractionName" />
        </td>
        <td class="gold-card"><p>황금카드</p></td>
        <td v-for="index in 2" :key="index">
          <Space :attraction-name="mapInfo.spaces[index + 18].attractionName" />
        </td>
        <td class="space-travel">
          <p style="color: white">우주여행</p>
        </td>
      </tr>
      <tr>
        <td>
          <Space :attraction-name="mapInfo.spaces[13].attractionName" />
        </td>
        <td class="center" colspan="6" rowspan="6">
          <div class="center-content">
            <div class="score-content">
              <Score :sidoId="sidoId"/>
            </div>
            <div class="dice-content">
              <Dice
                :mapId="mapInfo.mapId"
                :isRolling="isRolling"
                @moveHorse="moveHorse"
              />
            </div>
          </div>
        </td>
        <td>
          <Space :attraction-name="mapInfo.spaces[22].attractionName" />
        </td>
      </tr>
      <tr>
        <td class="gold-card"><p>황금카드</p></td>
        <td>
          <Space :attraction-name="mapInfo.spaces[23].attractionName" />
        </td>
      </tr>
      <tr>
        <td>
          <Space :attraction-name="mapInfo.spaces[11].attractionName" />
        </td>
        <td>
          <Space :attraction-name="mapInfo.spaces[24].attractionName" />
        </td>
      </tr>
      <tr>
        <td>
          <Space :attraction-name="mapInfo.spaces[10].attractionName" />
        </td>
        <td class="gold-card"><p>황금카드</p></td>
      </tr>
      <tr>
        <td class="gold-card"><p>황금카드</p></td>
        <td>
          <Space :attraction-name="mapInfo.spaces[26].attractionName" />
        </td>
      </tr>
      <tr>
        <td>
          <Space :attraction-name="mapInfo.spaces[8].attractionName" />
        </td>
        <td>
          <Space :attraction-name="mapInfo.spaces[27].attractionName" />
        </td>
      </tr>
      <tr>
        <td class="island"><p>무인도</p></td>
        <td v-for="index in 3" :key="index">
          <Space :attraction-name="mapInfo.spaces[7 - index].attractionName" />
        </td>
        <td class="gold-card"><p>황금카드</p></td>
        <td v-for="index in 2" :key="index">
          <Space :attraction-name="mapInfo.spaces[3 - index].attractionName" />
        </td>
        <td class="start"><p>출발지</p></td>
      </tr>
    </table>
  </div>
</div>
</template>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 20px 20px 10px rgba(0, 0, 0, 0.5);
}

td,
th {
  width: 100px;
  height: 100px;
  background-color: #a2bc6c;
  border: 1px solid black;
  text-align: center;
}

p {
  font-size: 20px;
  font-weight: bold;
  margin-top: 55px;
}

.center-container {
  width: 100%;
  height: 1200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #7EAF44;
}

.container {
  height: 1000px;
  width: 1000px;
  position: relative;
  display: flex;
  justify-content: center;
}

.center {
  background-image: url("/src/assets/logo.png");
  background-size: cover;
  background-position: center;
}

.center-content {
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.score-content {
  margin-top: 30px;
  margin-left: 400px;
}

.start {
  background-image: url("/src/assets/start.jpg");
  background-size: cover;
  background-position: center;
}

.island {
  background-image: url("/src/assets/island.jpg");
  background-size: cover;
  background-position: center;
}

.space-travel {
  background-image: url("/src/assets/space-travel.jpg");
  background-size: cover;
  background-position: center;
}

.gold-card {
  background-image: url("/src/assets/gold-key.jpg");
  background-size: cover;
  background-position: center;
}
</style>
