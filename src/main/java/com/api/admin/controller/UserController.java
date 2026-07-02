package com.api.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.admin.dto.UserRoleUpdateRequest;
import com.api.admin.service.UserService;
import com.api.dto.ApiResponse;
import com.api.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> listar() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users, "Usuarios obtenidos exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> obtenerPorId(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user, "Usuario encontrado exitosamente"));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<User>> actualizarRoles(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequest request) {
        var user = userService.updateRoles(id, request.getRoles());
        return ResponseEntity.ok(ApiResponse.success(user, "Roles actualizados exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario eliminado exitosamente"));
    }
}
