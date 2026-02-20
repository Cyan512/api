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
        // Recargamos la entidad desde la DB para asegurarnos de tener la versión más reciente (persistente)
        UserEntity userEntity = userRepository.findById(authUser.getUserEntity().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Validar Username si viene en el request
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isBlank()) {
            // Verificar si cambió
            if (!userEntity.getUsername().equals(updateRequest.getUsername())) {
                // Verificar si ya existe en otro usuario
                Optional<UserEntity> existingUser = userRepository.findByUsername(updateRequest.getUsername());
                if (existingUser.isPresent()) {
                    throw new RuntimeException("El nombre de usuario '" + updateRequest.getUsername() + "' ya está en uso");
                }
                userEntity.setUsername(updateRequest.getUsername());
            }
        }

        // 2. Validar y Encriptar Password si viene en el request
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isBlank()) {
           userEntity.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        // 3. Mapear el resto de campos (fullName, etc)
        // Mapeamos solo los campos que vengan en el request, ignorando nulos (configurado en el Mapper)
        userMapper.updateUserFromRequest(updateRequest, userEntity);
        
        // Guardamos los cambios
        UserEntity updatedUser = userRepository.save(userEntity);
        
        return userMapper.toResponse(updatedUser);
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
