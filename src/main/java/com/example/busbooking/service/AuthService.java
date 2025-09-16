package com.example.busbooking.service;

// import javax.management.RuntimeErrorException;

// import your own User entity instead of the Spring SecurityProperties.User
// import com.example.busbooking.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.busbooking.dto.auth.AuthResponse;
import com.example.busbooking.dto.auth.LoginRequest;
import com.example.busbooking.dto.auth.SignupRequest;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;
import com.example.busbooking.security.JwtService;

// import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse signup(SignupRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("email alreday exists");
        }
            User user =new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhone((request.getPhone()));
            userRepository.save(user);

            UserDetails userDetails=userDetailsService.loadUserByUsername(user.getEmail());
            String token =jwtService.generateToken(userDetails);

            return new AuthResponse(token,user.getEmail(),user.getName());
        
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails=userDetailsService.loadUserByUsername(request.getEmail());
        User user=userRepository.findByEmail(request.getEmail())
        .orElseThrow(()-> new RuntimeException("User not found"));

        String token=jwtService.generateToken(userDetails);
        return new AuthResponse(token,user.getEmail(),user.getName());
    }
}
