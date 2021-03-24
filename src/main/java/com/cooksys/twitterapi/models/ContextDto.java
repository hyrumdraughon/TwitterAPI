package com.cooksys.twitterapi.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {

    TweetDto target;

    List<TweetDto> before;

    List<TweetDto> after;

}
