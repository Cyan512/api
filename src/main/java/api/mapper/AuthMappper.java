package api.mapper;

import api.entity.UserEntity;
import api.models.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMappper {

    UserEntity registerRequestToUseEntity(RegisterRequest registerRequest);
}
