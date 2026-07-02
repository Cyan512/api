package com.api.auth.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.api.auth.dto.LoginRequest;
import com.api.auth.dto.RefreshRequest;
import com.api.auth.dto.RegisterRequest;
import com.api.auth.dto.TokenResponse;
import com.api.model.RefreshToken;
import com.api.model.Role;
import com.api.model.User;
import com.api.repository.RefreshTokenRepository;
import com.api.repository.RoleRepository;
import com.api.repository.UserRepository;
import com.api.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final String ROLE_USER = "ROLE_USER";

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El username ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
        }

        Set<Role> roles = resolveRoles(request.getRoles());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(user.getUsername(), roleNames);
        String refreshTokenRaw = jwtService.generateRefreshToken();

        saveRefreshToken(user.getId(), refreshTokenRaw);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRaw)
                .expiresIn(1800000L)
                .username(user.getUsername())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(user.getUsername(), roleNames);
        String refreshTokenRaw = jwtService.generateRefreshToken();

        // Revocar tokens anteriores y guardar el nuevo
        refreshTokenRepository.deleteByUserId(user.getId());
        saveRefreshToken(user.getId(), refreshTokenRaw);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRaw)
                .expiresIn(1800000L)
                .username(user.getUsername())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public TokenResponse refresh(RefreshRequest request) {
        String tokenHash = jwtService.hashToken(request.getRefreshToken());

        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));

        if (storedToken.isRevoked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token revocado");
        }

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirado");
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String accessToken = jwtService.generateAccessToken(user.getUsername(), roleNames);
        String refreshTokenRaw = jwtService.generateRefreshToken();

        // Rotar: revocar el anterior, guardar el nuevo
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
        saveRefreshToken(user.getId(), refreshTokenRaw);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenRaw)
                .expiresIn(1800000L)
                .username(user.getUsername())
                .roles(roleNames)
                .build();
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private Set<Role> resolveRoles(Set<String> requestedRoles) {
        Set<Role> roles = new HashSet<>();

        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(ROLE_USER).build()));
            roles.add(userRole);
            return roles;
        }

        for (String roleName : requestedRoles) {
            String formattedRole = roleName.toUpperCase();
            if (!formattedRole.startsWith("ROLE_")) {
                formattedRole = "ROLE_" + formattedRole;
            }
            String finalRoleName = formattedRole;
            Role role = roleRepository.findByName(formattedRole)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(finalRoleName).build()));
            roles.add(role);
        }

        return roles;
    }

    private void saveRefreshToken(Long userId, String rawToken) {
        String tokenHash = jwtService.hashToken(rawToken);
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtService.getRefreshExpirationMs() / 1000);

        RefreshToken rt = RefreshToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(expiresAt)
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(rt);
    }
}
