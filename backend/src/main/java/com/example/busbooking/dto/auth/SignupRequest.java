package com.example.busbooking.dto.auth;


import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min=6)
    private String password;

    @NotBlank
    private String phone;
}
