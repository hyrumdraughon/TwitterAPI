package com.cooksys.twitterapi.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.twitterapi.entities.Hashtag;
import com.cooksys.twitterapi.models.HashtagDto;


@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagDto entityToDto(Hashtag hashtag);
    
    List<HashtagDto> entitiesToDtos(List<Hashtag> hashtags);

}
