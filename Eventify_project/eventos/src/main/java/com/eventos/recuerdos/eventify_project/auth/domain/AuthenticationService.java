package com.eventos.recuerdos.eventify_project.auth.domain;

import com.eventos.recuerdos.eventify_project.HelloEmailEvent;
import com.eventos.recuerdos.eventify_project.auth.dto.JwtAuthenticationResponse;
import com.eventos.recuerdos.eventify_project.auth.dto.LoginResponseDto;
import com.eventos.recuerdos.eventify_project.auth.dto.SigninRequest;
import com.eventos.recuerdos.eventify_project.auth.dto.UserSignupRequestDto;
import com.eventos.recuerdos.eventify_project.exception.ResourceConflictException;
import com.eventos.recuerdos.eventify_project.securityconfig.domain.JwtService;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.user.domain.Role;
import com.eventos.recuerdos.eventify_project.user.infrastructure.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public JwtAuthenticationResponse signup(UserSignupRequestDto requestDto) {
        // Check if the username or email is already in use
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new ResourceConflictException("El nombre de usuario ya está en uso.");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResourceConflictException("El correo electrónico ya está en uso.");
        }

        // Verify that passwords match
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        // Create and configure the new UserAccount from the DTO
        UserAccount user = new UserAccount();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(Role.USER);  // Default role as USER

        // Save the user to the database
        userRepository.save(user);

        // Generate JWT token
        var jwt = jwtService.generateToken(user);

        // Create response with token and userId
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, user.getId());

        // Publish event to send a welcome email
        applicationEventPublisher.publishEvent(new HelloEmailEvent(user.getEmail()));

        return response;
    }

    public LoginResponseDto signin(SigninRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        var jwt = jwtService.generateToken(user);

        return new LoginResponseDto(jwt);
    }
}

