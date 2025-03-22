package com.smart.inventory.user_service.application.mapper;


import org.mapstruct.Mapper;
import com.smart.inventory.user_service.domain.model.User;
import com.smart.inventory.user_service.presentation.dto.UserDto;
import com.smart.inventory.user_service.presentation.request.UserRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Request → Domain
    User toDomain(UserRequest request);

    // Domain → DTO
    UserDto toDTO(User user);
}