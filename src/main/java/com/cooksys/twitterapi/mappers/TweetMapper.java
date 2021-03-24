package com.cooksys.twitterapi.mappers;

import com.cooksys.twitterapi.entities.Tweet;
import com.cooksys.twitterapi.models.TweetDto;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

    Tweet dtoToEntity(TweetDto tweetDto);

    TweetDto entityToDto(Tweet tweet);

    List<TweetDto> entitiesToDtoList(List<Tweet> tweets);
}
