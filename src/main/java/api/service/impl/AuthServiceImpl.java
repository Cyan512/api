package api.service.impl;

import api.config.security.JwtService;
import api.entity.UserEntity;
import api.enums.UserRole;
import api.models.request.AuthenticationRequest;
import api.models.request.RegisterRequest;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerUser(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya esta registrado");

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(UserRole.USER);

        userRepository.save(userEntity);

        String jwtToken = jwtService.generateToken(userEntity.getEmail());

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        String jwtToken = jwtService.generateToken(authenticationRequest.getEmail());

        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
