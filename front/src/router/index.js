import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: () => import("@/views/TheMainView.vue"),
      redirect: { name: "main-view" },
      children: [
        {
          path: "mainview",
          name: "main-view",
          component: () => import("@/components/main/Main.vue"),
        },
      ]
    },
    {
      path: "/review",
      name: "review",
      // component: TheReviewView,
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("@/views/TheReviewView.vue"),
      redirect: { name: "review-list" },
      children: [
        {
          path: "list",
          name: "review-list",
          component: () => import("@/components/review/ReviewList.vue"),
        },
        {
          path: "view/:reviewid",
          name: "review-view",
          component: () => import("@/components/review/ReviewDetail.vue"),
        },
        {
          path: "write",
          name: "review-write",
          component: () => import("@/components/review/ReviewWrite.vue"),
        },
        {
          path: "modify/:reviewid",
          name: "review-modify",
          component: () => import("@/components/review/ReviewModify.vue"),
        }
      ]
    },
    {
      path: '/auth',
      name: 'auth',
      component: () => import("@/views/TheAuthView.vue"),
      redirect: { name: "login" },
      children: [
        {
          path: "login",
          name: "login",
          component: () => import("@/components/auth/LoginForm.vue"),
        },
        {
          path: "signup",
          name: "signup",
          component: () => import("@/components/auth/SignupForm.vue"),
        },
        {
          path: "findid",
          name: "findid",
          component: () => import("@/components/auth/FindIdForm.vue"),
        },
        {
          path: '/oauth/:oauth_server_type',
          name: 'oauth',
          component: () => import("@/components/auth/OauthRedirect.vue"),
        },
      ]
    },
    {
      path: '/member',
      name: 'mypage',
      component: () => import("@/views/TheMypageView.vue"),
      redirect: { name: "mypage-main" },
      children: [
        {
          path: "main",
          name: "mypage-main",
          component: () => import("@/components/mypage/MypageMain.vue"),
        },
        {
          path: "modify",
          name: "member-modify",
          component: () => import("@/components/mypage/MemberModify.vue"),
        },
        {
          path: "findid",
          name: "findid",
          component: () => import("@/components/auth/FindIdForm.vue"),
        },
        {
          path: "find-password",
          name: "find-password",
          component: () => import("@/components/auth/FindPasswordForm.vue"),
        },
        {
          path: "write/:boardLogId",
          name: "write-board-review",
          component: () => import("@/components/mypage/BoardReviewWrite.vue"),
        }
      ]
    },
    {
      path: "/board/:sidoId/:imageId/:name",
      name: "board",
      component: () => import("@/components/board/BoardMain.vue"),
    },
  ]
})

export default router