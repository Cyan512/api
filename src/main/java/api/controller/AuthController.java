package api.controller;

import api.entity.UserEntity;
import api.mapper.AuthMappper;
import api.models.request.AuthenticationRequest;
import api.models.request.RegisterRequest;
import api.models.response.AuthenticationResponse;
import api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMappper authMappper;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        System.out.println("DTO fullName => " + registerRequest.getFullName());
        UserEntity userEntity = authMappper.registerRequestToUseEntity(registerRequest);
        System.out.println("ENTITY fullName => " + userEntity.getFullName());
        authService.registerUser(userEntity);
        //return ResponseEntity.ok(authService.registerUser(userEntity));
        return "Revisa tu correo para verificar tu cuenta";

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authService.authenticate(authenticationRequest));
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        authService.verifyEmail(token);
        return "Cuenta verificada correctamente";
    }
}
