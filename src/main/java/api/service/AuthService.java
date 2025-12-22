package api.service;

import api.entity.UserEntity;
import api.models.request.AuthenticationRequest;
import api.models.response.AuthenticationResponse;

public interface AuthService {
    void registerUser(UserEntity userEntity);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    void verifyEmail(String token);
}
