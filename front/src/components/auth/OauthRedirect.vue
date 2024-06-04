<script setup>
import { oauthMember } from "@/api/auth";
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import Swal from "sweetalert2";

const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get("code");
console.log("인증코드: ", code);

const route = useRoute();
const router = useRouter();

const { oauth_server_type } = route.params;

const requestData = ref({
  code: code,
  oauthServerType: oauth_server_type,
});

const showSwal = (icon, title, text) => {
  Swal.fire({
    icon: icon,
    title: title,
    text: text,
    confirmButtonText: "확인",
  });
};

oauthMember(
  requestData.value,
  (response) => {
    if (response.status == 200) {
      console.log(response.data.data.isFirst);
      if (response.data.data.isFirst) {
        showSwal("success", "회원가입을 진행해주세요.");
        router.replace({
          name: "signup",
          query: {
            email: response.data.data.email,
            oauthServerType: response.data.data.oauthServerType,
          },
        });
      } else {
        Swal.fire({
          icon: 'success',
          title: '로그인을 성공하셨습니다.',
          confirmButtonText: "확인",
        }).then(() => {
          localStorage.setItem("isLogin", true);
          location.reload();
          location.href = "/";
        });
      }
    }
  },
  (error) => {
    console.log(error);
  }
);
</script>

<template></template>

<style scoped></style>
