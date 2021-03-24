package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.Profile;
import com.cooksys.twitterapi.models.ProfileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToDto(Profile profile);

    Profile dtoToEntity(ProfileDto profileDto);

}
