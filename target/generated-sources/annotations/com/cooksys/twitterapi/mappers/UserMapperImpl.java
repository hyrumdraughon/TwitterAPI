package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.Credentials;
import com.cooksys.twitterapi.entities.User;
import com.cooksys.twitterapi.models.CreateUserDto;
import com.cooksys.twitterapi.models.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-18T15:35:44-0700",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private CredentialsMapper credentialsMapper;
    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public UserDto entityToDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        String username = userCredentialsUsername( user );
        if ( username != null ) {
            userDto.setUsername( username );
        }
        userDto.setProfile( profileMapper.entityToDto( user.getProfile() ) );
        userDto.setJoined( user.getJoined() );

        return userDto;
    }

    @Override
    public User dtoToEntity(CreateUserDto createUserDto) {
        if ( createUserDto == null ) {
            return null;
        }

        User user = new User();

        user.setCredentials( credentialsMapper.dtoToEntity( createUserDto.getCredentials() ) );
        user.setProfile( profileMapper.dtoToEntity( createUserDto.getProfile() ) );

        return user;
    }

    @Override
    public List<UserDto> entitiesToDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    private String userCredentialsUsername(User user) {
        if ( user == null ) {
            return null;
        }
        Credentials credentials = user.getCredentials();
        if ( credentials == null ) {
            return null;
        }
        String username = credentials.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
