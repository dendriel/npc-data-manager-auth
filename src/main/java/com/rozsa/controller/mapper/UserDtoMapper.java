package com.rozsa.controller.mapper;

import com.rozsa.dto.UserDto;
import com.rozsa.repository.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    User toUser(UserDto user);

    UserDto fromUser(User user);

    List<UserDto> fromUsers(List<User> users);
}
