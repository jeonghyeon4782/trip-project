<script setup>
import { findMemberId } from '@/api/auth';
import { ref } from 'vue';
import Swal from "sweetalert2";
import logo from "@/assets/home.png";
import show from "@/assets/show-password.png";
import hide from "@/assets/hide-password.png";


const data = ref({
    email: "",
});

const onFindMemberId = () => {
    findMemberId(
        data.value.email,
    (response) => {
        console.log(response.data.msg)
        showSwal("success", response.data.msg, "당신의 아이디는 <" + response.data.data + ">입니다.");
    },
    (error) => {
        console.log(error.response.data.msg)
        showSwal("error", error.response.data.msg, null);
    }
  );
}

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: '확인',
  });
};

</script>

<template>
    <section>
      <div class="container">
        <div class="logo-container">
          <img :src="logo" alt="Logo" />
        </div>
        <form class="login-form" @submit.prevent="onFindMemberId">
          <div class="form-group">
            <input
              type="text"
              id="username"
              name="username"
              required
              v-model="data.email"
              placeholder="이메일을 입력하세요"
            />
          </div>
          <button type="submit" class="btn">아이디 찾기</button>
        </form>
        <div class="link">
            <RouterLink :to="{ name: 'find-password' }">비밀번호 찾기</RouterLink>
          <RouterLink :to="{ name: 'login' }">로그인</RouterLink>
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
  margin-top: 50px;
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
