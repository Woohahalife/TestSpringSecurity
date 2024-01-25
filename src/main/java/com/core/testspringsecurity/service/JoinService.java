package com.core.testspringsecurity.service;

import com.core.testspringsecurity.entity.UserEntity;
import com.core.testspringsecurity.model.JoinDTO;
import com.core.testspringsecurity.model.Role;
import com.core.testspringsecurity.repository.UserRepository;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JoinService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDTO joinDTO) {

        // db에 해당 유저정보 있는지 확인(회원정보 존재 : true)
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());

        if(isUser) {
            return;
        }

        UserEntity user = new UserEntity();

        user.setUsername(joinDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); // 암호 해시화(암호화)
        user.setRole(Role.ADMIN);

        userRepository.save(user);

    }
}
