<script setup>
import {
  duplicateCheckNickname,
  authenticationEmail,
  resendAuthenticationKey,
  deleteAuthenticationKey,
  checkAuthenticationKey,
} from '@/api/auth';
import { modifyMember } from "@/api/member";
import { ref, watch, defineProps } from 'vue';
import { useRoute, useRouter } from "vue-router";
import Swal from "sweetalert2";

const route = useRoute();
const router = useRouter();

const queryMember = JSON.parse(route.query.member);

const file = ref({})
const preView = ref(null)

if(queryMember.imageUrl){
  preView.value=queryMember.imageUrl
}

const handleImageUpload = (event) => {
  file.value = event.target.files[0];
  preView.value = URL.createObjectURL(file.value);
};

const member = ref({
  memberId: queryMember.memberId,
  password: "",
  repassword: "",
  nickname: queryMember.nickname,
  email: queryMember.email
});

console.log(member);

// 중복검사 여부
const isDuplicateCheckNickname = ref(true);
// 이메일 인증 여부
const isAuthenticationEmail = ref(true);

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
  });
};

const nicknameErrMsg = ref("");
const emailErrMsg = ref("");
const passwordErrMsg = ref("");

const duplicateCheckSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
  });
};

const onDuplicateCheckNickname = () => {
  if (!isDuplicateCheckNickname.value) {
    duplicateCheckNickname(
      member.value.nickname,
      (response) => {
        duplicateCheckSwal("success", "닉네임 중복검사", response.data.msg);
        isDuplicateCheckNickname.value = true;
      },
      (error) =>
        duplicateCheckSwal("error", "닉네임 중복검사", error.response.data.msg)
    );
  } else {
    isDuplicateCheckNickname.value = false;
  }
};

const onAuthenticationEmail = () => {
  if (!isAuthenticationEmail.value) {
    let timerInterval;
    authenticationEmail(
      member.value.email,
      (response) => {
        let startTime = new Date().getTime(); // 타이머 시작 시간
        let remainingTime = 300; // 5분 타이머 (300 초)
        let timerInterval;

        const updateTimer = () => {
          const currentTime = new Date().getTime(); // 현재 시간
          const elapsedTime = Math.floor((currentTime - startTime) / 1000); // 경과 시간 (초)
          remainingTime = Math.max(0, 300 - elapsedTime); // 남은 시간 계산
          const timerElement = document.getElementById("timer");
          if (timerElement) {
            timerElement.textContent = remainingTime;
          }
          if (remainingTime <= 0) {
            clearInterval(timerInterval);
          }
        };

        const resendButtonCallback = () => {
          // 재전송 버튼 클릭 시
          resendAuthenticationKey(
            member.value.email,
            (response) => {
              const newKey = response.data.data;
              console.log(newKey);
              startTime = new Date().getTime(); // 시작 시간 업데이트
              remainingTime = 300; // 타이머 초기화
              clearInterval(timerInterval);
              timerInterval = setInterval(updateTimer, 1000);

              Swal.update({
                html: `
                인증번호를 입력해주세요.<br/><br/>
                남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
                <div style="display: flex; align-items: center;">
                  <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호 입력" style="flex: 1; margin-right: 10px;">
                  <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
                </div>
              `,
              });
              addResendButtonListener();
            },
            (error) => {
              console.error("재전송 에러 발생");
            }
          );
        };

        const addResendButtonListener = () => {
          const resendButton = document.getElementById("resend-button");
          if (resendButton) {
            resendButton.addEventListener("click", resendButtonCallback);
          }
        };

        Swal.fire({
          title: "이메일 인증",
          html: `
          인증번호를 입력해주세요.<br/><br/>
          남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
          <div style="display: flex; align-items: center;">
            <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호 입력" style="flex: 1; margin-right: 10px;">
            <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
          </div>
        `,
          icon: "success",
          showCancelButton: true,
          confirmButtonText: "확인",
          cancelButtonText: "취소",
          didOpen: () => {
            // 타이머 설정
            timerInterval = setInterval(updateTimer, 1000);
            addResendButtonListener();
          },
          preConfirm: () => {
            // 확인 버튼 클릭 시 이벤트 처리
            const verificationCode =
              document.getElementById("verification-code").value;
            checkAuthenticationKey(
              {
                email: member.value.email,
                key: verificationCode,
              },
              (response) => {
                showSwal("success", "이메일 인증", "인증 완료!!");
                isAuthenticationEmail.value = true;
              },
              (error) => {
                if (error.response.data.data == 1) {
                  Swal.update({
                    html: `
              인증번호를 입력해주세요.<br/><br/>
              남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
              <div style="display: flex; align-items: center;">
                <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호 입력" style="flex: 1; margin-right: 10px;">
                <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
              </div>
              <div id="error-message" style="color: red;">인증번호가 틀렸습니다.</div>
            `,
                  });
                  addResendButtonListener();
                } else if (error.response.data.data == 2) {
                  showSwal(
                    "error",
                    "이메일 인증",
                    "만료된 인증번호 입니다."
                  );
                }
              }
            );
            return false;
          },
        }).then((result) => {
          if (result.isDismissed) {
            // 취소 버튼을 클릭 시
            deleteAuthenticationKey(
              member.value.email,
              (response) => {
                console.log(response.data.msg);
              },
              (error) => console.log(error.response.data.msg)
            );
          }
        });
      },
      (error) => {
        showSwal("error", "이메일 인증", error.response.data.msg);
      }
    );
  } else {
    isAuthenticationEmail.value = false;
  }
};

watch(
  () => member.value.nickname,
  (value) => {
    let len = value.length;
    const regex = /^[a-zA-Z가-힣0-9]*$/;

    if (len < 3 || len > 10) {
      nicknameErrMsg.value = "닉네임은 3자 이상 10자 이하입니다.";
    } else if (!regex.test(value)) {
      nicknameErrMsg.value =
        "닉네임은 영어 소문자, 대문자, 한글, 숫자로만 이루어져야 하며, 공백이 없어야 합니다.";
    } else nicknameErrMsg.value = "";
  },
  { immediate: true }
);
watch(
  () => member.value.email,
  (value) => {
    let len = value.length;
    const regex = /^[^\s]+$/;

    if (len == 0 || len > 500) {
      emailErrMsg.value = "이메일을 확인해 주세요!!!";
    } else if (!regex.test(value)) {
      emailErrMsg.value = "이메일에 공백을 포함할 수 없습니다.";
    } else emailErrMsg.value = "";
  },
  { immediate: true }
);

watch(
  [() => member.value.password, () => member.value.repassword],
  ([password, repassword]) => {
    const regex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@_!*$%#])[A-Za-z\d@_!*$%#]{8,}$/;

    if(password.length==0){
      passwordErrMsg.value = "";
    }
    else if (password != repassword) {
      console.log(password, repassword);
      passwordErrMsg.value = "비밀번호가 서로 다릅니다.";
    } else if (password.length < 10 || password.length > 20) {
      passwordErrMsg.value = "비밀번호는 10자 이상 20자 이하입니다.";
    } else if (!regex.test(password)) {
      passwordErrMsg.value =
        "비밀번호는 영어 소문자, 대문자, 숫자, 특수 문자(@ _ ! * $ % #)를 모두 포함하여 8자 이상이어야 합니다.";
    } else {
      passwordErrMsg.value = "";
    }
  },
  { immediate: true }
);

function onSubmit() {
  if (nicknameErrMsg.value) {
    showSwal("error", "회원정보 수정 오류", nicknameErrMsg.value);
  } else if (emailErrMsg.value) {
    showSwal("error", "회원정보 수정 오류", emailErrMsg.value);
  } else if (passwordErrMsg.value) {
    showSwal("error", "회원정보 수정 오류", passwordErrMsg.value);
  } else if (!isDuplicateCheckNickname.value) {
    showSwal("error", "회원정보 수정", "닉네임 중복검사를 해주세요.");
  } else if (!isAuthenticationEmail.value) {
    showSwal("error", "회원정보 수정", "이메일 인증을 해주세요.");
  }
  else {
    editMember();
  }
}

function editMember() {
    console.log("회원 정보 수정 요청", member.value)
    // checkMember();
    modifyMember(
        member.value,
        file.value,
        (response) => {
            let msg = "회원 정보 처리시 문제 발생했습니다."
            console.log(response)
            if (response.status == 201) msg = "회원 정보 수정이 완료되었습니다."
            alert(msg)
            router.replace({ name: 'mypage-main'});
        },
        (error) => console.log(error)
    )
}


</script>

<template>
  <div class="container">
    <form @submit.prevent="onSubmit">

      <div class="form-group">
        <div class="review-image">
          <img v-if="preView" :src="preView" :alt="imageAlt" class="profile-picture">
        </div>
        <label for="image">이미지 추가</label>
        <input type="file" id="image" accept="image/*" @change="handleImageUpload">
      </div>
      <div class="form-group">
        <label for="id">아이디</label>
        <input type="text" id="id" name="id" required v-model="member.memberId" disabled />
      </div>
      <div class="form-group">
        <label for="nickname">닉네임</label>
        <input type="text" id="nickname" name="nickname" required v-model="member.nickname"
          :disabled="isDuplicateCheckNickname" />
        <button @click.prevent="onDuplicateCheckNickname">
          {{ isDuplicateCheckNickname ? "닉네임 수정" : "닉네임 중복검사" }}
        </button>
      </div>
      <div class="form-group">
        <label for="email">이메일</label>
        <input type="email" id="email" name="email" required v-model="member.email" :disabled="isAuthenticationEmail" />
        <button @click.prevent="onAuthenticationEmail">
          {{ isAuthenticationEmail ? "이메일 수정" : "이메일 인증" }}
        </button>
      </div>
      <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" v-model="member.password" />
      </div>
      <div class="form-group">
        <label for="confirm_password">비밀번호 확인</label>
        <input type="password" id="confirm_password" name="confirm_password" v-model="member.repassword" />
      </div>
      <button type="submit" class="btn">저장하기</button>
    </form>
  </div>
</template>

<style scoped>
.container {
  width: 500px;
  /* 폼 너비 조정 */
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px #f1f1f6;
}

.container h2 {
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 95%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.btn {
  display: block;
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #e1ccec;
  color: #fff;
  text-align: center;
  cursor: pointer;
}

.btn:hover {
  background-color: #e3effa;
}

.profile-picture {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-right: 20px;
}
</style>