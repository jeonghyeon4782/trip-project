import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// import { useKakao } from 'vue3-kakao-maps/@utils';
import { Carousel, Slide } from 'vue3-carousel';


// useKakao(`${import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY}`);

const app = createApp(App)

app.use(router)

app.mount('#app')

app.component('Carousel', Carousel);
app.component('Slide', Slide);