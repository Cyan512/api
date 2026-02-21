package api.service.impl;

import api.config.security.AuthUser;
import api.entity.UserEntity;
import api.mapper.UserMapper;
import api.models.request.UpdateUserRequest;
import api.models.response.UserResponse;
import api.repository.UserRepository;
import api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getCurrentUser() {
        AuthUser authUser = getAuthenticatedUser();
        return userMapper.toResponse(authUser.getUserEntity());
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateRequest) {
        AuthUser authUser = getAuthenticatedUser();
        UserEntity userEntity = userRepository.findById(authUser.getUserEntity().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isBlank()) {
            if (!userEntity.getUsername().equals(updateRequest.getUsername())) {
                Optional<UserEntity> existingUser = userRepository.findByUsername(updateRequest.getUsername());
                if (existingUser.isPresent()) {
                    throw new RuntimeException("El nombre de usuario '" + updateRequest.getUsername() + "' ya está en uso");
                }
                userEntity.setUsername(updateRequest.getUsername());
            }
        }

        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank()) {
           userEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userMapper.updateUserFromRequest(updateRequest, userEntity);
        
        UserEntity updatedUser = userRepository.save(userEntity);
        
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return userMapper.toResponse(user);
    }

    private AuthUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUser authUser) {
            return authUser;
        }

        throw new RuntimeException("Error al obtener el usuario autenticado");
    }
}
