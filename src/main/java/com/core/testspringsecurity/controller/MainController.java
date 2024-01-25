package com.core.testspringsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String MainP(Model model) {

        // 로그인 진행 후 사용자 정보는 SecurityContextHolder에 의해 서버 세션에 관리됨

        // 시큐리티에서 로그인한 사용자에 대해 세션에 저장되어있는 아이디를 확인
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        // 시큐리티에서 로그인한 사용자에 대해 세션에 저장되어있는 role을 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        System.out.println("role = " + role);

        model.addAttribute("id", id);
        model.addAttribute("role", role);
        return "main";
    }
}
