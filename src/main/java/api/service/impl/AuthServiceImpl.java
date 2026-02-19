package api.service.impl;

import api.config.security.JwtService;
import api.entity.UserEntity;
import api.enums.UserRole;
import api.models.request.AuthenticationRequest;
import api.models.response.AuthenticationResponse;
import api.repository.UserRepository;
import api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;

    @Override
    public void registerUser(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya esta registrado");

        String token = UUID.randomUUID().toString();

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setVerificationToken(token);

        userRepository.save(userEntity);

        //String jwtToken = jwtService.generateToken(userEntity.getEmail());
        emailService.sendVerificationEmail(userEntity.getEmail(), token);
        //return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        UserEntity user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Debes verificar tu correo antes de iniciar sesión");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());

            return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void verifyEmail(String token) {
        UserEntity user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        user.setEnabled(true);
        user.setVerificationToken(null);

        userRepository.save(user);
    }
}
