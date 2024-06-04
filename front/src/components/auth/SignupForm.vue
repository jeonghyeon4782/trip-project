<script setup>
import {
  registMember,
  duplicateCheckMemberId,
  duplicateCheckNickname,
  authenticationEmail,
  resendAuthenticationKey,
  deleteAuthenticationKey,
  checkAuthenticationKey,
} from "@/api/auth";
import { ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import Swal from "sweetalert2";
import logo from "@/assets/home.png";
import show from "@/assets/show-password.png";
import hide from "@/assets/hide-password.png";

const route = useRoute();
const router = useRouter();

const { email, oauthServerType } = route.query;
console.log(email, oauthServerType);

const member = ref({
  memberId: "",
  password: "",
  repassword: "",
  nickname: "",
  email: "",
  oauthServiceType: "GENERAL",
});
// 중복검사 여부
const isDuplicateCheckMemberId = ref(false);
const isDuplicateCheckNickname = ref(false);
// 이메일 인증 여부
const isAuthenticationEmail = ref(false);

if (email) {
  member.value.email = email;
  isAuthenticationEmail.value = true;
}

if (oauthServerType) {
  member.value.oauthServiceType = oauthServerType;
}

const memberIdErrMsg = ref("");
const nicknameErrMsg = ref("");
const emailErrMsg = ref("");
const passwordErrMsg = ref("");

watch(
  () => member.value.memberId,
  (value) => {
    let len = value.length;
    const regex = /^[a-z0-9]+$/;

    if (len < 6 || len > 12) {
      memberIdErrMsg.value = "아이디는 6자 이상 12자 이하입니다";
    } else if (!regex.test(value)) {
      memberIdErrMsg.value =
        "아이디는 영어 소문자와 숫자로만 허용되며, 공백이 없어야 합니다.";
    } else memberIdErrMsg.value = "";
  },
  { immediate: true }
);
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

    if (password != repassword) {
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
  if (!isDuplicateCheckMemberId.value) {
    showSwal("error", "아이디 중복검사를 해주세요.", null);
  } else if (!isDuplicateCheckNickname.value) {
    showSwal("error", "닉네임 중복검사를 해주세요.", null);
  } else if (!isAuthenticationEmail.value) {
    showSwal("error", "이메일 인증을 해주세요.", null);
  } else if (nicknameErrMsg.value) {
    showSwal("error", nicknameErrMsg.value, null);
  } else if (emailErrMsg.value) {
    showSwal("error", emailErrMsg.value, null);
  } else if (passwordErrMsg.value) {
    showSwal("error", passwordErrMsg.value, null);
  } else if (memberIdErrMsg.value) {
    showSwal("error", memberIdErrMsg.value, null);
  } else {
    signup();
  }
}

function signup() {
  console.log("회원가입 요청", member.value);
  registMember(
    member.value,
    (response) => {
      let msg = "회원가입 처리시 문제 발생했습니다.";
      if (response.status == 201) {
        msg = "회원가입이 완료되었습니다.";
        router.replace({ name: "login" });
      }
      showSwal('success', msg, null);
    },
    (error) => showSwal('error', error.response.data.msg, null)
  );
}

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: "확인"
  });
};

const onDuplicateCheckMemberId = () => {
  if (!isDuplicateCheckMemberId.value) {
    duplicateCheckMemberId(
      member.value.memberId,
      (response) => {
        showSwal("success", response.data.msg, null);
        isDuplicateCheckMemberId.value = true;
      },
      (error) => showSwal("error", error.response.data.msg, null)
    );
  } else {
    isDuplicateCheckMemberId.value = false;
  }
};

const onDuplicateCheckNickname = () => {
  if (!isDuplicateCheckNickname.value) {
    duplicateCheckNickname(
      member.value.nickname,
      (response) => {
        showSwal("success", response.data.msg, null);
        isDuplicateCheckNickname.value = true;
      },
      (error) => showSwal("error", error.response.data.msg, null)
    );
  } else {
    isDuplicateCheckNickname.value = false;
  }
};

const onAuthenticationEmail = () => {
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
              <div style="font-family: "Gaegu", cursive;">
                인증번호를 입력해주세요.<br/><br/>
                남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
                <div style="display: flex; align-items: center;">
                  <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호 입력" style="flex: 1; margin-right: 10px;">
                  <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
                </div>
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
        <div style="font-family: 'Gaegu', cursive;">
      인증번호를 입력해주세요.<br/><br/>
      남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
      <div style="display: flex; align-items: center;">
        <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호 입력" style="flex: 1; margin-right: 10px;">
        <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
      </div>
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
              showSwal("success", "이메일 인증이 완료되었습니다.", null);
              isAuthenticationEmail.value = true;
            },
            (error) => {
              if (error.response.data.data == 1) {
                Swal.update({
                  html: `
              인증번호를 입력해주세요.<br/><br/>
              남은 시간: <b id="timer">${remainingTime}</b> 초<br/><br/>
              <div style="display: flex; align-items: center;">
                <input type="text" id="verification-code" class="swal2-input" placeholder="인증번호를 입력해주세요." style="flex: 1; margin-right: 10px;">
                <button id="resend-button" class="swal2-confirm swal2-styled">재발송</button>
              </div>
              <div id="error-message" style="color: red;">인증번호가 틀렸습니다.</div>
            `,
                });
                addResendButtonListener();
              } else if (error.response.data.data == 2) {
                showSwal("error", "만료된 인증번호 입니다.", null);
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
      showSwal("error", error.response.data.msg, null);
    }
  );
};

const showPassword1 = ref(false);

// 비밀번호 보이기 버튼
function togglePasswordVisibility1() {
  showPassword1.value = !showPassword1.value;
  const passwordInput = document.getElementById("password");
  passwordInput.type = showPassword1.value ? "text" : "password";
}

const showPassword2 = ref(false);

// 비밀번호 보이기 버튼
function togglePasswordVisibility2() {
  showPassword2.value = !showPassword2.value;
  const passwordInput = document.getElementById("confirm_password");
  passwordInput.type = showPassword2.value ? "text" : "password";
}
</script>

<template>
  <section>
    <div class="container">
      <div class="logo-container">
        <!-- 새로운 div를 추가하여 이미지를 포함할 컨테이너를 만듭니다 -->
        <img :src="logo" alt="Logo" />
        <!-- 이미지를 넣습니다 -->
      </div>
      <form class="signup-form" @submit.prevent="onSubmit">
        <div class="form-group val-input">
          <input
            type="text"
            id="id"
            name="id"
            required
            v-model="member.memberId"
            :disabled="isDuplicateCheckMemberId"
            placeholder="아이디를 입력하세요."
          />
          <button class="check-val" @click.prevent="onDuplicateCheckMemberId">
            {{ isDuplicateCheckMemberId ? "수정" : "중복검사" }}
          </button>
        </div>
        <div class="form-group val-input">
          <input
            type="text"
            id="nickname"
            name="nickname"
            required
            v-model="member.nickname"
            :disabled="isDuplicateCheckNickname"
            placeholder="닉네임을 입력하세요."
          />
          <button class="check-val" @click.prevent="onDuplicateCheckNickname">
            {{ isDuplicateCheckNickname ? "수정" : "중복검사" }}
          </button>
        </div>
        <div class="form-group val-input">
          <input
            type="email"
            id="email"
            name="email"
            required
            v-if="!email"
            v-model="member.email"
            :disabled="isAuthenticationEmail"
            placeholder="이메일을 입력하세요."
          />
          <input
            type="email"
            id="email"
            name="email"
            placeholder="이메일을 입력하세요."
            required
            v-else
            :value="email"
            disabled
          />
          <button
            class="check-val"
            v-if="!isAuthenticationEmail"
            @click.prevent="onAuthenticationEmail"
          >
            인증
          </button>
        </div>
        <div class="form-group password-input">
          <input
            type="password"
            id="password"
            name="password"
            placeholder="비밀번호를 입력하세요."
            required
            v-model="member.password"
          />
          <button
            type="button"
            class="toggle-password"
            @click="togglePasswordVisibility1"
          >
            <img
              class="password-image"
              v-if="showPassword1"
              :src="hide"
              alt="Show Password"
            />
            <img
              class="password-image"
              v-else
              :src="show"
              alt="Hide Password"
            />
          </button>
        </div>
        <div class="form-group password-input">
          <input
            type="password"
            id="confirm_password"
            name="confirm_password"
            placeholder="비밀번호를 다시 한번 입력하세요."
            required
            v-model="member.repassword"
          />
          <button
            type="button"
            class="toggle-password"
            @click="togglePasswordVisibility2"
          >
            <img
              class="password-image"
              v-if="showPassword2"
              :src="hide"
              alt="Show Password"
            />
            <img
              class="password-image"
              v-else
              :src="show"
              alt="Hide Password"
            />
          </button>
        </div>
        <button type="submit" class="btn">회원가입</button>
        <div class="link">
          <RouterLink :to="{ name: 'findid' }">아이디 찾기</RouterLink>
          <RouterLink :to="{ name: 'find-password' }">비밀번호 찾기</RouterLink>
          <RouterLink :to="{ name: 'login' }">로그인</RouterLink>
        </div>
      </form>
    </div>
  </section>
</template>

<style scoped>
* {
  font-family: "Gaegu", cursive;
}

.container {
  width: 500px;
  margin: 10px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px #f1f1f6;
  text-align: center; /* 내부 요소들을 가로 방향으로 중앙 정렬 */
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
  width: 70%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 20px;
}

.btn {
  margin-top: 10px;
  display: block;
  width: 80%; /* 입력창과 동일한 너비로 조정 */
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
  font-weight: bo;
}

.btn:hover {
  background-color: #e3effa;
}

a:hover {
  background-color: #e3effa;
  border-radius: 10px;
}

.back-link {
  margin-top: 10px;
  /* 로그인 링크 위쪽 간격 조정 */
  margin-left: 10px;
  text-align: center;
}

img {
  width: 250px;
  height: 200px;
}

.val-input {
  position: relative;
}

.check-val {
  position: absolute;
  top: 51%;
  right: 70px; /* 오른쪽 여백 조정 */
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 15px;
  color: red;
}

.password-input {
  position: relative;
}

.toggle-password {
  position: absolute;
  top: 51%;
  right: 70px; /* 오른쪽 여백 조정 */
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
}

.password-image {
  width: 25px;
  height: 25px;
}

.link {
  display: flex;
  justify-content: space-between;
  margin: auto 0;
  text-align: right;
  padding: 0 10px;
  margin-top: 50px;
  font-size: 20px;
}

.signup-form {
  margin-top: 50px;
}
</style>

<style></style>
