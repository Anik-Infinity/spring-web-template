package com.anik.app.mapper;

import com.anik.app.config.CommonMapperConfig;
import com.anik.app.dto.auth.AuthenticationResponseDto;
import com.anik.app.dto.auth.UserRegistrationDto;
import com.anik.app.entity.user.User;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationDto registrationDto);

    AuthenticationResponseDto toDto(User user);
}
