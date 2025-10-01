package com.example.policlicabine.mapper;

import com.example.policlicabine.dto.UserDto;
import com.example.policlicabine.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
