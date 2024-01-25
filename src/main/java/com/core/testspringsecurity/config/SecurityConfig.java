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

        http // 로그인 폼 관련 설정(커스텀 로그인)
                .formLogin(
                        auth -> auth
                                .loginPage("/login") // 로그인 페이지 url 지정(접근권한 없는 url 요청시 로그인 페이지로 리다이렉트)
                                .loginProcessingUrl("/loginProc") // 로그인 데이터 처리 url 지정
                                .permitAll()
                );

        http // 다중 로그인 설정
                .sessionManagement(
                        auth -> auth
                                .maximumSessions(1) // 한 아이디당 중복 로그인 허용 개수 설정
                                .maxSessionsPreventsLogin(true) // 중복 로그인 개수 초과시 처리방법
                        // (true : 새로운 로그인 차단 / false : 초과시 기존 세션 하나 삭제)
                );

        http // 세션 고정 보호 : 세션 쿠키 탈취를 통해 user 정보가 탈취될 수 있는 세션 고정 공격을 보호하기 위한 방법
                // sessionManagement().sessionFixation().none() : 로그인 시 세션 정보 변경 안함
                // sessionManagement().sessionFixation().newSession() : 로그인 시 세션 새로 생성
                // sessionManagement().sessionFixation().changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경(JSESSIONID 변경)
                // sessionManagement().sessionFixation().migrateSession() : 로그인 시 새로운 세션 생성 및 기존 세션을 새로운 세션으로 마이그레이션(디폴트)
                .sessionManagement(
                        auth -> auth.sessionFixation().changeSessionId()
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
