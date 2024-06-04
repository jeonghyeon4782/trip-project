package com.dj.trip.global.config;

import com.dj.trip.global.security.UserDetailsService;
import com.dj.trip.global.security.filter.LoginFilter;
import com.dj.trip.global.security.filter.RefreshTokenFilter;
import com.dj.trip.global.security.filter.TokenCheckFilter;
import com.dj.trip.global.security.handler.LoginFailureHandler;
import com.dj.trip.global.security.handler.LoginSuccessHandler;
import com.dj.trip.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("------------------------------------------web configure------------------------------------------");

        return (web) -> web.ignoring()  // 정적 자원에 대한 요청을 무시
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        http.authenticationManager(authenticationManager);

        // LoginFilter 설정
        LoginFilter loginFilter = new LoginFilter("/auth/login");   // 로그인 URL
        loginFilter.setAuthenticationManager(authenticationManager);

        // LoginSuccessHandler, LoginFailureHandler
        LoginSuccessHandler successHandler = new LoginSuccessHandler(jwtUtil);
        LoginFailureHandler failureHandler = new LoginFailureHandler();
        // 로그인 성공시 Handler
        loginFilter.setAuthenticationSuccessHandler(successHandler);
        // 로그인 실패시 Handler
        loginFilter.setAuthenticationFailureHandler(failureHandler);

        // LoginFilter 위치 조정
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);   // UsernamePasswordAuthenticationFilter 보다 APILoginFilter 먼저 진행

        // api로 시작하는 모든 경로는 TokenCheckFilter 동작
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );

        // refreshToken 호출 처리
        http.addFilterBefore(new RefreshTokenFilter("/auth/refreshToken", jwtUtil),
                TokenCheckFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);  // CSRF 토큰 비활성화
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));    // SESSION 사용 X
        http.formLogin(AbstractHttpConfigurer::disable);    // form 로그인 사용 X
        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil) {
        return new TokenCheckFilter(jwtUtil);
    }
}