package com.core.testspringsecurity.security;

import com.core.testspringsecurity.entity.UserEntity;
import com.core.testspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username is not found"));

//        if(userEntity != null) {
//            return new CustomUserDetails(userEntity);
//        }

        System.out.println(userEntity.getRole().toString());

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().toString())
                .build();
    }
}
