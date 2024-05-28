package com.org.ezequielBolzi.auth;

import com.org.ezequielBolzi.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int edad;
    private String password;
}
