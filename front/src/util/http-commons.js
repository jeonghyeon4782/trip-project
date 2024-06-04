import router from "@/router";
import axios from "axios";

const { VITE_VUE_API_URL, VITE_POSTMAN_TEST_API_URL } = import.meta.env;

axios.defaults.withCredentials = true;

// local vue api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8"
    }
  });

  instance.interceptors.response.use(
    response => {
      return response;
    },
    async error => {
      const originalRequest = error.config;
      if (error.response && error.response.status === 403 && !originalRequest._retry) {
        originalRequest._retry = true;
        try {
          const response = await axios.post(VITE_VUE_API_URL + '/auth/refreshToken');
          console.log("엑세스 토큰 재 발급" + response.data);
          return axios(originalRequest);
        } catch (refreshTokenError) {
          localStorage.setItem('isLogin', false);
          console.log(error.response.data.msg)
          let msg = "로그인 필요";
          alert(msg)
          location.reload();
          location.href = '/auth';
          return Promise.reject(refreshTokenError);
        }
      }
      return Promise.reject(error);
    }
  )
  return instance;
}

export { localAxios };

