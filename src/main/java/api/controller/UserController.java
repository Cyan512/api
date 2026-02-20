package api.controller;

import api.models.response.UserResponse;
import api.models.request.UpdateUserRequest;
import api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Obtener usuario actual",
        description = "Retorna la información del usuario autenticado basado en el token JWT",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/currentUser")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(
        summary = "Actualizar perfil",
        description = "Permite actualizar la información del perfil del usuario autenticado (excepto email y rol)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest updateRequest) {
        return ResponseEntity.ok(userService.updateUser(updateRequest));
    }
}
