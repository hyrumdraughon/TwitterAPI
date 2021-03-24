package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.Credentials;
import com.cooksys.twitterapi.models.CredentialsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    Credentials dtoToEntity(CredentialsDto credentialsDto);

}
