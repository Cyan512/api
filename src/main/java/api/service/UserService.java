package api.service;

import api.models.request.UpdateUserRequest;
import api.models.response.UserResponse;

public interface UserService {
    UserResponse getCurrentUser();
    
    UserResponse updateUser(UpdateUserRequest updateRequest);
}
