package com.core.testspringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/", "login", "/join", "/joinProc").permitAll()
                                .requestMatchers("/admin").hasAnyRole("ADMIN") // 로그인 성공해도 role 해당 안될시 자원을 받지못함
                                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated()
                );

        http// csrf보호 비활성화 : 로그인 시 csrf도 같이 보내줘야 하지만 개발환경에서는 불편하기에 일단 비활성화함

                .csrf(auth -> auth.disable());

        http
                .formLogin(auth -> auth.loginPage("/login") // 로그인 페이지 url 지정(접근권한 없는 url 요청시 로그인 페이지로 리다이렉트)
                        .loginProcessingUrl("/loginProc") // 로그인 데이터 처리 url 지정
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
