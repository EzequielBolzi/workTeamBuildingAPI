package com.org.ezequielBolzi.auth;

import com.org.ezequielBolzi.config.JwtService;
import com.org.ezequielBolzi.customException.CustomEmailAlreadyExistsException;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {

        var user = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .edad(request.getEdad())
                .password(passwordEncoder.encode(request.getPassword()))
                .typeUser(com.org.ezequielBolzi.enums.User.EMPLOYEE)
                .build();

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new CustomEmailAlreadyExistsException("El correo electrónico ya está en uso");
        }
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
