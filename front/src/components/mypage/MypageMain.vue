<script setup>
import { detailMember, getModifyMember, deleteMember } from "@/api/member";
import { listAttractionInfo } from "@/api/attractionInfo";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import Swal from "sweetalert2";

import BoardLogListItem from '@/components/mypage/item/BoardLogListItem.vue';


import defaultImage from '@/assets/defaultImage.jpg'
import editIcon from '@/assets/edit.svg';
import deleteIcon from '@/assets/delete.svg';
import imageSrc from '@/assets/image1.jpg';

const route = useRoute();
const router = useRouter();

const boardLogs=ref({});


onMounted(() => {
  getMember();
  getboardLogs();
})

const member = ref({});
const editMember = ref({});
const inputPassword = ref({
  password: ''
});

const isEditing = ref(false);

const getMember = () => {
  console.log("회원 정보 요청");
  detailMember(
    ({ data }) => {
      console.log(data);
      member.value = data.data;
      console.log(member);
    },
    (error) => {
      console.log(error);
    }
  );
};

const getEditMember = (password) => {
  return new Promise((resolve, reject) => {
    console.log("수정 회원 정보 요청", password);
    getModifyMember(
      password,
      ({ data }) => {
        console.log(data);
        editMember.value = data.data;
        console.log(editMember);
        isEditing.value = true;
        resolve(); // 요청이 성공하면 resolve 호출
      },
      (error) => {
        console.log(error);
        isEditing.value = false;
        reject(error); // 요청이 실패하면 reject 호출
      }
    );
  });
};

const ondeleteMember = (password) => {
  return new Promise((resolve, reject) => {
    console.log("회원 삭제 요청", password);
    deleteMember(
      password,
      ({ data }) => {
        console.log(data);
        editMember.value = data.data;
        console.log(editMember);
        isEditing.value = true;
        resolve(); // 요청이 성공하면 resolve 호출
      },
      (error) => {
        console.log(error);
        isEditing.value = false;
        reject(error); // 요청이 실패하면 reject 호출
      }
    );
  });
};


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


function editClick() {
  Swal.fire({
    title: '비밀번호를 입력해주세요.',
    input: 'password',
    inputAttributes: {
      autocapitalize: 'off'
    },
    showCancelButton: true,
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    preConfirm: (password) => {
      return getEditMember(password)
        .then(() => {
          if (isEditing.value) {
            // 비밀번호가 맞으면 페이지 이동
            console.log("페이지 이동 요청", isEditing.value);
            router.replace({ name: 'member-modify', query: { member: JSON.stringify(member.value) } });
          } else {
            // 비밀번호가 틀리면 에러 메시지 표시
            Swal.fire({
              icon: 'error',
              title: '비밀번호를 틀렸습니다.',
              text: '다시 시도해주세요.'
            });
            throw new Error('Incorrect password');
          }
        })
        .catch((error) => {
          // 에러 발생 시 처리
          Swal.fire({
            icon: 'error',
            title: '비밀번호를 틀렸습니다.',
            text: '다시 시도해주세요.'
          });
        });
    },
    allowOutsideClick: () => !Swal.isLoading()
  });
}

function deleteClick() {
  Swal.fire({
    title: '비밀번호를 입력해주세요.',
    input: 'password',
    inputAttributes: {
      autocapitalize: 'off'
    },
    showCancelButton: true,
    confirmButtonText: '확인',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    preConfirm: (password) => {
      return ondeleteMember(password)
        .then(() => {
          if (isEditing.value) {
            // 비밀번호가 맞으면 페이지 이동
            localStorage.setItem("isLogin", false);
            location.reload();
            location.href = "/auth";
          } else {
            // 비밀번호가 틀리면 에러 메시지 표시
            Swal.fire({
              icon: 'error',
              title: '비밀번호를 틀렸습니다.',
              text: '다시 시도해주세요.'
            });
            throw new Error('Incorrect password');
          }
        })
        .catch((error) => {
          // 에러 발생 시 처리
          Swal.fire({
            icon: 'error',
            title: '비밀번호를 틀렸습니다.',
            text: '다시 시도해주세요.'
          });
        });
    },
    allowOutsideClick: () => !Swal.isLoading()
  });
}

</script>

<template>
  <div class="profile-card">
    <img :src="member.imageUrl ? member.imageUrl : defaultImage" class="profile-picture">
    <div class="profile-info">
      <div class="profile-top">
        <h1>{{ member.nickname }}</h1>
        <img :src="editIcon" class="edit-icon" @click="editClick">
        <img :src="deleteIcon" class="delete-icon" @click="deleteClick">
      </div>
      <p class="member-id" style="font-size: 25px;">{{ member.memberId }}</p>
      <p class="member-email" style="font-size: 25px;">{{ member.email }}</p>
    </div>
    <div class="info-container">
      <label for="posts">Posts</label>
      <p id="posts">{{ member.reviews }}</p>
      <label for="score">Score</label>
      <p id="score">{{ member.score }}</p>
    </div>
  </div>
  <div class="board-log">
    <h2 style="margin-top: 100px;">나의 여행일기</h2>
    <!-- My Trip log -->
    <div class="board-log-wrapper">
      <div class="board-log-list">
        <BoardLogListItem v-for="boardLog in boardLogs" :key="boardLog.boardLogId" :boardLog="boardLog" class="board-log-item">
        </BoardLogListItem>
      </div>
    </div>
  </div>
</template>

<style scoped>
.menu-item {
  cursor: pointer;
  margin-bottom: 10px;
}

.profile-card {
  display: flex;
  position: relative; /* 추가 */
  margin: 0 auto;
  width: 80%;
  height: 200px;
  align-items: center;
  padding: 20px;
  border: 1px solid #f1ebeb;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-picture {
  width: 150px;
  height: 150px;
  border: 3px solid #f1ebeb;
  border-radius: 50%;
  margin-right: 20px;
}

.profile-info {
  margin-left: 50px;
  margin-bottom: 30px;
  flex: 1;
}

.profile-top {
  display: flex;
}

.info-container {
  display: flex;
  font-size: 25px;
  align-items: center;
  text-align: center;
}

.info-container p {
  margin-right: 30px;
  margin-left: 10px;
}

.profile-info h2 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 1.5rem;
}

.profile-info p {
  margin: 0;
  font-size: 20px
}

.edit-icon {
  width: 30px;
  height: 30px;
  margin-left: 10px;
  margin-top: 23px;
  cursor: pointer;
}

.delete-icon {
  position: absolute; /* 추가 */
  top: 20px; /* 적절한 값으로 조절 */
  right: 30px; /* 적절한 값으로 조절 */
  cursor: pointer;
}

.board-log{
  width: 85%;
  margin: 0 auto;
}

.board-log-wrapper {
  margin-top: 50px;
  overflow-x: auto;
  white-space: nowrap;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 1%;
}

.board-log-list {
  display: flex;
  gap: 10px;
  width: 100%;
}

.board-log-item {
  flex: 0 0 25%;
  max-width: 25%; 
  box-sizing: border-box;
  background-color: white;
}
</style>