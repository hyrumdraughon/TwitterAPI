package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.models.CreateUserDto;
import com.cooksys.twitterapi.models.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = {CredentialsMapper.class, ProfileMapper.class})
public interface UserMapper {

    @Mapping(target = "username", source = "credentials.username")
    UserDto entityToDto(User user);

    User dtoToEntity(CreateUserDto createUserDto);

    List<UserDto> entitiesToDtos(List<User> users);
}
