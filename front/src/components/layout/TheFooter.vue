<script setup>
</script>

<template>
  <!-- Site footer -->
  <footer class="site-footer">
    <div class="container">
      <div class="row">
        <!-- Contact Info Section -->
        <div class="col-xs-6 col-md-3 contact-info">
          <h2>고객센터</h2>
          <p>대전광역시 유성구 덕명동 124</p>
          <p>전화번호: 010-6208-4786</p>
          <p>이메일: wjd4782@naver.com</p>
        </div>

        <div class="col-xs-6 col-md-3 footer-section quick-links">
          <h2>바로 가기</h2>
          <ul class="footer-links">
            <li><a href="/">홈</a></li>
            <li><a href="/review/list">리뷰</a></li>
            <li><a href="/auth/login">로그인</a></li>
            <li><a href="/auth/signup">회원가입</a></li>
          </ul>
        </div>
      </div>
      <hr>
    </div>
    
    <div class="container">
      <div class="row">
        <div class="col-md-8 col-sm-6 col-xs-12">
          <p class="copyright-text">
            made by. Dujeong
          </p>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.site-footer {
  background-color: white;
  padding: 25px 0 20px;
  font-size: 20px;
  line-height: 24px;
  text-align: center;
  color: black;
  border-top: 2px solid #f1ebeb;
}

.site-footer hr {
  border-top-color: #f1ebeb;
  opacity: 0.5;
}

.site-footer h2 {
  font-size: 20px;
  margin-top: 5px;
  margin-bottom: 20px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.site-footer a {
  color: #737373;
}

.site-footer a:hover {
  color: #3366cc;
  text-decoration: none;
}

.footer-section {
  margin-bottom: 20px;
}

.contact-info {
  margin-bottom: 40px; /* 연락처와 빠른 링크 사이의 여백 추가 */
}

.footer-links {
  padding-left: 0;
  list-style: none;
  text-align: center;
}

.footer-links li {
  display: inline-block;
  margin-right: 15px; /* 링크 사이 간격 조절 */
}

.footer-links a {
  color: #737373;
}

.footer-links a:active, .footer-links a:focus, .footer-links a:hover {
  color: #3366cc;
  text-decoration: none;
}

.copyright-text {
  margin: 0;
}

@media (max-width: 991px) {
  .site-footer [class^=col-] {
    margin-bottom: 30px;
  }
}

@media (max-width: 767px) {
  .site-footer {
    padding-bottom: 0;
  }
  .site-footer .copyright-text {
    text-align: center;
  }
  .footer-links {
    text-align: center;
  }
}

a:hover {
  background-color: #e3effa; /* 배경색도 변경 */
}
</style>
