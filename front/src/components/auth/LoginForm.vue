<script setup>
import { loginMember } from "@/api/auth";
import { ref } from "vue";

import kakaoIcon from "@/assets/kakao_login.svg";
import naverIcon from "@/assets/naver_login.svg";
import logo from "@/assets/home.png";
import show from "@/assets/show-password.png";
import hide from "@/assets/hide-password.png";
import Swal from "sweetalert2";
import { useRouter } from "vue-router";

const router = useRouter();

const member = ref({
  memberId: "",
  password: "",
});

const showPassword = ref(false);

// 비밀번호 보이기 버튼
function togglePasswordVisibility() {
  showPassword.value = !showPassword.value;
  const passwordInput = document.getElementById("password");
  passwordInput.type = showPassword.value ? "text" : "password";
}

function onSubmit() {
  console.log("로그인 요청", member.value);
  loginMember(
    member.value,
    (response) => {
      if (response.status == 200) {
        Swal.fire({
          icon: 'success',
          title: '로그인을 성공하셨습니다.',
          confirmButtonText: "확인",
          customClass: {
            title: "swal-title",
            text: "swal-text",
          },
        }).then(() => {
          localStorage.setItem("isLogin", true);
          location.reload();
          location.href = "/";
        });
      }
    },
    (error) => {
      showSwal("error", "아이디 또는 비밀번호를 다시 확인해주세요.", null);
      let msg = error.response.data.msg;
    }
  );
}

function googleLogin() {
  window.location.href =
    "https://accounts.google.com/o/oauth2/v2/auth?client_id=825050218681-l73t52gvefmitanqdcdfbt9nllmodg82.apps.googleusercontent.com&redirect_uri=http://localhost:3000/oauth/google&response_type=code&scope=openid%20email%20profile";
}

function kakaoLogin() {
  window.location.href =
    "https://kauth.kakao.com/oauth/authorize?client_id=dd52c20acaab911207c5de72b3089744&redirect_uri=http://localhost:3000/oauth/kakao&response_type=code&scope=account_email%20profile_nickname";
}

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
</script>

<template>
  <section>
    <div class="container">
      <div class="logo-container">
        <!-- 새로운 div를 추가하여 이미지를 포함할 컨테이너를 만듭니다 -->
        <img :src="logo" alt="Logo" />
        <!-- 이미지를 넣습니다 -->
      </div>
      <form class="login-form" @submit.prevent="onSubmit">
        <div class="form-group">
          <input
            type="text"
            id="username"
            name="username"
            required
            v-model="member.memberId"
            placeholder="아이디를 입력하세요."
          />
        </div>
        <div class="form-group password-input">
          <input
            type="password"
            id="password"
            name="password"
            required
            v-model="member.password"
            placeholder="비밀번호를 입력하세요."
          />
          <button
            type="button"
            class="toggle-password"
            @click="togglePasswordVisibility"
          >
            <img
              class="password-image"
              v-if="showPassword"
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
        <button type="submit" class="btn">로그인</button>
      </form>
      <div class="icon-container">
        <!-- <img :src="googleIcon" :alt="imageAlt" class="icon" @click="googleLogin"> -->
        <img
          :src="kakaoIcon"
          :alt="imageAlt"
          class="icon"
          @click="kakaoLogin"
        />
        <img :src="naverIcon" :alt="imageAlt" class="icon" />
      </div>
      <div class="link">
        <RouterLink :to="{ name: 'findid' }">아이디 찾기</RouterLink>
        <RouterLink :to="{ name: 'find-password' }">비밀번호 찾기</RouterLink>
        <RouterLink :to="{ name: 'signup' }">회원가입</RouterLink>
      </div>
    </div>
  </section>
</template>

<style scoped>
* {
  font-family: "Gaegu", cursive;
}

section {
  margin: 0 auto;
  width: 27%;
  display: flex;
  justify-content: center;
}

.container {
  width: 500px;
  margin: 20px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px #f1f1f6;
  text-align: center; /* 내부 요소들을 가로 방향으로 중앙 정렬 */
}

.container h2 {
  text-align: center;
}

.login-form {
  margin-top: 50px;
}

img {
  width: 250px;
  height: 200px;
}

.form-group {
  margin-bottom: 20px;
  text-align: center; /* input을 중앙으로 배치하기 위한 스타일 */
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
  margin: 0 auto; /* 중앙 정렬 */
  font-size: 20px;
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

.link {
  display: flex;
  justify-content: space-between;
  margin: auto 0;
  text-align: right;
  padding: 0 10px;
  margin-top: 30px;
  font-size: 20px;
}

.icon-container {
  display: flex;
  justify-content: space-around;
  margin-top: 30px;
}

.icon {
  margin: 10px;
  width: 50px;
  height: 50px;
  cursor: pointer;
}

a:hover {
  background-color: #e3effa;
  border-radius: 10px;
}

.password-image {
  width: 25px;
  height: 25px;
}
</style>

<style></style>
