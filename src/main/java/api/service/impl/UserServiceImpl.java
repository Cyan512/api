package api.service.impl;

import api.config.security.AuthUser;
import api.mapper.UserMapper;
import api.models.response.UserResponse;
import api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUser authUser) {
            return userMapper.toResponse(authUser.getUserEntity());
        }

        throw new RuntimeException("Error al obtener el usuario autenticado");
    }
}
