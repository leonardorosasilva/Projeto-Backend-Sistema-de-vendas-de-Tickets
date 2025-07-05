package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.auth.LoginRequestDTO;
import com.example.ticketmaster.dto.auth.RegisterRequestDTO;
import com.example.ticketmaster.dto.auth.AuthResponseDTO;
import com.example.ticketmaster.model.Role;
import com.example.ticketmaster.model.User;
import com.example.ticketmaster.repository.RoleRepository;
import com.example.ticketmaster.repository.UserRepository;
import com.example.ticketmaster.security.JwtTokenProvider;
import com.example.ticketmaster.exception.BusinessException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponseDTO registerUser(RegisterRequestDTO registerRequest) {*+
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BusinessException("Nome de usuário já está em uso!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email já está em uso!");
        }


        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));


        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new BusinessException("Role de usuário não encontrada."));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponseDTO(token, "Usuário registrado com sucesso!");
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticateUser(LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponseDTO(token, "Login realizado com sucesso!");
    }
}