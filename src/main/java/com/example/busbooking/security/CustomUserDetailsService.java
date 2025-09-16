package com.example.busbooking.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user=userRepository.findByEmail(email)
        .orElseThrow(()->new UsernameNotFoundException("User not found with email :"+email));
         
        return new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(), 
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
